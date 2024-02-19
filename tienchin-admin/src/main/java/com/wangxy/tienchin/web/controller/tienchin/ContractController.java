package com.wangxy.tienchin.web.controller.tienchin;

import com.wangxy.tienchin.common.core.controller.BaseController;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.core.page.TableDataInfo;
import com.wangxy.tienchin.contract.domain.Contract;
import com.wangxy.tienchin.contract.domain.ContractApproveInfo;
import com.wangxy.tienchin.contract.domain.ContractSummary;
import com.wangxy.tienchin.contract.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangxy
 * @since 2024-02-03
 */
@RestController
@RequestMapping("/tienchin/contract")
public class ContractController extends BaseController {
    @Autowired
    IContractService contractService;
    @PreAuthorize("hasPermission('tienchin:contract:create')")
    @PostMapping("/upload")
    public AjaxResult uploadContractFile(HttpServletRequest req, MultipartFile file){
        return contractService.uploadContractFile(req,file);
    }
    @PreAuthorize("hasPermission('tienchin:contract:create')")
    @PostMapping()
    public AjaxResult addContract(@RequestBody Contract contract){
        return contractService.addContract(contract);
    }
    @PreAuthorize("hasPermission('tienchin:contract:list')")
    @GetMapping("/unapprove")
    public TableDataInfo getUnapproveTask(){
        startPage();
        List<ContractSummary> list = contractService.getUnapproveTask();
        return getDataTable(list);
    }

    @PreAuthorize("hasPermission('tienchin:contract:list')")
    @GetMapping("/commit")
    public TableDataInfo getCommitTask(){
        startPage();
        List<ContractSummary> list = contractService.getCommitTask();
        return getDataTable(list);
    }
    @PreAuthorize("hasPermission('tienchin:contract:view')")
    @GetMapping("/views/{contractId}")
    public AjaxResult getContractById(@PathVariable Integer contractId){
        return contractService.getContractById(contractId);
    }

    @PreAuthorize("hasPermission('tienchin:contract:approve')")
    @PostMapping("/approveOrReject")
    public AjaxResult approveOrReject(@RequestBody ContractApproveInfo contractApproveInfo){
        System.out.println("---------------------------");
        return contractService.approveOrReject(contractApproveInfo);
    }
    @PreAuthorize("hasPermission('tienchin:contract:approve')")
    @GetMapping("/approved")//返回已经提交且审批通过的流程
    public TableDataInfo getApprovedTask(){
        startPage();
        List<ContractSummary> list = contractService.getApprovedTask();
        return getDataTable(list);
    }
    @PreAuthorize("hasPermission('tienchin:contract:edit')")
    @PostMapping("/edit")
    public AjaxResult editContract(@RequestBody Contract contract){
        return contractService.editContract(contract);
    }

}
