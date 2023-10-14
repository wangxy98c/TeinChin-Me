package com.wangxy.tienchin.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wangxy.tienchin.activity.domain.Activity;
import com.wangxy.tienchin.activity.domain.vo.ActivityVO;
import com.wangxy.tienchin.activity.mapper.ActivityMapper;
import com.wangxy.tienchin.activity.service.IActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangxy
 * @since 2023-10-03
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    //因为要分页，不实用mybatisPlus的QueryMapper
    @Autowired
    ActivityMapper activityMapper;
    @Override
    public boolean deleteActivityByIds(Long[] activityIds) {
        UpdateWrapper<Activity> uw = new UpdateWrapper<>();
        uw.lambda().set(Activity::getDelFlag,1).in(Activity::getActivityId,activityIds);//把delFlag设为1（where）所有channelIds
        return update(uw);
    }
    @Override
    public List<ActivityVO> selectActivityList(ActivityVO activityVO) {
        verifyActivity();//每次查询前都先判断处理可能过期的活动。
        List<ActivityVO> activities = activityMapper.selectActivityList(activityVO);
        return activities;
    }

    @Override
    public AjaxResult addActivity(ActivityVO activityVO) {
        Activity activity = new Activity();
        System.out.println("VO===>"+activityVO.getBeginTime());
        BeanUtils.copyProperties(activityVO,activity);
        activity.setCreateBy(SecurityUtils.getUsername());
        activity.setCreateTime(LocalDateTime.now());
        //此处可以设置del_flag
        return save(activity)?AjaxResult.success("添加成功"):AjaxResult.error("添加出错");
    }

    @Override
    public AjaxResult updateActivity(ActivityVO activityVO) {
        Activity activity=new Activity();
        BeanUtils.copyProperties(activityVO,activity);
        activity.setUpdateBy(SecurityUtils.getUsername());
        activity.setUpdateTime(LocalDateTime.now());
        activity.setCreateBy(null);//保障不修改字段
        activity.setCreateTime(null);
        activity.setDelFlag(null);
        return updateById(activity)?AjaxResult.success("活动更新成功"):AjaxResult.error("活动更新失败");
    }

    //将实际未开始或已过期的活动设置正确的status(每次查询前执行）
    public boolean verifyActivity(){
        //处理过期的活动
        UpdateWrapper<Activity> uw = new UpdateWrapper<>();
        uw.lambda().set(Activity::getStatus,2).lt(Activity::getEndTime, LocalDateTime.now());
        update(uw);
        uw.clear();
        //处理还没开始的活动
        uw.lambda().set(Activity::getStatus,0).ge(Activity::getBeginTime,LocalDateTime.now());
        //处理还在进行的活动
        uw.clear();
        uw.lambda().set(Activity::getStatus,1).lt(Activity::getBeginTime,LocalDateTime.now()).ge(Activity::getEndTime,LocalDateTime.now());
        return  update(uw);
    }

}
