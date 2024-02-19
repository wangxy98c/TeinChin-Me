package com.wangxy.tienchin.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wangxy.tienchin.business.domain.Business;
import com.wangxy.tienchin.business.service.IBusinessService;
import com.wangxy.tienchin.common.constant.TienchinConstants;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.utils.SecurityUtils;
import com.wangxy.tienchin.contract.domain.Contract;
import com.wangxy.tienchin.contract.domain.ContractApproveInfo;
import com.wangxy.tienchin.contract.domain.ContractSummary;
import com.wangxy.tienchin.contract.mapper.ContractMapper;
import com.wangxy.tienchin.contract.service.IContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangxy
 * @since 2024-02-03
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

    @Value("${tienchin.contract.file}")
    String contractFolder;
    @Autowired
    IBusinessService businessService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("/yyyy/MM/dd/");//注意，斜杠不能少。用于将上传的文件按日期分文件夹
    @Override
    public AjaxResult uploadContractFile(HttpServletRequest req, MultipartFile file) {
        //允许通过配置文件配置
        String format = simpleDateFormat.format(new Date());
        String fileDir=contractFolder+ format;
        File dir=new File(fileDir);
        if(!dir.exists()){//文件夹不存在就建出来
            dir.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        //新的文件名
        String newName = UUID.randomUUID().toString() + "-" + originalFilename;
        try {
            file.transferTo(new File(dir,newName));
            //返回前端口请求的
            String url=req.getScheme()+"://"
                    +req.getServerName()+":"
                    +req.getServerPort()
                    +req.getContextPath()
                    +"/tienchin/contract/views"
                    +format+newName;//生成了文件的网络地址（点击后可请求文件），需要提供接口
            HashMap<Object, Object> data = new HashMap<>();
            data.put("name",originalFilename);
            data.put("url",url);//不想封装成对象了
            return AjaxResult.success(data);
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
        return AjaxResult.error("文件上传失败");
    }

    @Transactional
    @Override
    public AjaxResult addContract(Contract contract) {
        System.out.println(contract.toString());
        //先操作数据库（添加后获得id）
        //1.查询商机id并设置
        QueryWrapper<Business> qw = new QueryWrapper<>();
        qw.lambda().eq(Business::getPhone,contract.getPhone()).orderByDesc(Business::getCreateTime);
        List<Business> list = businessService.list(qw);
        if(list==null){
            return AjaxResult.error("未查询到支持此电话号码的商机,请先添加商机 ");
        }
        Integer businessId=list.get(0).getBusinessId();
        contract.setBusinessId(businessId);
        //这里从前端传来了课程价格了，可以再查询一次。但这里不查询了，影响不大。主要取决于最终合同价格
        contract.setCreateBy(SecurityUtils.getUsername());
        contract.setCreateTime(LocalDateTime.now());
        contract.setStatus(TienchinConstants.CONTRACT_UNAPPROVE);
        save(contract);

        //启动流程
        //Authentication.setAuthenticatedUserId(SecurityUtils.getUsername());
        //此用户发起的流程,但不能用。因为用了它就相当于用了follow里的security体系。导致原本的认证体系失效。变为启动式就设置INITATOR
        Map<String,Object> pivars=new HashMap<>();
        pivars.put("currentUser",SecurityUtils.getUsername());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(TienchinConstants.CONTRACT_PROCESS_DEFINITION_ID, pivars);

        Map<String,Object> vars=new HashMap<>();
        vars.put("contractId",contract.getContractId());
        vars.put("approveUser",contract.getApproveUserName());
        vars.put("approveUserId",contract.getApproveUserId());
        //提交信息
        Task task = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId()).singleResult();
        taskService.complete(task.getId(),vars);
        //更新数据库里的流程信息
        contract.setProcessInstanceId(processInstance.getProcessInstanceId());
        updateById(contract);
        return AjaxResult.success("提交成功");
    }

    @Override
    public List<ContractSummary> getUnapproveTask() {
        List<ContractSummary> result=new ArrayList<>();
        //查询当前用户需要处理的任务
        List<Task> list = taskService.createTaskQuery().taskAssignee(SecurityUtils.getUsername())
                .processDefinitionKey(TienchinConstants.CONTRACT_PROCESS_DEFINITION_ID)
                .active().orderByTaskCreateTime().desc().list();
        for (Task task : list) {
            String taskId = task.getId();
            Integer contractId = (Integer) taskService.getVariable(task.getId(),"contractId");
            String reason=(String) taskService.getVariable(task.getId(),"reason");
            ContractSummary summary = new ContractSummary();
            Contract contract = getById(contractId);
            BeanUtils.copyProperties(contract,summary);
            summary.setTaskId(taskId);
            summary.setReason(reason);
            result.add(summary);
        }
        return result;
    }

    @Override
    public AjaxResult getContractById(Integer contractId) {
        Contract contract = getById(contractId);
        return AjaxResult.success(contract);
    }

    @Override
    public List<ContractSummary> getCommitTask() {
        List<ContractSummary> result=new ArrayList<>();
        List<Execution> list = runtimeService.createExecutionQuery().variableValueEquals("currentUser", SecurityUtils.getUsername()).processDefinitionKey(TienchinConstants.CONTRACT_PROCESS_DEFINITION_ID).list();
        for (Execution e : list) {
            String eId = e.getId();
            Integer contractId = (Integer) runtimeService.getVariable(e.getId(), "contractId");
            String reason=(String) runtimeService.getVariable(e.getId(),"reason");
            //String reason = (String) runtimeService.getVariable(e.getId(), "reason");
            ContractSummary summary = new ContractSummary();
            Contract contract = getById(contractId);
            BeanUtils.copyProperties(contract, summary);
            summary.setTaskId(eId);
            summary.setReason(reason);
            result.add(summary);
        }
        return result;
    }

    @Override
    public AjaxResult approveOrReject(ContractApproveInfo contractApproveInfo) {
        //流程审批
        Map<String, Object> vars=new HashMap<>();
        System.out.println();
        vars.put("approve",contractApproveInfo.getApprove());
        vars.put("reason",contractApproveInfo.getReason());
        taskService.complete(contractApproveInfo.getTaskId(),vars);
        //修改合同表中的合同状态
        UpdateWrapper<Contract> uw = new UpdateWrapper<>();
        if(contractApproveInfo.getApprove()){
            uw.lambda().set(Contract::getStatus,TienchinConstants.CONTRACT_APPROVED).eq(Contract::getContractId,contractApproveInfo.getContractId());
        }else {
            uw.lambda().set(Contract::getStatus,TienchinConstants.CONTRACT_REJECT).eq(Contract::getContractId,contractApproveInfo.getContractId());
        }
        update(uw);
        return AjaxResult.success("审批完成");
    }

    @Override
    public List<ContractSummary> getApprovedTask() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().variableValueEquals("currentUser", SecurityUtils.getUsername()).finished() .list();
        ArrayList<ContractSummary> contractSummaries = new ArrayList<>();
        for (HistoricProcessInstance hpi : list) {
            List<HistoricVariableInstance> varList = historyService.createHistoricVariableInstanceQuery().processInstanceId(hpi.getId()).list();
            Integer contractId = null;
            String reason = null;
            for (HistoricVariableInstance hvi : varList) {
                if("contractId".equals(hvi.getVariableName())){
                    contractId=(Integer) hvi.getValue();
                }else if("reason".equals(hvi.getVariableName())){
                    reason=(String) hvi.getValue();
                }
            }
            Contract contract = getById(contractId);
            ContractSummary summary = new ContractSummary();
            BeanUtils.copyProperties(contract,summary);
            summary.setContractId(contractId);
            summary.setReason(reason);
            contractSummaries.add(summary);
        }
        return contractSummaries;
    }

    @Override
    public AjaxResult editContract(Contract contract) {
        //先操作数据库（添加后获得id）
        //这里从前端传来了课程价格了，可以再查询一次。但这里不查询了，影响不大。主要取决于最终合同价格
        contract.setUpdateBy(SecurityUtils.getUsername());
        contract.setUpdateTime(LocalDateTime.now());
        contract.setStatus(TienchinConstants.CONTRACT_UNAPPROVE);
        updateById(contract);

        Map<String,Object> vars=new HashMap<>();
        vars.put("currentUser",SecurityUtils.getUsername());
        vars.put("contractId",contract.getContractId());
        vars.put("approveUser",contract.getApproveUserName());
        vars.put("approveUserId",contract.getApproveUserId());
        //提交信息
        Task task = taskService.createTaskQuery()
                .processInstanceId(contract.getProcessInstanceId()).active().singleResult();
        taskService.complete(task.getId(),vars);
        return AjaxResult.success("提交成功");
    }
}
