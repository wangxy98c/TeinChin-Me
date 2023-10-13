package com.wangxy.tienchin.activity.mapper;

import com.wangxy.tienchin.activity.domain.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangxy.tienchin.activity.domain.vo.ActivityVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangxy
 * @since 2023-10-03
 */
public interface ActivityMapper extends BaseMapper<Activity> {

    List<ActivityVO> selectActivityList();
}
