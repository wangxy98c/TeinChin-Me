<template>
    <div class="app-container">
       <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
          <el-form-item label="合同姓名" prop="name">
             <el-input
                v-model="queryParams.name"
                placeholder="合同姓名"
                clearable
                style="width: 100px"
                @keyup.enter="handleQuery"
             />
          </el-form-item>
          <el-form-item label="合同手机号码" prop="phone">
             <el-input
                v-model="queryParams.phone"
                placeholder="手机号码"
                clearable
                style="width: 150px"
                @keyup.enter="handleQuery"
             />
          </el-form-item>
          <el-form-item label="合同归属人" prop="owner">
             <el-input
                v-model="queryParams.owner"
                placeholder="合同归属人"
                clearable
                style="width: 100px"
                @keyup.enter="handleQuery"
             />
          </el-form-item>
          <!-- <el-form-item label="渠道来源" prop="channelId">
            <el-select v-model="queryParams.channelId" class="m-2" placeholder="请选择获取合同的渠道" clearable>
                <el-option
                    v-for="item in channelList"
                    :key="item.channelId"
                    :label="item.channelName"
                    :value="item.channelId"
                />
              </el-select>
          </el-form-item>
          <el-form-item label="合同状态" prop="status">
            <el-select v-model="queryParams.status" class="m-2" placeholder="线索状态" clearable>
                <el-option
                    v-for="item in business_status" 
                    :key="item.value" 
                    :label="item.label" 
                    :value="parseInt(item.value)"
                />
              </el-select>
          </el-form-item> -->
          <el-form-item label="下次跟进日期" prop="status">
            <el-date-picker
              v-model="dateRange"
              value-format="YYYY-MM-DD hh:mm:ss"
              type="daterange"
              range-separator="至"
              start-placeholder="起始时间"
              end-placeholder="结束时间"
              clearable
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
                v-hasPermi="['tienchin:contract:create']"
             >新增</el-button>
          </el-col>
          <el-col :span="1.5">
             <el-button
                type="success"
                plain
                icon="Edit"
                :disabled="single"
                @click="handleUpdate"
                v-hasPermi="['tienchin:contract:edit']"
             >修改</el-button>
          </el-col>
          <el-col :span="1.5">
             <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['tienchin:contract:remove']"
             >删除</el-button>
          </el-col>
          <!-- <el-col :span="1.5">
             <el-button
                type="warning"
                plain
                icon="Download"
                @click="handleExport"
                v-hasPermi="['tienchin:contract:export']"
             >导出</el-button>
          </el-col> -->
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
       </el-row>
       <el-tag>待处理任务列表</el-tag>
      <!-- 展示表格 -->
       <el-table v-loading="loading" :data="unapproveTask" @selection-change="handleSelectionChange" :row-class-name="myTaskListRowStyle">
          <el-table-column type="selection" width="30px" align="center" />
          <el-table-column label="合同编号" align="center" prop="contractId" width="50px"/>
          <el-table-column label="手机号码" align="center" prop="phone" width="200px" />
          <el-table-column label="客户姓名" align="center" prop="name" />
          <el-table-column label="渠道名称" align="center" prop="channelName" />
          <el-table-column label="活动名称" align="center" prop="activityName" />
          <el-table-column label="课程名称" align="center" prop="courseName" />
          <el-table-column label="合同价格" align="center" prop="contractPrice" />

          <el-table-column label="操作" width="180" align="center" class-name="small-padding fixed-width">
             <template #default="scope">
                <el-button link type="primary" icon="View" @click="handleBusinessView(scope.row)" v-hasPermi="['tienchin:contract:details']">查看</el-button>
                <el-button link type="primary" icon="Pointer" @click="handleView(scope.row)" v-hasPermi="['tienchin:contract:view']" >预览</el-button>
                <el-button link type="primary" icon="TopRight" @click="showApproveContractDialog(scope.row)" v-hasPermi="['tienchin:contract:approve']" v-if="!scope.row.reason">审批</el-button>
             </template>
          </el-table-column>
          <!-- <el-table-column label="线索简介" align="center" prop="info" :show-overflow-tooltip="true" width="80px"/> -->
       </el-table>
       <pagination
          v-show="total > 0"
          :total="total" 
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
       />
       <!--  合同审批对话框 -->
        <el-dialog
          v-model="approveDialog"
          title="合同审批"
          width="500"
        >
          <el-tag>通过或拒绝的理由</el-tag>
          <el-input placeholder="快速审批" v-model="approveInfo.reason"></el-input>
          <template #footer>
            <div class="dialog-footer">
              <el-button type="danger" @click="handleApproveContract(false)">拒绝</el-button>
              <el-button type="primary" @click="handleApproveContract(true)">通过</el-button>
            </div>
          </template>
        </el-dialog>

      <div style="margin-top: 30px;">
        <el-tag>自己已提交的任务列表</el-tag>
        <el-table v-loading="loading" :data="commitTask" @selection-change="handleSelectionChange" :row-class-name="myTaskListRowStyle">
          <el-table-column type="selection" width="30px" align="center" />
          <el-table-column label="合同编号" align="center" prop="contractId" width="50px"/>
          <el-table-column label="手机号码" align="center" prop="phone" width="200px" />
          <el-table-column label="客户姓名" align="center" prop="name" />
          <el-table-column label="渠道名称" align="center" prop="channelName" />
          <el-table-column label="活动名称" align="center" prop="activityName" />
          <el-table-column label="课程名称" align="center" prop="courseName" />
          <el-table-column label="合同价格" align="center" prop="contractPrice" />
          <el-table-column label="操作" width="180" align="center" class-name="small-padding fixed-width">
             <template #default="scope">
                <el-button link type="primary" icon="View" @click="handleBusinessView(scope.row)" v-hasPermi="['tienchin:contract:details']">查看</el-button>
                <el-button link type="primary" icon="Pointer" @click="handleView(scope.row)" v-hasPermi="['tienchin:contract:view']" >预览</el-button>
                <el-button link type="primary" icon="TopRight" @click="editAddContract(scope.row)" v-hasPermi="['tienchin:contract:approve']" v-if="scope.row.reason">补充合同信息</el-button>
             </template>
          </el-table-column>
          <!-- <el-table-column label="线索简介" align="center" prop="info" :show-overflow-tooltip="true" width="80px"/> -->
       </el-table>
       <pagination
          v-show="total > 0"
          :total="total" 
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
       />
      </div>

      <div style="margin-top: 30px;">
        <el-tag>审批完成列表</el-tag>
        <el-table v-loading="loading" :data="approvedTask" @selection-change="handleSelectionChange" :row-class-name="myTaskListRowStyle">
          <el-table-column type="selection" width="30px" align="center" />
          <el-table-column label="合同编号" align="center" prop="contractId" width="50px"/>
          <el-table-column label="手机号码" align="center" prop="phone" width="200px" />
          <el-table-column label="客户姓名" align="center" prop="name" />
          <el-table-column label="渠道名称" align="center" prop="channelName" />
          <el-table-column label="活动名称" align="center" prop="activityName" />
          <el-table-column label="课程名称" align="center" prop="courseName" />
          <el-table-column label="合同价格" align="center" prop="contractPrice" />
          <!-- <el-table-column label="理由" align="center" prop="reason" /> -->
          <el-table-column label="操作" width="180" align="center" class-name="small-padding fixed-width">
             <template #default="scope">
                <el-button link type="primary" icon="View" @click="handleBusinessView(scope.row)" v-hasPermi="['tienchin:contract:details']">查看</el-button>
                <el-button link type="primary" icon="Pointer" @click="handleView(scope.row)" v-hasPermi="['tienchin:contract:view']" >预览</el-button>
                <el-button link type="primary" icon="TopRight" @click="showApproveContractDialog(scope.row)" v-hasPermi="['tienchin:contract:approve']" v-if="scope.row.reason">补充合同信息</el-button>
             </template>
          </el-table-column>
          <!-- <el-table-column label="线索简介" align="center" prop="info" :show-overflow-tooltip="true" width="80px"/> -->
       </el-table>
       <pagination
          v-show="total > 0"
          :total="total" 
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
       />
      </div>
       
       <!-- 分配 -->
       <el-dialog title="分配合同" v-model="showContractDetailsDialog" width="500px" append-to-body>
          <el-row :gutter="20">
            <el-col :span="6">
              <div>合同编号</div>
              <div>{{ contractDetails.contractId }}</div>
            </el-col>
            <el-col :span="6">
              <div>客户手机号码</div>
              <div>{{ contractDetails.phone }}</div>
            </el-col>
            <el-col :span="6">
              <div>客户手机姓名</div>
              <div>{{ contractDetails.name }}</div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="6">
              <div>课程分类</div>
              <div>
                <template v-for="dict in course_type">
                  <template v-if="dict.value==contractDetails.type">
                    {{ dict.label }}
                  </template>
                </template>
              </div>
            </el-col>
            <el-col :span="6">
              <div>课程名</div>
              <div>{{ contractDetails.courseName }}</div>
            </el-col>
            <el-col :span="6">
              <div>渠道名称</div>
              <div>{{ contractDetails.channelName  }}</div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="6">
              <div>活动名称</div>
              <div>{{ contractDetails.activityName }}</div>
            </el-col>
            <el-col :span="6">
              <div>合同状态</div>
              <div>
                <template v-for="dict in contract_status">
                  <template v-if="dict.value==contractDetails.status">
                    {{ dict.label }}
                  </template>
                </template>
              </div>
            </el-col>
            <el-col :span="6">
              <div>合同金额</div>
              <div>{{ contractDetails.contractPrice }}</div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="6">
              <div>课程价格</div>
              <div>{{ contractDetails.coursePrice }}</div>
            </el-col>
            <el-col :span="6">
              <div>折扣类型</div>
              <div>
                <template v-for="dict in activity_type">
                  <template v-if="dict.value==contractDetails.discountType">
                    {{ dict.label }}
                  </template>
                </template>
              </div>
            </el-col>
            <el-col :span="6">
              <div>审批人</div>
              <div>{{ contractDetails.approveUserName }}</div>
            </el-col>
          </el-row>
          <template #footer>
             <div class="dialog-footer">
                <el-button type="primary" @click="handleAssignBusiness">确 定</el-button>
                <el-button @click="assignBusinesscancel">取 消</el-button>
             </div>
          </template>
       </el-dialog>
       <!-- 添加对话框 -->
       <el-dialog :title="title" v-model="open" width="500px" append-to-body>
          <el-form ref="businessRef" :model="form" :rules="rules" label-width="120px">
             <el-form-item label="客户姓名" prop="name">
                <el-input v-model="form.name" placeholder="请输入客户昵称"/>
             </el-form-item> 
             <el-form-item label="客户电话号" prop="phone">
                <el-input v-model="form.phone" placeholder="客户手机号" />
            </el-form-item>
            
            <el-form-item label="来自渠道" prop="channelId" v-if="!isedit">
                <el-select v-model="queryParams.channelId" class="m-2" placeholder="请选择合同的渠道" @change="getActivityList">
                  <el-option
                     v-for="item in channelList"
                     :key="item.channelId"
                     :label="item.channelName"
                     :value="{value:parseInt(item.channelId),channelName:item.channelName}"
                  />
                  <!--这种:value形式必须要有value:item.xxx属性作为原本的value。 -->
               </el-select>
             </el-form-item>
             <el-form-item label="来自活动" prop="activityId" v-if="!isedit">
                <el-select v-model="queryParams.activityId" class="m-2" placeholder="请选择吸引合同的活动" @change="activityChange">
                  <el-option
                     v-for="item in activityList"
                     :key="item.activityId"
                     :label="item.name"
                     :value="{value:parseInt(item.activityId),activityName:item.name,discountType:item.type}"
                  />
               </el-select>
             </el-form-item>
             <el-form-item label="合同内课程类型" prop="type" v-if="!isedit">
                <el-select v-model="form.type" class="m-2" placeholder="请选择课程类型" @change="getCourseList">
                    <el-option
                        v-for="item in course_type"
                        :key="item.value"
                        :label="item.label"
                        :value="parseInt(item.value)"
                    />
                </el-select>
             </el-form-item>
            <el-form-item label="合同课程" prop="weixin" v-if="!isedit">
              <el-select v-model="queryParams.courseId" class="m-2" placeholder="请选择课程" @change="courseChange">
                <el-option
                    v-for="item in courseList"
                    :key="item.courseId"
                    :label="item.name"
                    :value="{value:parseInt(item.courseId),courseName:item.name,price:item.price}"
                />
                </el-select>
              </el-form-item>
              <el-form-item label="课程价格" prop="coursePrice" v-if="!isedit">
                <el-input-number v-model="form.coursePrice" :min="0" :precision="2" :step="100"></el-input-number>
              </el-form-item>
              <el-form-item label="合同价格" prop="contractPrice">
                <el-input-number v-model="form.contractPrice" :min="0" :precision="2" :step="100"></el-input-number>
              </el-form-item>
            <el-form-item label="上传合同原件">
              <!-- action中的地址是由element发送的，没经过axios。故直接写地址不行。需要加/dev-api才会被vite代理 -->
              <!-- 加了之后仍有问题，问题来自于权限认证。因为没走框架的request请求（有拦截器配置权限认证），用header自己加Bearer -->
              <el-upload
                ref="upload"
                :limit="1" 
                class="upload-demo"
                action="/dev-api/tienchin/contract/upload"
                :headers="headersObj"
                :on-success="handleSuccess"
                :before-upload="handleBeforeUpload"
                :on-remove="handleRemove"
                :on-exceed="handleExceed"
                :auto-upload="false"
              >
                <template #trigger>
                  <el-button type="primary">选择合同原件</el-button>
                </template>
                <el-button class="ml-3" type="success" @click="submitUpload">
                  上传
                </el-button>
                <template #tip>
                  <div class="el-upload__tip text-red">
                    请注意，文件大小不能超过5MB。
                  </div>
                </template>
              </el-upload>
            </el-form-item>
            
            <el-form-item label="审批人部门" prop="departmentId">
              <el-tree-select
                v-model="form.approveDeptId"
                :data="deptOptions"
                :props="{ value: 'id', label: 'label', children: 'children' }"
                value-key="id"
                placeholder="请选择审批人的归属部门"
                check-strictly
                @change="deptChange"
              />
            </el-form-item>
            <el-form-item label="审批人" prop="approveUsername">
              <el-select
                v-model="queryParams.username"
                aria-placeholder="请选择审批人"
                clearable 
                @change="approveUserChange"
              >
                <el-option
                  v-for="ul in userList"
                  :key="ul.userId"
                  :label="ul.nickName"
                  :value="{value:ul.userId,userName:ul.userName,deptId:ul.deptId}"
                >
                <!-- 此处:value中的value（代替掉userId:）用于控制选中的颜色，如果没有此字段则选中也是蓝色加粗字体 -->
                </el-option>
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
 import {getCourseByType, addContract,getUnapproveTask,getContractById,getCommitTask,handleApprove,getApprovedTask,editContract} from "@/api/tienchin/contract.js"
 import {listBusiness,listChannel,listActivity,addBusiness,assignBusiness,businessFollow,getBusinessById,updateBusiness,delBusiness} from "@/api/tienchin/business.js"
 import {listClue,listUsers,getClueSummary,updateClue,delClue} from "@/api/tienchin/clue.js"
 import { changeUserStatus, listUser, resetUserPwd, delUser, getUser, updateUser, addUser, deptTreeSelect } from "@/api/system/user";
 import {ElMessage,ElMessageBox} from 'element-plus'
 import useUserStore from '@/store/modules/user'
 import { useRoute, useRouter } from "vue-router";
import { getClueById } from "../../../api/tienchin/clue";


//上传
const upload = ref()
function submitUpload(){
  upload.value&&upload.value.submit();
}
function handleBeforeUpload(rawFile){
  let isLimit=(rawFile.size/1024/1024) < 5;
  if(!isLimit){
    proxy.$modal.msgError("文件过大，文件大小要求不超过5MB")
  }
  return isLimit;
}
function handleRemove(){
  
}
function handleExceed(files, uploadFiles){
  upload.clearFiles()
  upload.handleStart(files[0])
}
function handlePreview(){

}
function handleSuccess(response, uploadFile, uploadFiles){
  form.value.filePath=response.data.url;
  ElMessage(response.msg)
}
import { getToken } from '@/utils/auth'
const headersObj=ref({
  "Authorization": 'Bearer ' + getToken()
})
//

const { proxy } = getCurrentInstance();
const {business_status,course_apply_to,course_type,sys_user_sex,clue_level,contract_status,activity_type} = proxy.useDict("business_status","course_apply_to","course_type","sys_user_sex","clue_level","contract_status","activity_type");
const courseList=ref([])
const approveDialog=ref(false)
const userStore=useUserStore();
const unapproveTask = ref([]);
const commitTask=ref([]);
const approvedTask=ref([]);
const contractDetails=ref([]);
const open = ref(false);
const showContractDetailsDialog = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const deptOptions = ref();
const total = ref(0);
const title = ref("");
const isedit=ref(false)
const approveInfo=ref({})
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
     contractPrice:[{ required: true, message: "必须有最终的合同价格", trigger: "blur" }]
   },
   assignFormRules:{
    departmentId:[{ required: true, message: "部门Id不能为空", trigger: "blur" }],
    nickName:[{ required: true, message: "用户名称不能为空", trigger: "blur" }],
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
 getTaskList()
 function getTaskList(){
   getUnapproveTask().then(response=>{
      unapproveTask.value=response.rows;
   })
   getCommitTask().then(response=>{
    commitTask.value=response.rows;
   })
   getApprovedTask().then(response=>{
    approvedTask.value=response.rows;
   })
   loading.value=false;
 }
 //分配用户改变后修改多条信息
 function approveUserChange(data){
  form.value.approveUserName=data.userName;
  form.value.approveUserId=data.value;
  form.value.approveDeptId=data.deptId;
 }
 function courseChange(data){
   form.value.courseId=data.value;
   form.value.courseName=data.courseName;
   form.value.coursePrice=data.price;
 }
 function activityChange(data){
  form.value.activityId=data.value;
  form.value.activityName=data.activityName;
  form.value.discountType=data.discountType;
 } 
 function deptChange(data){
  initUsers();
 }
 function handleApproveContract(approve){
  approveInfo.value.approve=approve;//approve表示通过/拒绝
  if(!approve){
    //不通过
    if(!approveInfo.value.reason){
      proxy.$message.error('审批拒绝必须给出修改建议')
      return;
    }
  }
  handleApprove(approveInfo.value).then(resp=>{
    proxy.$message.success("审批完成");
    approveDialog.value=false;
    getTaskList()
  })
 }
 function getCourseList(){
    getCourseByType(form.value.type).then(resp=>{
        courseList.value=resp.data;
    })
 }
 function handleBusinessView(data){
    //proxy.$router.push('/clue/detial/index/'+data.clueId+'/view')

    proxy.$router.push('/business/detail/index/' + data.businessId+'/view')
 }
 function showApproveContractDialog(data){
  approveDialog.value=true;
  approveInfo.value=JSON.parse(JSON.stringify(data));//如果直接=data，相当于两个指针指向同一地址（浅拷贝)，会出问题

 }
 //处理分配线索
function handleAssignBusiness(){
  proxy.$refs["businessAssignRef"].validate(valid=>{
    if(valid){
      assignBusiness(assignForm.value).then(resp=>{
      getList();
      showContractDetailsDialog.value=false;
      ElMessage("分配成功")
      //重置表单
      assignBusinesscancel()
      })
    }
  })
}
//补充修正合同信息
function editAddContract(data){
  isedit.value=true
  initDeptOptions();
  title.value="补充修正合同信息"
  getContractById(data.contractId).then(response=>{
    console.log("===",response.data)
    form.value=response.data;
    console.log(form.value)
    open.value=true;
  })

  
}


 //获得部门信息
 function initDeptOptions(){
    deptTreeSelect().then(response => {//需要注意的是，不一定有这个权限。需要在后台重新写一个接口（但实际不用，因为后台没设置权限）
      deptOptions.value = response.data;
    });
 }
 //获得有哪些活动,以及处理
 function getActivityList(data){
    form.value.channelId=data.value
    form.value.channelName=data.channelName;
    form.value.activityId=undefined;
    listActivity(data.value).then(response=>{
        activityList.value=undefined;
        activityList.value=response.data;
    })
 }
 

 //活动部门下的用户
 function initUsers(){
  listUsers(form.value.approveDeptId).then(resp=>{
    userList.value=resp.data;
  })
 }
 /** 查询列表 */
 function getList() {
  getTaskList();
 }
 /** 取消按钮 */
 function cancel() {
   open.value = false;
   reset();
 }
 function assignBusinesscancel() {
   showContractDetailsDialog.value = false;
   AssignReset();
 }
 function AssignReset(){
  assignForm.value={
    deptmentId:undefined,
  }
 }
 /** 表单重置 */
 function reset() {
   form.value = {
    //  clueId:undefined,
    //  name:undefined,
    //  channelId:undefined,
    //  activityId:undefined,
    //  gender:undefined,
    //  age:undefined,
    //  weixin:undefined,
    //  qq:undefined,
    //  phone:undefined,
    //  level:undefined,
    //  subject:undefined,
    //  status:undefined,
    //  failCount:undefined,
    //  nextTime:undefined,
    //  endTime:undefined,
    //  transfer:undefined,
    //  remark:undefined,
   };
   proxy.resetForm("businessRef");
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
   ids.value = selection.map(item => item.businessId);
   single.value = selection.length != 1;
   multiple.value = !selection.length;
 }
 /** 新增按钮操作 */
 function handleAdd() {
  isedit.value=false;
  initDeptOptions();
  reset();
   open.value = true;
   title.value = "添加合同";
 }

 /** 修改按钮操作 */
 function handleUpdate() {
   reset();
   const businessId = ids.value;
   //查询岗位详细信息，为什么不直接用呢：页面可能长时间没有刷新。
   getBusinessById(businessId).then(response => {
     form.value = response.data;
     title.value = "修改合同信息";
     open.value = true;
   });
 }
//  分配操作
function handleView(data){
  getContractById(data.contractId).then(resp=>{
    contractDetails.value=resp.data;
  })
  showContractDetailsDialog.value=true;

  assignForm.value.assignId=data.businessId;
  assignForm.value.type=1;
  initDeptOptions();
  showContractDetailsDialog.value=true
}

 /** 提交按钮 */
 function submitForm() {
   //修改或新增请求
   proxy.$refs["businessRef"].validate(valid => {
     if (valid) {
       if (form.value.contractId != undefined) {
         //更新操作
         editContract(form.value).then(response => {
           proxy.$modal.msgSuccess("修改成功");
           open.value = false;
           getList();
         });
       } else {
         //添加操作
         addContract(form.value).then(response => {
           proxy.$modal.msgSuccess("新增合同成功");
           open.value = false;
           getList();
         });
       }
     }
   });
 }

 /** 删除按钮操作 */
 function handleDelete(row) {
   const businessIds = row.businessId || ids.value;
   proxy.$modal.confirm('是否确认删除合同编号为"' + businessIds + '"的数据项？').then(function() {
    return delBusiness(businessIds);
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
function myTaskListRowStyle(row,rowIndex){
  if(row.row.reason){//注意此处是row.row来访问某行的数据。
    return 'red-bg';
  }
}
</script>
<style>
.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}
.el-table .red-bg{
  background-color: rgb(244, 95, 95);
  color: white;
}
</style>