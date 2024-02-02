package com.wangxy.tienchin.clue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wangxy.tienchin.assign.domain.Assign;
import com.wangxy.tienchin.assign.service.IAssignService;
import com.wangxy.tienchin.business.domain.Business;
import com.wangxy.tienchin.business.service.IBusinessService;
import com.wangxy.tienchin.clue.domain.Clue;
import com.wangxy.tienchin.clue.domain.vo.ClueDetail;
import com.wangxy.tienchin.clue.domain.vo.ClueSummaryVO;
import com.wangxy.tienchin.clue.domain.vo.ClueVO;
import com.wangxy.tienchin.clue.mapper.ClueMapper;
import com.wangxy.tienchin.clue.service.IClueService;
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
 * @since 2023-10-16
 */
@Service
public class ClueServiceImpl extends ServiceImpl<ClueMapper, Clue> implements IClueService {
    @Autowired
    IAssignService assignService;
    @Autowired
    ClueMapper clueMapper;
    @Autowired
    IFollowRecordService followRecordService;
    @Autowired
    IBusinessService businessService;
    @Override
    public List<ClueSummaryVO> selectClueList(ClueVO clueVO) {
        return clueMapper.selectClueList(clueVO);
    }

    @Transactional
    @Override
    public AjaxResult clue2Bussiness(Integer clueId) {
        try {
            Clue clue = getById(clueId);
            //此出用到business模块，需要添加依赖
            Business business = new Business();
            BeanUtils.copyProperties(clue,business);
            business.setCreateBy(SecurityUtils.getUsername());
            business.setCreateTime(LocalDateTime.now());
            business.setEndTime(null);
            business.setFailCount(0);//重置
            business.setNextTime(null);
            business.setRemark(null);
            business.setUpdateBy(null);
            business.setNextTime(LocalDateTime.now().plusHours(TienchinConstants.NEXT_FOLLOW_TIME));
            business.setStatus(TienchinConstants.BUSINESS_ALLOCATED);
            //删除线索,逻辑删除
            UpdateWrapper<Clue> uw = new UpdateWrapper<>();
            uw.lambda().set(Clue::getDelFlag,1).eq(Clue::getClueId,clueId);
            update(uw);
            //添加商机
            businessService.save(business);
            //默认将商机分配给admin
            Assign assign = new Assign();
            assign.setUserName(TienchinConstants.ADMIN_NAME);
            assign.setType(TienchinConstants.BUSINESS_TYPE);
            assign.setCreateBy(SecurityUtils.getUsername());
            assign.setCreateTime(LocalDateTime.now());
            assign.setAssignId(business.getBusinessId());
            assign.setDeptId(SecurityUtils.getDeptId());
            assign.setUserId(TienchinConstants.ADMIN_ID);
            assignService.save(assign);
        } catch (BeansException e) {
            return AjaxResult.error("转化失败"+e.getMessage());
        }
        return AjaxResult.success("转化成功");
    }

    @Override
    public Boolean deleteClueByIds(Long[] clueIds) {
        //逻辑删除
        UpdateWrapper<Clue> uw = new UpdateWrapper<>();
        uw.lambda().set(Clue::getDelFlag,1).in(Clue::getClueId,clueIds);
        return update(uw);
    }

    @Override
    public AjaxResult getClueSummaryByClueId(Integer clueId) {
        Clue clue = getById(clueId);
        return AjaxResult.success(clue);
    }

    @Override
    public AjaxResult updateClue(Clue clue) {
        return updateById(clue)?AjaxResult.success("更新成功"):AjaxResult.error("更新失败");
    }

    @Override
    @Transactional
    public AjaxResult invalidateClueFollow(ClueDetail clueDetail) {
        try {
            //如果已经失败三次了，直接变伪线索（即本次是第四次）
            Clue clue = getById(clueDetail.getClueId());
            if(clue.getFailCount()==3){
                UpdateWrapper<Clue> updateWrapper = new UpdateWrapper<>();
                updateWrapper.lambda().set(Clue::getStatus,TienchinConstants.CLUE_INVALIDATE).eq(Clue::getClueId,clueDetail.getClueId());
                update(updateWrapper);
                return AjaxResult.success("无效线索次数已达上限,线索失效");
            }
            //设置tienchin_clue的fail_count++(失败三次才废弃)
            UpdateWrapper<Clue> uw = new UpdateWrapper<>();
            uw.lambda().setSql("fail_count=fail_count+1").eq(Clue::getClueId,clueDetail.getClueId());
            update(uw);
            //添加记录
            FollowRecord followRecord = new FollowRecord();
            followRecord.setAssignId(clueDetail.getClueId());
            followRecord.setCreateBy(SecurityUtils.getUsername());
            followRecord.setCreateTime(LocalDateTime.now());
            followRecord.setType(TienchinConstants.CLUE_TYPE);
            followRecord.setInfo(clueDetail.getRecord());
            followRecordService.save(followRecord);
        } catch (Exception e) {
            return AjaxResult.error("设置失败"+e.getMessage());
        }
        return AjaxResult.success("设置成功");
    }

    @Transactional
    @Override
    public AjaxResult clueFollow(ClueDetail clueDetail) {
        try {
            //更新clue表
            //UpdateWrapper<Clue> uw = new UpdateWrapper<>();
            //update(uw);不这样写的原因是ClueDetail转Clue太麻烦,更需要判断一些字段是否存在
            Clue clue = new Clue();
            BeanUtils.copyProperties(clueDetail, clue);
            updateById(clue);

            //添加clue_follow表
            FollowRecord followRecord = new FollowRecord();
            followRecord.setAssignId(clueDetail.getClueId());
            followRecord.setCreateBy(SecurityUtils.getUsername());
            followRecord.setCreateTime(LocalDateTime.now());
            followRecord.setType(TienchinConstants.CLUE_TYPE);
            followRecord.setInfo(clueDetail.getRecord());
            followRecordService.save(followRecord);
        } catch (BeansException e) {
           return AjaxResult.error("跟进失败:"+e.getMessage());
        }
        return AjaxResult.success("跟进成功");
    }

    @Override
    public AjaxResult getClueDetailByClueId(Integer clueId) {
        ClueDetail cd=clueMapper.getClueDetailByClueId(clueId);
        return AjaxResult.success(cd);
    }

    @Override
    public AjaxResult addClue(Clue clue) {
        //录入时先查询此人是否曾经登记过（依赖于手机号）
        QueryWrapper<Clue> qw = new QueryWrapper<>();
        qw.lambda().eq(Clue::getPhone,clue.getPhone());
        Clue one = getOne(qw);
        if(one!=null){//说明录入过了
            return AjaxResult.error("手机号码重复，客户此前已经登记过了");
        }
        clue.setCreateBy(SecurityUtils.getUsername());
        clue.setCreateTime(LocalDateTime.now());
        try {
            //添加线索
            save(clue);
            //设置默认的分配人(是两张表)
            Assign assign = new Assign();
            assign.setAssignId(clue.getClueId());//添加线索后会返回有自增长的id
            assign.setLatest(true);//是否最新
            assign.setType(TienchinConstants.CLUE_TYPE);//在common中定义的一个常量类
            assign.setUserName(SecurityUtils.getUsername());
            assign.setUserId(SecurityUtils.getUserId());
            assign.setDeptId(SecurityUtils.getDeptId());
            assign.setCreateBy(SecurityUtils.getUsername());
            assign.setCreateTime(LocalDateTime.now());
            assignService.save(assign);
            return AjaxResult.success("录入成功");
        } catch (Exception e) {
            return AjaxResult.error("录入失败");
        }
    }
}
