<template>
    <div>
        <el-row :gutter="24">
            <el-col :span="18">
                <el-row>
                    <el-card class="box-card">
                        <template #header>
                            <div class="card-header">
                                <span>客户资料：{{ business.name }}/{{ business.businessId }}</span>
                            </div>
                            <div>
                                <el-row :gutter="24">
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">客户姓名</div>
                                        <div>
                                            <el-input v-model="business.name" placeholder="客户姓名" :disabled="isDisableEdit"></el-input>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">职业</div>
                                        <div>
                                            <el-select v-model="business.occupation" class="m-2" placeholder="请选择职业大类">
                                                <el-option
                                                    v-for="item in business_occupation"
                                                    :key="item.value"
                                                    :label="item.label"
                                                    :value="item.value"
                                                />
                                            </el-select>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">性别</div>
                                        <div>
                                            <el-radio-group v-model="business.gender" :disabled="isDisableEdit">
                                                <el-radio
                                                    v-for="dict in sys_user_sex"
                                                    :key="dict.value"
                                                    :label="parseInt(dict.value)"
                                                >{{ dict.label }}</el-radio>
                                            </el-radio-group>
                                        </div>
                                    </el-col>
                                    
                                </el-row>
                                <el-row :gutter="24">
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">年龄</div>
                                        <div>
                                            <el-input-number v-model="business.age" :disabled="isDisableEdit" :min="6" :max="99"></el-input-number>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">体重/kg</div>
                                        <div>
                                            <el-input-number v-model="business.weight" :disabled="isDisableEdit" :min="10" :max="250"></el-input-number>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">身高</div>
                                        <div>
                                            <el-input-number v-model="business.height" :disabled="isDisableEdit" :min="30" :max="300"></el-input-number>
                                            <span style="font-size: 15px;">厘米</span>
                                        </div>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="24">
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">手机号码</div>
                                        <div>
                                            <el-input v-model="business.phone" :row="5" placeholder="手机号码" :disabled="isDisableEdit"></el-input>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">微信号码</div>
                                        <div>
                                            <el-input v-model="business.weixin" :row="5" placeholder="微信号码" :disabled="isDisableEdit"></el-input>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">QQ</div>
                                        <div>
                                            <el-input v-model="business.qq" :row="5" placeholder="qq号码" :disabled="isDisableEdit"></el-input>
                                        </div>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="24">
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">所在地区</div>
                                        <el-cascader
                                            :disabled="isDisableEdit"
                                            :options="pcTextArr"
                                            v-model="business.address">
                                        </el-cascader>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">学历</div>
                                        <el-select
                                            v-model="business.education"
                                            aria-placeholder="请选择学历"
                                            clearable 
                                            style="width: 100%;"
                                            :disabled="isDisableEdit"
                                            >
                                                <el-option
                                                v-for="ci in business_education"
                                                :key="ci.value"
                                                :label="ci.label"
                                                :value="ci.label"
                                                >
                                                <!-- 此处:value中的value（代替掉userId:）用于控制选中的颜色，如果没有此字段则选中也是蓝色加粗字体 -->
                                                </el-option>
                                            </el-select>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">备注信息</div>
                                        <el-input v-model="business.remark" placeholder="请输入内容" :disabled="isDisableEdit"></el-input>
                                    </el-col>
                                </el-row>
                            </div>
                        </template>
                        <template #footer></template>
                    </el-card>
                </el-row>
                <el-row>
                    <el-card class="box-card">
                        <template #header>
                            <div class="card-header">
                                <span>客户意向</span>
                            </div>
                        </template>
                        <div>
                            <!-- gutter是间隔 -->
                            <el-row :gutter="24">
                                <el-col :span="8">
                                    <div style="font-style: italic;font-weight: bold;">意向课程类型</div>
                                    <div>
                                        <el-select v-model="business.type" class="m-2" placeholder="请选择课程类型" @change="getCourseList">
                                            <el-option
                                                v-for="item in course_type"
                                                :key="item.value"
                                                :label="item.label"
                                                :value="item.value"
                                            />
                                        </el-select>
                                    </div>
                                </el-col>
                                <el-col :span="8">
                                    <div style="font-style: italic;font-weight: bold;">意向课程</div>
                                    <div>
                                        <el-select v-model="business.courseId" class="m-2" placeholder="请选择课程类型">
                                            <el-option
                                                v-for="item in courseList"
                                                :key="item.courseId"
                                                :label="item.name"
                                                :value="item.courseId"
                                            />
                                        </el-select>
                                    </div>
                                </el-col>
                                <el-col :span="8">
                                    <div style="font-style: italic;font-weight: bold;">每周可支配时间</div>
                                    <div>
                                        <el-input-number v-model="business.hours" :disabled="isDisableEdit" :step="1"></el-input-number>
                                        <span style="font-size: 15px;">小时</span>
                                    </div>
                                </el-col>
                            </el-row>
                            <el-row :gutter="24">
                                <el-col :span="8">
                                    <div style="font-style: italic;font-weight: bold;">健身原因</div>
                                    <div>
                                        <el-input v-model="business.reason" :disabled="isDisableEdit"></el-input>
                                    </div>
                                </el-col>
                                <el-col :span="8">
                                    <div style="font-style: italic;font-weight: bold;">预算</div>
                                    <div>
                                        <el-input-number v-model="business.money" :disabled="isDisableEdit" :step="100"></el-input-number>
                                    </div>
                                </el-col>
                                <el-col :span="8">
                                    <div style="font-style: italic;font-weight: bold;">其他意向</div>
                                    <el-input :disabled="isDisableEdit" v-model="business.otherIntention"></el-input>
                                </el-col>
                            </el-row>
                        </div>
                    </el-card>
                    <el-card>
                        <template #header>
                            <div class="card-header">
                                <span>沟通记录</span>
                            </div>
                            <div>
                                <el-row :gutter="24">
                                    <el-col :span="6">
                                        <div style="font-style: italic;">跟进状态</div> 
                                        <div>
                                            <el-select v-model="business.status" class="m-2" placeholder="请选择">
                                                <el-option
                                                    v-for="item in business_status"
                                                    :key="item.value"
                                                    :label="item.label"
                                                    :value="parseInt(item.value)"
                                                />
                                            </el-select>
                                        </div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;">下次跟进时间</div> 
                                        <div>
                                            <el-date-picker
                                            value-format="YYYY-MM-DD hh:mm:ss"
                                            v-model="business.nextTime"
                                            type="datetime"
                                            placeholder="选择下次跟进的时间"
                                            :shortcuts="dateTimeShortcuts"
                                            :disabled="isDisableEdit"
                                            />
                                        </div>
                                    </el-col>
                                    <el-col :span="6">
                                        <div style="font-style: italic;">沟通重点</div>
                                        <div>
                                            <el-select v-model="business.record1" class="m-2" placeholder="沟通客户的重点方向">
                                                <el-option
                                                    v-for="item in business_focus"
                                                    :key="item.value"
                                                    :label="item.label"
                                                    :value="item.value"
                                                />
                                            </el-select>
                                        </div>
                                    </el-col>
                                    <el-col :span="6">
                                        <div style="font-style: italic;">沟通纪要</div>
                                        <el-input v-model="business.record2" ></el-input>
                                    </el-col>
                                </el-row>
                                <el-row>
                                    <div style="display: flex;justify-content: flex-end;">
                                        <el-button type="primary" :disabled="isDisableEdit" @click="handleSave">保存</el-button>
                                        <el-button type="danger" @click="goBack">返回</el-button>
                                        <el-button type="danger" @click="">转化为成交用户</el-button>
                                    </div>
                                </el-row>
                            </div>
                        </template>
                    </el-card>
                </el-row>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>
                        <div class="card-header">商机跟进记录</div>
                    </template>
                    <div v-for="(r,index) in records" :key="index" style="margin-bottom: 20px;">
                        <span style="font-weight: bold;font-style: italic;font-size: 14px;">{{ r.createBy }}:</span>
                        <span style="font-size: 10px;">{{ parseTime(r.createTime) }}  {{ r.info }}</span>
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <el-dialog title="无效线索" v-model="invalidateClueDialog" width="500px" append-to-body>
          <el-form ref="invalidateClueRef" :model="invalidateClueForm" :rules="invalidateClueRules" label-width="120px">
            <el-form-item label="失效原因" prop="reason_type">
                <el-select
                v-model="invalidateClueForm.reason_type"
                aria-placeholder="请选择失效原因"
                clearable 
                style="width: 100%;"
                :disabled="isDisableEdit"
                >
                    <el-option
                    v-for="ci in business_invalidate"
                    :key="ci.value"
                    :label="ci.label"
                    :value="ci.label"
                    >
                    <!-- 此处:value中的value（代替掉userId:）用于控制选中的颜色，如果没有此字段则选中也是蓝色加粗字体 -->
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="备注" prop="reasonRemark" >
              <el-input
                v-model="invalidateClueForm.reasonRemark"
                :row="5"
                :disabled="isDisableEdit"
                type="textarea"
                placeholder="请输入备注"
              >
                
              </el-input>
            </el-form-item>
          </el-form>
          <template #footer>
             <div class="dialog-footer">
                <el-button type="primary" @click="handleClueInvalidate">确 定</el-button>
                <el-button @click="invalidateCluecancel">取 消</el-button>
             </div>
          </template>
       </el-dialog>
    </div>
</template>
<script setup>
import { onMounted, ref, toRef } from 'vue';  
import {businessSave,getCourseByType,getBusinessById,getBusinessRecordById} from '@/api/tienchin/business.js'
import {getClueById,getClueRecordByClueId,invalidateClue,toBusiness} from '@/api/tienchin/clue.js'
import { ElMessage, ElMessageBox } from 'element-plus';
import router from '../../../router';
import {
  provinceAndCityData,
  pcTextArr,
  regionData,
  pcaTextArr,
  codeToText,
} from "element-china-area-data";
const { proxy } = getCurrentInstance();
const {business_occupation,business_education,course_type,sys_user_sex,business_focus,business_invalidate,business_status} = proxy.useDict("business_occupation","business_education","course_type","sys_user_sex","business_focus","business_invalidate","business_status");

const type=ref('');
const isDisableEdit=ref(false);
const dateTimeShortcuts = [//日期快速选择
  {
    text: '三天后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 3+ 8 * 60 * 1000)
      return date
    },
  },
  {
    text: '一周后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 7+8 * 60 * 1000)
      return date
    },
  },
  {
    text: '一个月后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 30+8 * 60 * 1000)
      return date
    },
  },
  {
    text: '三个月后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 90+8 * 60 * 1000)
      return date
    },
  },
  {
    text: '半年后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 180+8 * 60 * 1000)
      return date
    },
  },
  {
    text: '一年后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 365 + 8 * 60 * 1000)
      return date
    },
  }
]
const records=ref({})
const courseList=ref([])
const invalidateClueDialog=ref(false)
const data = reactive({
   invalidateClueForm:{},
   invalidateClueRules:{
        reason_type:[{ required: true, message: "必选项", trigger: "blur" }],
   },
   business:{
        businessId: undefined,
        name: undefined,
        channelId: undefined,
        activityId: undefined,
        gender: undefined,
        age: undefined,
        weixin: undefined,
        education: undefined,
        money: undefined,
        qq: undefined,
        phone: undefined,
        level: undefined,
        subject: undefined,
        status: undefined,
        failCount: undefined,
        nextTime: undefined,
        endTime: undefined,
        transfer: undefined,
        remark: undefined,
        createTime: undefined,
        createBy: undefined,
        updateTime: undefined,
        city: undefined,
        area: undefined,
        updateBy: undefined,
        delFlag: undefined,
        address:undefined,
        province: undefined,
        courseId: undefined,
        occupation: undefined,
        height: undefined,
        weight: undefined,
        reason: undefined,
        hours: undefined,
        otherIntention: undefined,
        clueId: undefined
   }
 });
 const {invalidateClueRules,invalidateClueForm,business}=toRefs(data);
 function handleClueInvalidate(){
    proxy.$refs["invalidateClueRef"].validate(valid=>{
    if(valid){
        business.value.record=invalidateClueForm.value.reason_type+':'+(invalidateClueForm.value.reasonRemark?invalidateClueForm.value.reasonRemark:'')
        invalidateClue(business.value).then(resp=>{
            proxy.$router.go(-1);
        })
    }
  })
 }
 //根据类型获得有哪些课程
 function getCourseList(){
    getCourseByType(business.value.type).then(resp=>{
        courseList.value=resp.data;
        console.log('课程列表',courseList)
    })
 }
 function handleBusiness(){
    ElMessageBox.confirm(
    '确认要将此线索转化为商机?',
    'Warning',
    {
      confirmButtonText: '确定转化',
      cancelButtonText: '取消',
      type: 'warning',
    })
    .then(() => {
        toBusiness(business.value.businessId).then(resp=>{
            ElMessage({
                type:'success',
                message:'转化成功'
            })
            proxy.$route.go(-1)
        })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消转化线索',
      })
    })
 }
 function invalidateCluecancel(){
    invalidateClueDialog.value=false;
 }
 function showInvalidateClueDialog(){
    invalidateClueDialog.value=true;
 }
 function getBusiness(businessId){
    getBusinessById(businessId).then(resp=>{
        business.value=resp.data;
        console.log('business====',business)
        business.value.address=[]//此处不可以 ={}。必须是数组类型
        business.value.address[0]=business.value.province
        business.value.address[1]=business.value.area
        console.log("地区选择",business.value)
        isDisableEdit.value=(type.value=='view') || (business.value.status==4)
    })
 }
 function getRecords(businessId){
    getBusinessRecordById(businessId).then(resp=>{
        records.value=resp.data;
        console.log('records:',records);
    })
 }
 function goBack(){
    proxy.$router.go(-1);
 }
 function handleSave(){
  business.value.province=business.value.address[0]
  business.value.area=business.value.address[1]
    businessSave(business.value).then(resp=>{
        router.go(-1);
    })
 }

 //跳转的路径是 /business/detail/index/10/view  其中view代表操作
onMounted(()=>{
    // 当你需要获取当前路由的信息时，使用 $route；当你需要进行页面导航时，使用 $router。
    const businessId=proxy.$route.params && proxy.$route.params.businessId;
    console.log('businessId:',business)
    type.value=proxy.$route.params && proxy.$route.params.type; 
    getBusiness(businessId);
    getRecords(businessId);
})
</script>
<style scoped>
.box-card{
    margin-top: 10px;
    width: 100%;
}
.el-row{
    margin-bottom: 16px;
}
.el-row:last-child{
    margin-bottom: 10px;
} 
</style> 