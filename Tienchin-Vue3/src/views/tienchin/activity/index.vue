<template>
    <div class="app-container">
       <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
          <el-form-item label="活动名称" prop="name">
             <el-input
                v-model="queryParams.name"
                placeholder="请输入活动名称"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
             />
          </el-form-item>
          <el-form-item label="所属渠道" prop="channelId">
            <el-select v-model="queryParams.channelId" class="m-2" style="width: 200px;" placeholder="请选择所属渠道">
                  <el-option
                     v-for="item in channelList"
                     :key="item.channelId"
                     :label="item.channelName"
                     :value="item.channelId"
                  />
               </el-select>
          </el-form-item>
          <el-form-item label="活动状态" prop="status">
             <el-select v-model="queryParams.status" placeholder="请选择活动处于的状态" clearable style="width: 200px">
                <el-option
                   v-for="dict in activity_status"
                   :key="dict.value"
                   :label="dict.label"
                   :value="dict.value"
                />
             </el-select>
          </el-form-item>
          <el-form-item label="活动类型" prop="type">
             <el-select v-model="queryParams.type" placeholder="请选择活动的类型" clearable style="width: 200px">
                <el-option
                   v-for="dict in activity_type"
                   :key="dict.value"
                   :label="dict.label"
                   :value="dict.value"
                />
             </el-select>
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
                v-hasPermi="['tienchin:activity:create']"
             >新增</el-button>
          </el-col>
          <el-col :span="1.5">
             <el-button
                type="success"
                plain
                icon="Edit"
                :disabled="single"
                @click="handleUpdate"
                v-hasPermi="['tienchin:activity:edit']"
             >修改</el-button>
          </el-col>
          <el-col :span="1.5">
             <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['tienchin:activity:remove']"
             >删除</el-button>
          </el-col>
          <el-col :span="1.5">
             <el-button
                type="warning"
                plain
                icon="Download"
                @click="handleExport"
                v-hasPermi="['tienchin:activity:export']"
             >导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
       </el-row>
      <!-- 展示表格 -->
       <el-table v-loading="loading" :data="activityList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="35" align="center" />
          <el-table-column label="活动编号" align="center" prop="activityId" />
          <el-table-column label="活动名称" align="center" prop="name" />
          <el-table-column label="所属渠道" align="center" prop="channelName" />
          <el-table-column label="活动类型" align="center">
             <template #default="scope">
                <dict-tag :options="activity_type" :value="scope.row.type" />
             </template>
          </el-table-column>
          <el-table-column label="活动明细" align="center">
            <template #default="scope">
                <div v-if="scope.row.type==1">
                    折扣券/{{ scope.row.discount }}折
                </div>
                <div v-else>
                    代金券/{{ scope.row.voucher }}元
                </div>
            </template>
          </el-table-column>
          <el-table-column label="活动状态" align="center">
             <template #default="scope">
                <dict-tag :options="activity_status" :value="scope.row.status" />
             </template>
          </el-table-column>
          <el-table-column label="起始时间" align="center" prop="beginTime" width="180">
             <template #default="scope">
                <span>{{ parseTime(scope.row.beginTime) }}</span>
             </template>
          </el-table-column>
          <el-table-column label="结束时间" align="center" prop="endTime" width="180">
             <template #default="scope">
                <span>{{ parseTime(scope.row.endTime) }}</span>
             </template>
          </el-table-column>
          <el-table-column label="活动简介" align="center" prop="info" :show-overflow-tooltip="true" width="100px"/>
          <el-table-column label="操作" width="180" align="center" class-name="small-padding fixed-width">
             <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['tienchin:activity:edit']">修改</el-button>
                <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['tienchin:activity:remove']">删除</el-button>
             </template>
          </el-table-column>
       </el-table>
       <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
       />
 
       <!-- 添加或修改活动对话框 -->
       <el-dialog :title="title" v-model="open" width="500px" append-to-body>
          <el-form ref="activityRef" :model="form" :rules="rules" label-width="100px">
             <el-form-item label="活动名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入活动名称" />
             </el-form-item>
             <el-form-item label="活动渠道" prop="channelId">
               <el-select v-model="form.channelId" class="m-2" placeholder="请选择活动的渠道">
                  <el-option
                     v-for="item in channelList"
                     :key="item.channelId"
                     :label="item.channelName"
                     :value="item.channelId"
                  />
               </el-select>
             </el-form-item>
             <el-form-item label="活动简介" prop="info">
                  <el-input v-model="form.info" placeholder="关于此活动的简介信息"/>
             </el-form-item>
             <el-form-item label="活动类型" prop="type">
                <el-radio-group v-model="form.type">
                   <el-radio
                      v-for="dict in activity_type"
                      :key="dict.value"
                      :label="dict.value"
                   >{{ dict.label }}</el-radio>
                </el-radio-group>
             </el-form-item>
             <!-- 根据上面选择的类型，显示对应的框。词典方式赞时没有测试。先注释掉-->
             <!-- <template v-for="dict in activity_type">
               <el-form-item :label="dict.label" v-show="form.type==dict.value">
                  <el-input-number :v-model="form.label"/>
               </el-form-item>
             </template> -->
             <el-form-item label="折扣" prop="discount" v-show="form.type==1">
                  <el-input-number v-model="form.discount" :min="0" :max="10" :precision="1" :step="0.1"/>
             </el-form-item>
             <el-form-item label="代金" prop="voucher" v-show="form.type==2">
                  <el-input-number v-model="form.voucher" :min="0" :precision="2" :step="5"/>
             </el-form-item>
             <el-form-item label="备注" prop="remark">
                <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
             </el-form-item>
             
             <el-form-item label="活动时间" style="width: 100%" prop="activityTime">
               <el-date-picker
                  v-model="form.activityTime"
                  value-format="YYYY-MM-DD hh:mm:ss"
                  type="datetimerange"
                  range-separator="-"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
               ></el-date-picker>
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
 import {listActivity,addActivity,listChannel,updateActivity,getActivity,delActivity} from "@/api/tienchin/activity.js"
 const { proxy } = getCurrentInstance();
 const {activity_type,activity_status } = proxy.useDict("activity_type","activity_status");
 
 const activityList = ref([]);
 const open = ref(false);
 const loading = ref(true);
 const showSearch = ref(true);
 const ids = ref([]);
 const single = ref(true);
 const multiple = ref(true);
 const total = ref(0);
 const title = ref("");
const channelList=ref([]);
 
 const data = reactive({
   form: {},
   queryParams: {
     //activityId: undefined,
     channelId: undefined,
     name: undefined,
     //beginTime:undefined,
     //endTime:undefined,
     type: undefined,
     status: undefined,
     pageNum: 1,
     pageSize: 10
     //remark: undefined
   },
   rules: {
     name: [{ required: true, message: "活动名称不能为空", trigger: "blur" }],
     channelId: [{ required: true, message: "请选择活动渠道", trigger: "blur" }],
     activityTime: [{ required: true, message: "请选择活动的开始结束时间", trigger: "blur" }],
     type: [{ required: true, message: "请选择活动的开始结束时间", trigger: "blur" }]
   }
 });
 
 const { queryParams, form, rules } = toRefs(data);
 
 /** 查询活动列表 */
 function getList() {
   loading.value = true;
   listActivity(queryParams.value).then(response => {
     activityList.value = response.rows;
     total.value = response.total;
     loading.value = false;
   });
 }
 /** 取消按钮 */
 function cancel() {
   open.value = false;
   reset();
 }
 getChannelList()
 /** 表单重置 */
 function reset() {
   form.value = {
     activityId: undefined,
     channelId: undefined,
     name: undefined,
     activityTime:undefined,
     beginTime:undefined,
     endTime:undefined,
     type: undefined,
     status: undefined,
     remark: undefined,
     info:undefined
   };
   proxy.resetForm("activityRef");
 }
 /** 搜索按钮操作 */
 function handleQuery() {
   queryParams.value.pageNum = 1;
   getList();
 }
 /** 重置按钮操作 */
 function resetQuery() {
   proxy.resetForm("queryRef");
   handleQuery();
 }
 /** 多选框选中数据 */
 function handleSelectionChange(selection) {
   ids.value = selection.map(item => item.activityId);
   single.value = selection.length != 1;
   multiple.value = !selection.length;
 }
 function getChannelList(){
   listChannel().then(response=>{
      channelList.value=response.data;
   })
 }
 /** 新增按钮操作 */
 function handleAdd() {
   getChannelList();
   reset();
   open.value = true;
   title.value = "添加活动";
 }

 /** 修改按钮操作 */
 function handleUpdate(row) {
   getChannelList()
   reset();
   const activityId = row.activityId || ids.value;
   console.log("activityId===>:",activityId);
   //查询岗位详细信息，为什么不直接用呢：页面可能长时间没有刷新。
   getActivity(activityId).then(response => {
     form.value = response.data;
     console.log(form.value)
     title.value = "修改活动信息";
     form.value.type=String(form.value.type)
     form.value.activityTime=new Array()
     form.value.activityTime[0]=form.value.beginTime
      form.value.activityTime[1]=form.value.endTime
     console.log("form-activityTime",form.value.activityTime)
     open.value = true;
   });
 }

 /** 提交按钮 */
 function submitForm() {
   //修改或新增请求
   proxy.$refs["activityRef"].validate(valid => {
     if (valid) {
      //处理时间选择器的数组格式===>begintime、endtime
      form.value.beginTime=form.value.activityTime[0]
      form.value.endTime=form.value.activityTime[1]
      delete form.value.activityTime
       if (form.value.activityId != undefined) {
         console.log("====》更新操作")
         console.log(form.value)
         //更新操作
         updateActivity(form.value).then(response => {
           proxy.$modal.msgSuccess("修改成功");
           open.value = false;
           getList();
         });
       } else {
         console.log("form==>\n",form.value);
         //添加操作
         addActivity(form.value).then(response => {
           proxy.$modal.msgSuccess("新增活动成功");
           open.value = false;
           getList();
         });
       }
     }
   });
 }

 /** 删除按钮操作 */
 function handleDelete(row) {
   const activityIds = row.activityId || ids.value;
   proxy.$modal.confirm('是否确认删除活动编号为"' + activityIds + '"的数据项？').then(function() {
     return delActivity(activityIds);
   }).then(() => {
     getList();
     proxy.$modal.msgSuccess("删除成功");
   }).catch(() => {});
 }
 /** 导出按钮操作 */
 function handleExport() {
   
   proxy.download("tienchin/activity/export", {
     ...queryParams.value
   }, `activity_${new Date().getTime()}.xlsx`);
 }
 
 getList();
 </script>
 