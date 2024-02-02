package com.wangxy.tienchin.business.mapper;

import com.wangxy.tienchin.business.domain.Business;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangxy.tienchin.business.domain.BusinessSummaryVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangxy
 * @since 2024-01-22
 */
public interface BusinessMapper extends BaseMapper<Business> {

    List<BusinessSummaryVO> selectBusinessList(BusinessSummaryVO businessSummaryVO);
}
