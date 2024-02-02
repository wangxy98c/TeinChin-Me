<template>
    <div>
        <el-row :gutter="16">
            <el-col :span="18">
                <el-row>
                    <el-card class="box-card">
                        <template #header>
                            <div class="card-header">
                                <span>{{ clue.name }}/{{ clue.clueId }}</span>
                            </div>
                            <div>
                                <el-row>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">创建线索时间</div>
                                        <div style="color:#8392a6">{{ parseTime(clue.createTime) }}</div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">客户联系方式</div>
                                        <div style="color:#8392a6">{{ clue.phone }}</div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">渠道来源</div>
                                        <div style="color:#8392a6">{{ clue.channelName}}</div>
                                    </el-col>
                                </el-row>
                                <el-row>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">线索分配者</div>
                                        <div style="color:#8392a6">{{ clue.allocator }}</div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">线索归属时间</div>
                                        <div style="color:#8392a6">{{ clue.belongTime }}</div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">线索归属人</div>
                                        <div style="color:#8392a6">{{ clue.owner}}</div>
                                    </el-col>
                                </el-row>
                                <el-row>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">所属促销活动信息</div>
                                        <div style="color:#8392a6">{{ clue.activityName}}</div>
                                    </el-col>
                                    <el-col :span="8">
                                        <div style="font-style: italic;font-weight: bold;">所属活动信息</div>
                                        <div style="color:#8392a6">{{ clue.activityInfo }}</div>
                                    </el-col>
                                    <el-col :span="8">
                                        <el-button type="danger"  @click="showInvalidateClueDialog">无效线索</el-button>
                                        <el-button type="primary"  @click="handleBusiness()">转为商机</el-button>
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
                                <span>线索跟进</span>
                            </div>
                        </template>
                        <div>
                            <!-- gutter是间隔 -->
                            <el-row :gutter="24">
                                <el-col :span="8">
                                    <div style="font-style: italic;font-weight: bold;">客户姓名</div>
                                    <div>
                                        <el-input v-model="clue.name" :disabled="isDisableEdit"></el-input>
                                    </div>
                                </el-col>
                                <el-col :span="8">
                                    <div style="font-style: italic;font-weight: bold;">客户性别</div>
                                    <div>
                                        <el-radio-group v-model="clue.gender" :disabled="isDisableEdit">
                                            <el-radio
                                                v-for="dict in sys_user_sex"
                                                :key="dict.value"
                                                :label="parseInt(dict.value)"
                                            >{{ dict.label }}</el-radio>
                                        </el-radio-group>
                                    </div>
                                </el-col>
                                <el-col :span="8">
                                    <div style="font-style: italic;font-weight: bold;">客户年龄</div>
                                    <div>
                                        <el-input-number v-model="clue.age" :disabled="isDisableEdit" :min="6" :max="99"></el-input-number>
                                    </div>
                                </el-col>
                            </el-row>
                            <el-row :gutter="24">
                                <el-col :span="8">
                                    <div style="font-style: italic;font-weight: bold;">客户手机号码</div>
                                    <div>
                                        <el-input v-model="clue.phone" :disabled="isDisableEdit"></el-input>
                                    </div>
                                </el-col>
                                <el-col :span="8">
                                    <div style="font-style: italic;font-weight: bold;">微信号</div>
                                    <el-input :disabled="isDisableEdit" v-model="clue.weixin"></el-input>
                                </el-col>
                                <el-col :span="8">
                                    <div style="font-style: italic;font-weight: bold;">客户QQ号码</div>
                                    <div>
                                        <el-input v-model="clue.qq" :disabled="isDisableEdit"></el-input>
                                    </div>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col>
                                    <div style="font-style: italic;font-weight: bold;">意向等级</div>
                                    <div>
                                        <el-radio-group v-model="clue.level" class="ml-4" :disabled="isDisableEdit">
                                            <el-radio :label="parseInt(cl.value)" :key="index" v-for="(cl,index) in clue_level">{{ cl.label }}</el-radio>
                                        </el-radio-group>
                                    </div>
                                </el-col>
                                <el-col>
                                    <div style="font-style: italic;font-weight: bold;">意向类型</div>
                                    <div>
                                        <el-radio-group v-model="clue.subject" class="ml-4" :disabled="isDisableEdit">
                                            <el-radio :label="cl.value" :key="index" v-for="(cl,index) in course_type">{{ cl.label }}</el-radio>
                                        </el-radio-group>
                                    </div>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="24">
                                    <div style="font-style: italic;font-weight: bold;">跟进记录</div> 
                                    <div>
                                        <el-input v-model="clue.record" :row="5" type="textarea" placeholder="请输入内容" :disabled="isDisableEdit"></el-input>
                                    </div>
                                </el-col>
                                <el-col>
                                    <div style="font-style: italic;font-weight: bold;">下次跟进时间</div> 
                                    <el-date-picker
                                        v-model="clue.nextTime"
                                        type="datetime"
                                        placeholder="选择下次跟进的时间"
                                        :shortcuts="dateTimeShortcuts"
                                        :disabled="isDisableEdit"
                                    />
                                </el-col>
                            </el-row>
                            <el-row>
                                <div style="display: flex;justify-content: flex-end;">
                                    <el-button type="primary" :disabled="isDisableEdit" @click="handleClueFollow">提交</el-button>
                                    <el-button type="danger" @click="goBack">返回</el-button>
                                </div>
                            </el-row>
                        </div>
                    </el-card>
                </el-row>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>
                        <div class="card-header">线索跟进记录</div>
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
                  v-for="ci in clue_invalidate"
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
import {getClueById,clueFollow,getClueRecordByClueId,invalidateClue,toBusiness} from '@/api/tienchin/clue.js'
import { ElMessage, ElMessageBox } from 'element-plus';
import router from '../../../router';
const { proxy } = getCurrentInstance();
const {clue_status,course_apply_to,course_type,sys_user_sex,clue_level,clue_invalidate} = proxy.useDict("clue_status","course_apply_to","course_type","sys_user_sex","clue_level","clue_invalidate");

const type=ref('');
const isDisableEdit=ref(false);
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
const clue=ref({})
const records=ref({})
const invalidateClueDialog=ref(false)
const data = reactive({
   invalidateClueForm:{},
   invalidateClueRules:{
        reason_type:[{ required: true, message: "必选项", trigger: "blur" }],
   }
 });
 const {invalidateClueRules,invalidateClueForm}=toRefs(data);
 function handleClueInvalidate(){
    proxy.$refs["invalidateClueRef"].validate(valid=>{
    if(valid){
        clue.value.record=invalidateClueForm.value.reason_type+':'+(invalidateClueForm.value.reasonRemark?invalidateClueForm.value.reasonRemark:'')
        invalidateClue(clue.value).then(resp=>{
            proxy.$router.go(-1);
        })
    }
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
        toBusiness(clue.value.clueId).then(resp=>{
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
 function handleClue(clueId){
    getClueById(clueId).then(resp=>{
        clue.value=resp.data;
        isDisableEdit.value=(type.value=='view') || (clue.value.status==4)
    })
 }
 function getRecords(clueId){
    getClueRecordByClueId(clueId).then(resp=>{
        records.value=resp.data;
        console.log('records:',records);
    })
 }
 function goBack(){
    proxy.$router.go(-1);
 }
 function handleClueFollow(){
    clueFollow(clue.value).then(resp=>{
        router.go(-1);
    })
 }

 //跳转的路径是 /clue/detail/index/10/view  其中view代表操作
onMounted(()=>{
    // 当你需要获取当前路由的信息时，使用 $route；当你需要进行页面导航时，使用 $router。
    const clueId=proxy.$route.params && proxy.$route.params.clueId;
    type.value=proxy.$route.params && proxy.$route.params.type; 
    handleClue(clueId);
    getRecords(clueId);
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