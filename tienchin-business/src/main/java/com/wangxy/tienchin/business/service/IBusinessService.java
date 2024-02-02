package com.wangxy.tienchin.business.service;

import com.wangxy.tienchin.business.domain.Business;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangxy.tienchin.business.domain.BusinessSummaryVO;
import com.wangxy.tienchin.common.core.domain.AjaxResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangxy
 * @since 2024-01-22
 */
public interface IBusinessService extends IService<Business> {

    List<BusinessSummaryVO> selectBusinessList(BusinessSummaryVO businessSummaryVO);

    AjaxResult addBusiness(Business business);

    AjaxResult getBusinessById(Integer businessId);

    AjaxResult follow(com.wangxy.tienchin.business.domain.BusinessFollow businessFollow);

    AjaxResult updateBusiness(Business business);

    Boolean deleteBusinessByIds(Long[] businessIds);
}
