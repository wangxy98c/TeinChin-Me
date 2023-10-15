<template>
    <div class="app-container">
       <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
          <el-form-item label="课程名称" prop="name">
             <el-input
                v-model="queryParams.name"
                placeholder="请输入活动名称"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
             />
          </el-form-item>
          <el-form-item label="适用人群" prop="applyTo">
            <el-select v-model="queryParams.applyTo" class="m-2" style="width: 200px;" placeholder="请选择适用人群">
                  <el-option
                     v-for="item in course_apply_to"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value"
                  />
               </el-select>
          </el-form-item>
          <el-form-item label="课程类型" prop="type">
             <el-select v-model="queryParams.type" placeholder="请选择活动的类型" clearable style="width: 200px">
                <el-option
                   v-for="dict in course_type"
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
                v-hasPermi="['tienchin:course:create']"
             >新增</el-button>
          </el-col>
          <el-col :span="1.5">
             <el-button
                type="success"
                plain
                icon="Edit"
                :disabled="single"
                @click="handleUpdate"
                v-hasPermi="['tienchin:course:edit']"
             >修改</el-button>
          </el-col>
          <el-col :span="1.5">
             <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['tienchin:course:remove']"
             >删除</el-button>
          </el-col>
          <el-col :span="1.5">
             <el-button
                type="warning"
                plain
                icon="Download"
                @click="handleExport"
                v-hasPermi="['tienchin:course:export']"
             >导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
       </el-row>
      <!-- 展示表格 -->
       <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="30px" align="center" />
          <el-table-column label="编号" align="center" prop="courseId" />
          <el-table-column label="课程名称" align="center" prop="name" />
          <el-table-column label="课程类型" align="center" prop="type">
            <template #default="scope">
                <dict-tag :options="course_type" :value="scope.row.type"></dict-tag>
            </template>
          </el-table-column>
          <el-table-column label="课程价格" align="center" prop="price" />
          <el-table-column label="适用人群" align="applyTo" width="200px">
             <template #default="scope">
                <dict-tag :options="course_apply_to" :value="scope.row.applyTo" />
             </template>
          </el-table-column>
          <el-table-column label="课程简介" align="center" prop="info" :show-overflow-tooltip="true" width="80px"/>
          <el-table-column label="操作" width="180" align="center" class-name="small-padding fixed-width">
             <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['tienchin:course:edit']">修改</el-button>
                <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['tienchin:course:remove']">删除</el-button>
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
 
       <!-- 添加或修改课程对话框 -->
       <el-dialog :title="title" v-model="open" width="500px" append-to-body>
          <el-form ref="courseRef" :model="form" :rules="rules" label-width="120px">
             <el-form-item label="课程名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入课程名称"/>
             </el-form-item>
             <el-form-item label="课程类型" prop="type">
                <el-select v-model="form.type" class="m-2" placeholder="请选择课程类型">
                  <el-option
                     v-for="item in course_type"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value"
                  />
                  <!-- 这里有个非常神奇的事情：此处不用parseInt即可实现转换，
                     但下面的applyTo则必须parseInt才能正常转换。都是字典，类型应该一样啊？ -->
               </el-select>
             </el-form-item>
             <el-form-item label="课程价格" prop="price">
                <el-input-number v-model="form.price" placeholder="输入课程的价格" :min="0" :precision="2" :step="100"></el-input-number>
             </el-form-item>
             <el-form-item label="适用人群" prop="applyTo">
               <el-select v-model="form.applyTo" class="m-2" placeholder="请选择课程适用人群">
                  <el-option
                     v-for="item in course_apply_to"
                     :key="item.value"
                     :label="item.label"
                     :value="parseInt(item.value)"
                  />
               </el-select>
             </el-form-item>
             <el-form-item label="课程简介" prop="info">
                  <el-input v-model="form.info" placeholder="关于此课程的简介信息"/>
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
 import {listCourse,addCourse,getCourse,updateCourse,delCourse} from "@/api/tienchin/course.js"
 const { proxy } = getCurrentInstance();
 const {course_apply_to,course_type} = proxy.useDict("course_apply_to","course_type");
 
 const courseList = ref([]);
 const open = ref(false);
 const loading = ref(true);
 const showSearch = ref(true);
 const ids = ref([]);
 const single = ref(true);
 const multiple = ref(true);
 const total = ref(0);
 const title = ref("");
 
 const data = reactive({
   form: {},
   queryParams: {
     name: undefined,
     type: undefined,
     applyTo: undefined,
     pageNum: 1,
     pageSize: 10
   },
   rules: {
     name: [{ required: true, message: "活动名称不能为空", trigger: "blur" }],
     applyTo: [{ required: true, message: "请选择课程适用人群", trigger: "blur" }],
     price: [{ required: true, message: "请输入课程价格", trigger: "blur" }],
     type: [{ required: true, message: "请选择课程所属类型", trigger: "blur" }]
   }
 });
 
 const { queryParams, form, rules } = toRefs(data);
 
 /** 查询课程列表 */
 function getList() {
   loading.value = true;
   listCourse(queryParams.value).then(response => {
     courseList.value = response.rows;
     total.value = response.total;
     loading.value = false;
   });
 }
 /** 取消按钮 */
 function cancel() {
   open.value = false;
   reset();
 }
 /** 表单重置 */
 function reset() {
   form.value = {
     courseId: undefined,
     name: undefined,
     type: undefined,
     price:undefined,
     applyTo:undefined,
     info:undefined,
   };
   proxy.resetForm("courseRef");
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
   ids.value = selection.map(item => item.courseId);
   single.value = selection.length != 1;
   multiple.value = !selection.length;
 }
 /** 新增按钮操作 */
 function handleAdd() {
   reset();
   open.value = true;
   title.value = "添加课程";
 }

 /** 修改按钮操作 */
 function handleUpdate(row) {
   reset();
   const courseId = row.courseId || ids.value;
   //查询岗位详细信息，为什么不直接用呢：页面可能长时间没有刷新。
   getCourse(courseId).then(response => {
     form.value = response.data;
     title.value = "修改课程信息";
     form.value.type=String(form.value.type)
     open.value = true;
   });
 }

 /** 提交按钮 */
 function submitForm() {
   //修改或新增请求
   proxy.$refs["courseRef"].validate(valid => {
     if (valid) {
       if (form.value.courseId != undefined) {
         //更新操作
         updateCourse(form.value).then(response => {
           proxy.$modal.msgSuccess("修改成功");
           open.value = false;
           getList();
         });
       } else {
         //添加操作
         addCourse(form.value).then(response => {
           proxy.$modal.msgSuccess("新增课程成功");
           open.value = false;
           getList();
         });
       }
     }
   });
 }

 /** 删除按钮操作 */
 function handleDelete(row) {
   const courseIds = row.courseId || ids.value;
   proxy.$modal.confirm('是否确认删除活动编号为"' + courseIds + '"的数据项？').then(function() {
     return delCourse(courseIds);
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
 </script>
 