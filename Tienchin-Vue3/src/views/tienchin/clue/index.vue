<template>
    <div class="app-container">
       <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
          <el-form-item label="客户姓名" prop="name">
             <el-input
                v-model="queryParams.name"
                placeholder="客户姓名"
                clearable
                style="width: 100px"
                @keyup.enter="handleQuery"
             />
          </el-form-item>
          <el-form-item label="客户手机号码" prop="phone">
             <el-input
                v-model="queryParams.phone"
                placeholder="手机号码"
                clearable
                style="width: 150px"
                @keyup.enter="handleQuery"
             />
          </el-form-item>
          <el-form-item label="线索归属人" prop="owner">
             <el-input
                v-model="queryParams.owner"
                placeholder="线索归属人"
                clearable
                style="width: 100px"
                @keyup.enter="handleQuery"
             />
          </el-form-item>
          <el-form-item label="渠道来源" prop="channelId">
            <el-select v-model="queryParams.channelId" class="m-2" placeholder="请选择获取客户的渠道" clearable>
                <el-option
                    v-for="item in channelList"
                    :key="item.channelId"
                    :label="item.channelName"
                    :value="item.channelId"
                />
              </el-select>
          </el-form-item>
          <el-form-item label="线索状态" prop="status">
            <el-select v-model="queryParams.status" class="m-2" placeholder="线索状态" clearable>
                <el-option
                    v-for="item in clue_status"
                    :key="item.value"
                    :label="item.label"
                    :value="parseInt(item.value)"
                />
              </el-select>
          </el-form-item>
          <el-form-item label="线索下次跟进日期" prop="status">
            <el-date-picker
              v-model="dateRange"
              value-format="YYYY-MM-DD hh:mm:ss"
              type="daterange"
              range-separator="至"
              start-placeholder="起始时间"
              end-placeholder="结束时间"
            />
          </el-form-item>
          <el-form-item>
             <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
             <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
       </el-form>
 
       <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
             <el-button
                type="primary"
                plain
                icon="Plus"
                @click="handleAdd"
                v-hasPermi="['tienchin:clue:create']"
             >新增</el-button>
          </el-col>
          <el-col :span="1.5">
             <el-button
                type="success"
                plain
                icon="Edit"
                :disabled="single"
                @click="handleUpdate"
                v-hasPermi="['tienchin:clue:edit']"
             >修改</el-button>
          </el-col>
          <el-col :span="1.5">
             <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['tienchin:clue:remove']"
             >删除</el-button>
          </el-col>
          <!-- <el-col :span="1.5">
             <el-button
                type="warning"
                plain
                icon="Download"
                @click="handleExport"
                v-hasPermi="['tienchin:clue:export']"
             >导出</el-button>
          </el-col> -->
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
       </el-row>
      <!-- 展示表格 -->
       <el-table v-loading="loading" :data="clueList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="30px" align="center" />
          <el-table-column label="线索编号" align="center" prop="clueId" />
          <el-table-column label="手机号码" align="center" prop="phone" width="200px" />
          <el-table-column label="客户姓名" align="center" prop="name" />
          <el-table-column label="渠道来源" align="center" prop="channelName" />
          <el-table-column label="创建时间" :show-overflow-tooltip="true" width="200">
            <template #default="scope">
              <span>{{  parseTime(scope.row.createTime)  }}</span>
            </template>
          </el-table-column>
          <el-table-column label="下次跟进时间" :show-overflow-tooltip="true" width="200">
            <template #default="scope">
              <span>{{  parseTime(scope.row.nextTime)  }}</span>
            </template>
          </el-table-column>
          <el-table-column label="线索归属" align="center" prop="owner" />
          <el-table-column label="线索状态" align="center" prop="status">
            <template #default="scope">
                <dict-tag :options="clue_status" :value="scope.row.status"></dict-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" align="center" class-name="small-padding fixed-width">
             <template #default="scope">
                <el-button link type="primary" icon="View" @click="handleClueView(scope.row)" v-hasPermi="['tienchin:clue:view']">查看</el-button>
                <el-button link type="primary" icon="Pointer" @click="handleAssign(scope.row)" v-hasPermi="['tienchin:clue:assign']" v-if="scope.row.owner==userStore.name ||userStore.name=='admin'">分配</el-button>
                <el-button link type="primary" icon="TopRight" @click="handleClueFollow(scope.row)" v-hasPermi="['tienchin:clue:follow']" v-if="scope.row.owner==userStore.name ||userStore.name=='admin'">跟进</el-button>
             </template>
          </el-table-column>
          <el-table-column label="线索简介" align="center" prop="info" :show-overflow-tooltip="true" width="80px"/>
       </el-table>
       <pagination
          v-show="total > 0"
          :total="total" 
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
       />
       
       <!-- 分配 -->
       <el-dialog title="线索分配" v-model="assignClueDialog" width="500px" append-to-body>
          <el-form ref="clueAssignRef" :model="assignForm" :rules="assignFormRules" label-width="120px">
            <el-form-item label="归属部门" prop="departmentId">
              <el-tree-select
                v-model="assignForm.departmentId"
                :data="deptOptions"
                :props="{ value: 'id', label: 'label', children: 'children' }"
                value-key="id"
                placeholder="请选择归属部门"
                check-strictly
                @change="deptChange"
              />
            </el-form-item>
            <el-form-item label="分配员工" prop="nickName" >
              <el-select
                v-model="assignForm.nickName"
                aria-placeholder="请选择用户"
                clearable 
                @change="assignUserChange"
              >
                <el-option
                  v-for="ul in userList"
                  :key="ul.userId"
                  :label="ul.nickName"
                  :value="{value:ul.userId,userName:ul.userName,nickName:ul.nickName,deptId:ul.deptId}"
                >
                <!-- 此处:value中的value（代替掉userId:）用于控制选中的颜色，如果没有此字段则选中也是蓝色加粗字体 -->
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <template #footer>
             <div class="dialog-footer">
                <el-button type="primary" @click="handleAssignClue">确 定</el-button>
                <el-button @click="assignCluecancel">取 消</el-button>
             </div>
          </template>
       </el-dialog>
       <!-- 添加或修改线索对话框 -->
       <el-dialog :title="title" v-model="open" width="500px" append-to-body>
          <el-form ref="clueRef" :model="form" :rules="rules" label-width="120px">
             <el-form-item label="客户姓名" prop="name">
                <el-input v-model="form.name" placeholder="请输入客户昵称"/>
             </el-form-item>
             <el-form-item label="电话号" prop="phone">
                <el-input v-model="form.phone" placeholder="客户手机号" />
            </el-form-item>
            <el-form-item label="客户性别" prop="gender">
                <el-radio-group v-model="form.gender">
                   <el-radio
                      v-for="dict in sys_user_sex"
                      :key="dict.value"
                      :label="dict.value"
                   >{{ dict.label }}</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="客户年龄" prop="age">
                <el-input-number v-model="form.age" placeholder="客户年龄" :min="6" :max="70"/>
            </el-form-item>
            <el-form-item label="客户微信" prop="weixin" >
                <el-input v-model="form.weixin" placeholder="客户的微信号" />
            </el-form-item>
            <el-form-item label="客户QQ" prop="age">
                <el-input v-model="form.qq" placeholder="客户的QQ" />
            </el-form-item>
            <el-form-item label="意向等级" prop="level">
                <el-select v-model="form.level" class="m-2" placeholder="请选择客户来自的活动">
                  <el-option
                     v-for="item in clue_level"
                     :key="item.value"
                     :label="item.label"
                     :value="parseInt(item.value)" 
                  />
               </el-select>
            </el-form-item>
            <!-- <el-form-item label="意向课程" prop="subject">
                <el-input v-model="form.subject" placeholder="" />
            </el-form-item> -->
            <el-form-item label="下次跟进时间" prop="nextTime">
                <el-date-picker
                    v-model="form.nextTime"
                    type="datetime"
                    placeholder="选择下次跟进的时间"
                    :shortcuts="dateTimeShortcuts"
                />
            </el-form-item>
             <el-form-item label="来自渠道" prop="channelId">
                <el-select v-model="form.channelId" class="m-2" placeholder="请选择获取客户的渠道" @change="getActivityList">
                  <el-option
                     v-for="item in channelList"
                     :key="item.channelId"
                     :label="item.channelName"
                     :value="item.channelId"
                  />
               </el-select>
             </el-form-item>
             <el-form-item label="来自活动" prop="activityId">
                <el-select v-model="form.activityId" class="m-2" placeholder="请选择吸引客户的活动">
                  <el-option
                     v-for="item in activityList"
                     :key="item.activityId"
                     :label="item.name"
                     :value="item.activityId"
                  />
               </el-select>
             </el-form-item>
             <el-form-item label="备注" prop="remark">
                  <el-input v-model="form.remark" placeholder="备注信息"/>
             </el-form-item>
          </el-form>
          <template #footer>
             <div class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
             </div>
          </template>
       </el-dialog>
    </div>
 </template>
 
 <script setup name="Activity">
 import {addClue,listChannel,listActivity,listClue,listUsers,assignClue,getClueSummary,updateClue,delClue} from "@/api/tienchin/clue.js"
 import { changeUserStatus, listUser, resetUserPwd, delUser, getUser, updateUser, addUser, deptTreeSelect } from "@/api/system/user";
 import {ElMessage} from 'element-plus'
 import useUserStore from '@/store/modules/user'
 import { useRoute, useRouter } from "vue-router";
 const { proxy } = getCurrentInstance();
 const {clue_status,course_apply_to,course_type,sys_user_sex,clue_level} = proxy.useDict("clue_status","course_apply_to","course_type","sys_user_sex","clue_level");
 
const userStore=useUserStore();
 const clueList = ref([]);
 const open = ref(false);
 const assignClueDialog = ref(false);
 const loading = ref(true);
 const showSearch = ref(true);
 const ids = ref([]);
 const single = ref(true);
 const multiple = ref(true);
 const deptOptions = ref(undefined);
 const total = ref(0);
 const title = ref("");
 const channelList=ref([])
 const activityList=ref([])
 const userList=ref([])
 const dateRange=ref([])
 const dateTimeShortcuts = [//日期快速选择
  {
    text: '三天后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 3)
      return date
    },
  },
  {
    text: '一周后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 7)
      return date
    },
  },
  {
    text: '一个月后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 30)
      return date
    },
  },
  {
    text: '三个月后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 90)
      return date
    },
  },
  {
    text: '半年后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 180)
      return date
    },
  },
  {
    text: '一年后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 365)
      return date
    },
  }
]
 const data = reactive({
   form: {},
   assignForm:{},
   queryParams: {
     name: undefined,
     phone: undefined,
     owner: undefined,
     channelId: undefined,
     status: undefined,
     startDate: undefined,
     endDate: undefined,
     pageNum: 1,
     pageSize: 10
   },
   rules: {
     name: [{ required: true, message: "活动名称不能为空", trigger: "blur" }],
     phone: [{ required: true, message: "手机号不能为空", trigger: "blur" }],
   },
   assignFormRules:{
    departmentId:[{ required: true, message: "部门Id不能为空", trigger: "blur" }],
    nickName:[{ required: true, message: "用户名称不能为空", trigger: "blur" }]
   }
 });
 
 const { queryParams, form, rules,assignForm,assignFormRules} = toRefs(data);
 

//初始化的函数
getChannelList()
//获得有哪些渠道
 function getChannelList(){
   listChannel().then(response=>{
      channelList.value=response.data;
   })
 }
 //分配用户改变后修改多条信息
 function assignUserChange(data){
  assignForm.value.nickName=data.nickName;
  assignForm.value.userId=data.value;//因为:value绑定的是value而不是userId:
  assignForm.value.deptId=data.deptId;
  assignForm.value.userName=data.userName;

 }
 function handleClueView(data){
    //proxy.$router.push('/clue/detial/index/'+data.clueId+'/view')

    proxy.$router.push('/clue/detail/index/' + data.clueId+'/view')
 }
 function handleClueFollow(data){
    proxy.$router.push('/clue/detail/index/'+data.clueId+'/follow')
 }
 //处理分配线索
function handleAssignClue(){
  proxy.$refs["clueAssignRef"].validate(valid=>{
    if(valid){
      assignClue(assignForm.value).then(resp=>{
      getList();
      assignClueDialog.value=false;
      ElMessage("分配成功")
      //重置表单
      assignCluecancel()
      })
    }
  })
}

 //获得部门信息
 function initDeptOptions(){
    deptTreeSelect().then(response => {//需要注意的是，不一定有这个权限。需要在后台重新写一个接口（但实际不用，因为后台没设置权限）
      deptOptions.value = response.data;
    });
 }
 //获得有哪些活动
 function getActivityList(){
    form.value.activityId=undefined
    listActivity(form.value.channelId).then(response=>{
        activityList.value=response.data;
    })
 }
 function deptChange(){
  assignForm.value.nickName=undefined;
  initUsers();
 }
 //活动部门下的用户
 function initUsers(){
  listUsers(assignForm.value.departmentId).then(resp=>{
    userList.value=resp.data;
  })
 }
 /** 查询线索列表 */
 function getList() {
   loading.value = true;
   listClue(queryParams.value).then(response => {
    console.log(queryParams.value)
    //## 这里有点奇怪，为什么是row还没出问题.
    //原因：后端口使用了分页插件，不用AjaxResult返回而是TableDataInfo（getDataTable（list））
    console.log(response);
     clueList.value = response.rows;
     total.value = response.total;
     loading.value = false;
   });
 }
 /** 取消按钮 */
 function cancel() {
   open.value = false;
   reset();
 }
 function assignCluecancel() {
   assignClueDialog.value = false;
   AssignReset();
 }
 function AssignReset(){
  assignForm.value={
    deptmentId:undefined,
    nickName:undefined
  }
 }
 /** 表单重置 */
 function reset() {
   form.value = {
     clueId:undefined,
     name:undefined,
     channelId:undefined,
     activityId:undefined,
     gender:undefined,
     age:undefined,
     weixin:undefined,
     qq:undefined,
     phone:undefined,
     level:undefined,
     subject:undefined,
     status:undefined,
     failCount:undefined,
     nextTime:undefined,
     endTime:undefined,
     transfer:undefined,
     remark:undefined,
   };
   proxy.resetForm("clueRef");
 }
 /** 搜索按钮操作 */
 function handleQuery() {
  queryParams.value.startDate=dateRange.value[0];
  queryParams.value.endDate=dateRange.value[1];
   getList();
 }
 /** 重置按钮操作 */
 function resetQuery() {
   proxy.resetForm("queryRef");
   dateRange.value=[];
   handleQuery();
 }
 /** 多选框选中数据 */
 function handleSelectionChange(selection) {
   ids.value = selection.map(item => item.clueId);
   single.value = selection.length != 1;
   multiple.value = !selection.length;
 }
 /** 新增按钮操作 */
 function handleAdd() {
   reset();
   open.value = true;
   title.value = "添加线索";
 }

 /** 修改按钮操作 */
 function handleUpdate() {
   reset();
   const clueId = ids.value;
   //查询岗位详细信息，为什么不直接用呢：页面可能长时间没有刷新。
   getClueSummary(clueId).then(response => {
     form.value = response.data;
     title.value = "修改线索信息";
     open.value = true;
   });
 }
//  分配操作
function handleAssign(data){
  assignForm.value.assignId=data.clueId;
  assignForm.value.type=0;
  initDeptOptions();
  assignClueDialog.value=true
}

 /** 提交按钮 */
 function submitForm() {
   //修改或新增请求
   proxy.$refs["clueRef"].validate(valid => {
     if (valid) {
       if (form.value.clueId != undefined) {
         //更新操作
         updateClue(form.value).then(response => {
           proxy.$modal.msgSuccess("修改成功");
           open.value = false;
           getList();
         });
       } else {
         //添加操作
         addClue(form.value).then(response => {
           proxy.$modal.msgSuccess("新增线索成功");
           open.value = false;
           getList();
         });
       }
     }
   });
 }

 /** 删除按钮操作 */
 function handleDelete(row) {
   const clueIds = row.clueId || ids.value;
   proxy.$modal.confirm('是否确认删除线索编号为"' + clueIds + '"的数据项？').then(function() {
     return delClue(clueIds);
   }).then(() => {
     getList();
     proxy.$modal.msgSuccess("删除成功");
   }).catch(() => {});
 }
 /** 导出按钮操作 */
 function handleExport() {
   
   proxy.download("tienchin/course/export", {
     ...queryParams.value
   }, `course_${new Date().getTime()}.xlsx`);
 }
 getList();


 function getDeptTree() {
  deptTreeSelect().then(response => {
    deptOptions.value = response.data;
  });
};

 </script>
 