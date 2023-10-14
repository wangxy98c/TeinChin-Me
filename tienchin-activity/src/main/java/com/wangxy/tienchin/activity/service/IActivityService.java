package com.wangxy.tienchin.activity.service;

import com.wangxy.tienchin.activity.domain.Activity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangxy.tienchin.activity.domain.vo.ActivityVO;
import com.wangxy.tienchin.common.core.domain.AjaxResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangxy
 * @since 2023-10-03
 */
public interface IActivityService extends IService<Activity> {

    List<ActivityVO> selectActivityList(ActivityVO activityVO);

    AjaxResult addActivity(ActivityVO activityVO);

    AjaxResult updateActivity(ActivityVO activityVO);

    boolean deleteActivityByIds(Long[] activityIds);
}
