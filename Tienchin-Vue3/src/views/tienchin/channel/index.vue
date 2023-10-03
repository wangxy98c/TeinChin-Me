<template>
    <div class="app-container">
       <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true" label-width="68px">
          <el-form-item label="渠道名称" prop="channelName">
             <el-input
                v-model="queryParams.channelName"
                placeholder="请输入渠道名称"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
             />
          </el-form-item> 
          <el-form-item label="渠道类型" prop="type">
            <el-select
                v-model="queryParams.type"
                placeholder="渠道状态"
                clearable
                style="width: 240px"
             >
                <el-option
                   v-for="dict in channel_type"
                   :key="dict.value"
                   :label="dict.label"
                   :value="dict.value"
                />
             </el-select>
          </el-form-item>
          <el-form-item label="渠道状态" prop="status">
             <el-select
                v-model="queryParams.status"
                placeholder="渠道状态"
                clearable
                style="width: 240px"
             >
                <el-option
                   v-for="dict in channel_status"
                   :key="dict.value"
                   :label="dict.label"
                   :value="dict.value"
                />
             </el-select>
          </el-form-item>
          <el-form-item label="创建时间" style="width: 308px">
             <el-date-picker
                v-model="dateRange"
                value-format="YYYY-MM-DD"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
             ></el-date-picker>
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
                v-hasPermi="['tienchin:channel:add']"
             >新增</el-button>
          </el-col>
          <el-col :span="1.5">
             <el-button
                type="success"
                plain
                icon="Edit"
                :disabled="single"
                @click="handleUpdate"
                v-hasPermi="['tienchin:channel:edit']"
             >修改</el-button>
          </el-col>
          <el-col :span="1.5">
             <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['tienchin:channel:remove']"
             >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
                     type="info"
                     plain
                     icon="Upload"
                     @click="handleImport"
                     v-hasPermi="['tienchin:channel:import']"
            >导入</el-button>
             <el-button
                type="warning"
                plain
                icon="Download"
                @click="handleExport"
                v-hasPermi="['tienchin:channel:export']"
             >导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
       </el-row>

       <!-- 渠道导入对话框 -->
      <el-dialog :title="upload.title" v-model="upload.open" width="400px" append-to-body>
         <el-upload
            ref="uploadRef"
            :limit="1"
            accept=".xlsx, .xls"
            :headers="upload.headers"
            :action="upload.url + '?updateSupport=' + upload.updateSupport"
            :disabled="upload.isUploading"
            :on-progress="handleFileUploadProgress"
            :on-success="handleFileSuccess"
            :auto-upload="false"
            drag
         >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <template #tip>
               <div class="el-upload__tip text-center">
                  <div class="el-upload__tip">
                     <el-checkbox v-model="upload.updateSupport" />我要更新已经存在的渠道数据
                  </div>
                  <span>仅允许导入xls、xlsx格式文件。</span>
                  <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
               </div>
            </template>
         </el-upload>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitFileForm">确 定</el-button>
               <el-button @click="upload.open = false">取 消</el-button>
            </div>
         </template>
      </el-dialog>
 
       <!-- 表格数据 -->
       <el-table v-loading="loading" :data="channelList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55"/>
          <el-table-column label="渠道编号" prop="channelId" width="120" />
          <el-table-column label="渠道名称" prop="channelName" :show-overflow-tooltip="true" width="150" />
          <el-table-column label="渠道类型" :show-overflow-tooltip="true" width="150">
            <template #default="scope">
                <template v-for="(type,indexi) in channel_type">
                    <el-tag :key="indexi" v-if="type.value==scope.row.type" :type="type.elTagType">{{ type.label }}</el-tag>
                </template>
            </template>
          </el-table-column>
          <el-table-column label="渠道状态" prop="status" width="100" >
            <template #default="scope">
                <template v-for="(type,indexi) in channel_status">
                    <el-tag :key="indexi" v-if="type.value==scope.row.status" :type="type.elTagType">{{ type.label }}</el-tag>
                </template>
            </template>
          </el-table-column>

          <el-table-column label="创建人" prop="createBy" width="100" />
          <el-table-column label="创建时间" :show-overflow-tooltip="true" width="200">
            <template #default="scope">
              <span>{{  parseTime(scope.row.createTime)  }}</span>
            </template>
          </el-table-column>
          <el-table-column label="备注信息" prop="remark" width="100" />

          
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
             <template #default="scope">
               <el-tooltip content="修改" placement="top" v-if="scope.row.roleId !== 1">
                 <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['tienchin:channel:edit']"></el-button>
               </el-tooltip>
               <el-tooltip content="删除" placement="top" v-if="scope.row.roleId !== 1">
                 <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['tienchin:channel:remove']"></el-button>
               </el-tooltip>
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
 
       <!-- 添加或修改渠道配置对话框 -->
       <el-dialog :title="title" v-model="open" width="500px" append-to-body>
          <el-form ref="channelRef" :model="form" :rules="rules" label-width="100px">
             <el-form-item label="渠道名称" prop="channelName">
                <el-input v-model="form.channelName" placeholder="请输入渠道名称" />
             </el-form-item>
             <el-form-item label="渠道状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio v-for="dict in channel_status" :key="dict.value" :label="parseInt(dict.value)" :model-value="dict.value">
                    {{ dict.label }}
                  </el-radio>
                </el-radio-group>
             </el-form-item>
             <el-form-item label="渠道类型">
                <el-radio-group v-model="form.type">
                  <el-radio v-for="dict in channel_type" :key="dict.value" :label="parseInt(dict.value)" :model-value="dict.value">
                    {{ dict.label }} 
                  </el-radio>
                </el-radio-group>
             </el-form-item>
             <el-form-item label="备注">
                <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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
 
 <script setup name="Role">
 import {listChannel,addChannel,getChannel,updateChannel,delChannel} from "@/api/tienchin/channel"
 import { getToken } from "@/utils/auth";

 const router = useRouter();
 const { proxy } = getCurrentInstance();// 获得当前vue实例
 //channel_type  _status实际是数组。   useDict是全局挂载的方法（src/utils/dict.js中定义）。
 const { channel_type,channel_status} = proxy.useDict("channel_type","channel_status");//必须与字典中的字段对应
 
 const channelList = ref([]);
 const open = ref(false);
 const loading = ref(true);
 const showSearch = ref(true);
 const ids = ref([]);
 const single = ref(true);
 const multiple = ref(true);
 const total = ref(0);
 const title = ref("");
 const dateRange = ref([]);
 const menuExpand = ref(false);
 const menuNodeAll = ref(false);
 const deptExpand = ref(true);
 const deptNodeAll = ref(false);
 const menuRef = ref(null);
 
 /*** 渠道导入参数 */
const upload = reactive({
  // 是否显示弹出层（渠道导入）
  open: false,
  // 弹出层标题（渠道导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },//认证的令牌
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/tienchin/channel/importData"
});
 /** 数据范围选项*/
 const dataScopeOptions = ref([
   { value: "1", label: "全部数据权限" },
   { value: "2", label: "自定数据权限" },
   { value: "3", label: "本部门数据权限" },
   { value: "4", label: "本部门及以下数据权限" },
   { value: "5", label: "仅本人数据权限" }
 ]);
 
 const data = reactive({
   form: {},
   queryParams: {
     pageNum: 1,
     pageSize: 10,
     channelName: undefined,
     type: undefined,
     status: undefined

   },
   rules: {
     channelName: [{ required: true, message: "渠道名称不能为空", trigger: "blur" }],
     status: [{ required: true, message: "渠道状态不能为空", trigger: "blur" }],
     type: [{ required:  true, message: "渠道类型不能为空", trigger: "blur" }]
   },
 });
 
 const { queryParams, form, rules } = toRefs(data);
 
 /** 查询渠道列表 */
 function getList() {
   loading.value = true;
   listChannel(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
     channelList.value = response.rows;
     total.value = response.total;
     loading.value = false;
   });
 }
 /** 下载模板操作 */
function importTemplate() {
  proxy.download("tienchin/channel/importTemplate", {
  }, `user_template_${new Date().getTime()}.xlsx`);
};
 function handleImport() {
  upload.title = "渠道导入";
  upload.open = true;
};
 /** 搜索按钮操作 */
 function handleQuery() {
   queryParams.value.pageNum = 1;
   getList();
 }
 /** 重置按钮操作 */
 function resetQuery() {
   dateRange.value = [];
   proxy.resetForm("queryRef");
   handleQuery();
 }
 /** 删除按钮操作 */
 function handleDelete(row) {
   const channelIds = row.channelId || ids.value;//无参数就是从上面按钮进的，有参数就是从表格某行进的。
   proxy.$modal.confirm('是否确认删除渠道编号为"' + channelIds + '"的数据项?').then(function () {
     return delChannel(channelIds);
   }).then(() => {
     getList();
     proxy.$modal.msgSuccess("删除成功");
   }).catch(() => {});
 }
 /** 导出按钮操作 */
 function handleExport() {
   proxy.download("tienchin/channel/export", {//##note :封装好的download方法。第二个参数是导出的文件名。
     ...queryParams.value,
   }, `channel_${new Date().getTime()}.xlsx`);
 }
 /** 多选框选中数据 , 只有选中有数据 才能修改*/
 function handleSelectionChange(selection) { 
   ids.value = selection.map(item => item.channelId);//ids用于存储所选中的那行数据的键值
   single.value = selection.length != 1;
   multiple.value = !selection.length;
 }

 /** 重置新增的表单以及其他数据  */
 function reset() {
   if (menuRef.value != undefined) {
     menuRef.value.setCheckedKeys([]);
   }
   menuExpand.value = false;
   menuNodeAll.value = false;
   deptExpand.value = true;
   deptNodeAll.value = false;
   form.value = {
     channelName:undefined,
     status:undefined,
     type:undefined,
     remark:undefined
   };
   proxy.resetForm("channelRef");
 }
 /** 添加渠道 */
 function handleAdd() {
   reset();
   open.value = true;
   title.value = "添加渠道";
 }
 /** 修改，无论是最上面的修改还是表格中的修改调用的都是此方法 */
 function handleUpdate(row) {
   reset();
   const channelId = row.channelId || ids.value;//拿到多选的ids或者某行的row.id
   //为了防止页面长时间没有刷新（在此期间其他人刷新了 )
   getChannel(channelId).then(response => {
     form.value = response.data;
     open.value = true;
     title.value = "修改渠道";
   });
 }
 
 /** 提交按钮 */
 function submitForm() {
   proxy.$refs["channelRef"].validate(valid => {
     if (valid) {
       if (form.value.channelId != undefined) {//用于更新
         updateChannel(form.value).then(response => {
           proxy.$modal.msgSuccess("修改成功");
           open.value = false;
           getList();
         });
       } else {
         //form.value.channelIds = getMenuAllCheckedKeys();
         addChannel(form.value).then(response => {
           proxy.$modal.msgSuccess("新增成功");
           open.value = false;
           getList();
         });
       }
     }
   });
 }
 /** 取消按钮 */
 function cancel() {
   open.value = false;
   reset();
 }
 
 getList();
 </script>
 