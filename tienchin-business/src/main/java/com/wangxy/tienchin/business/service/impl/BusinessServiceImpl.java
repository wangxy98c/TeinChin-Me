package com.wangxy.tienchin.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wangxy.tienchin.assign.domain.Assign;
import com.wangxy.tienchin.assign.service.IAssignService;
import com.wangxy.tienchin.business.domain.Business;
import com.wangxy.tienchin.business.domain.BusinessFollow;
import com.wangxy.tienchin.business.domain.BusinessSummaryVO;
import com.wangxy.tienchin.business.mapper.BusinessMapper;
import com.wangxy.tienchin.business.service.IBusinessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangxy.tienchin.common.constant.TienchinConstants;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.utils.SecurityUtils;
import com.wangxy.tienchin.follow.domain.FollowRecord;
import com.wangxy.tienchin.follow.service.IFollowRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangxy
 * @since 2024-01-22
 */
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessMapper, Business> implements IBusinessService {
    @Autowired
    BusinessMapper businessMapper;
    @Override
    public List<BusinessSummaryVO> selectBusinessList(BusinessSummaryVO businessSummaryVO) {
        return businessMapper.selectBusinessList(businessSummaryVO);
    }
    @Autowired
    IAssignService assignService;
    @Autowired
    IFollowRecordService followRecordService;

    @Override
    public Boolean deleteBusinessByIds(Long[] businessIds) {
        //逻辑删除
        UpdateWrapper<Business> uw = new UpdateWrapper<>();
        uw.lambda().set(Business::getDelFlag,1).in(Business::getBusinessId,businessIds);
        return update(uw);
    }

    @Override
    public AjaxResult updateBusiness(Business business) {
        return updateById(business)?AjaxResult.success("更新成功"):AjaxResult.error("更新失败");
    }

    @Transactional
    @Override
    public AjaxResult follow(BusinessFollow businessFollow) {
            //商机本身的完善
            Business business = new Business();
            BeanUtils.copyProperties(businessFollow,business);
            business.setUpdateTime(LocalDateTime.now());
            business.setUpdateBy(SecurityUtils.getUsername());
            updateById(business);
            //添加一个跟踪记录
            FollowRecord followRecord = new FollowRecord();
            followRecord.setInfo(businessFollow.getInfo());
            followRecord.setType(TienchinConstants.BUSINESS_TYPE);
            followRecord.setCreateBy(SecurityUtils.getUsername());
            followRecord.setCreateTime(LocalDateTime.now());
            followRecord.setAssignId(businessFollow.getBusinessId());
            followRecordService.save(followRecord);
        return AjaxResult.success("跟进保存成功");
    }

    @Override
    public AjaxResult getBusinessById(Integer businessId) {
        return AjaxResult.success(getById(businessId));
    }

    @Transactional
    @Override
    public AjaxResult addBusiness(Business business) {
        //录入时先查询此人是否曾经登记过（依赖于手机号）
        QueryWrapper<Business> qw = new QueryWrapper<>();
        qw.lambda().eq(Business::getPhone,business.getPhone());
        Business one = getOne(qw);
        if(one!=null){//说明录入过了
            return AjaxResult.error("手机号码重复，客户此前已经登记过了");
        }
        try {
            business.setCreateBy(SecurityUtils.getUsername());
            business.setCreateTime(LocalDateTime.now());
            business.setStatus(TienchinConstants.BUSINESS_ALLOCATED);
            business.setNextTime(LocalDateTime.now().plusHours(TienchinConstants.NEXT_FOLLOW_TIME));
            save(business);
            //
            Assign assign = new Assign();
            assign.setUserName(TienchinConstants.ADMIN_NAME);
            assign.setType(TienchinConstants.BUSINESS_TYPE);
            assign.setCreateBy(SecurityUtils.getUsername());
            assign.setCreateTime(LocalDateTime.now());
            assign.setAssignId(business.getBusinessId());
            assign.setDeptId(SecurityUtils.getDeptId());
            assign.setUserId(TienchinConstants.ADMIN_ID);
            assignService.save(assign);
        } catch (Exception e) {
            return AjaxResult.error("添加商机错误"+e.getMessage());
        }

        return AjaxResult.success("商机添加成功");
    }
}
