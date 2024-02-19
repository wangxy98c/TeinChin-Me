package com.wangxy.tienchin.contract.service;

import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.contract.domain.Contract;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangxy.tienchin.contract.domain.ContractApproveInfo;
import com.wangxy.tienchin.contract.domain.ContractSummary;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangxy
 * @since 2024-02-03
 */
public interface IContractService extends IService<Contract> {

    AjaxResult uploadContractFile(HttpServletRequest req, MultipartFile file);

    AjaxResult addContract(Contract contract);

    List<ContractSummary> getUnapproveTask();

    AjaxResult getContractById(Integer contractId);

    List<ContractSummary> getCommitTask();

    AjaxResult approveOrReject(ContractApproveInfo contractApproveInfo);

    List<ContractSummary> getApprovedTask();

    AjaxResult editContract(Contract contract);
}
