package com.wangxy.tienchin.follow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangxy.tienchin.common.constant.TienchinConstants;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.follow.domain.FollowRecord;
import com.wangxy.tienchin.follow.mapper.FollowRecordMapper;
import com.wangxy.tienchin.follow.service.IFollowRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 线索、商机的跟踪表。 服务实现类
 * </p>
 *
 * @author wangxy
 * @since 2023-10-16
 */
@Service
public class FollowRecordServiceImpl extends ServiceImpl<FollowRecordMapper, FollowRecord> implements IFollowRecordService {
    @Override
    public AjaxResult getFollowRecordByClueId(Integer clueId) {
        QueryWrapper<FollowRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FollowRecord::getType, TienchinConstants.CLUE_TYPE).eq(FollowRecord::getAssignId,clueId).orderByDesc(FollowRecord::getCreateTime );
        return AjaxResult.success(list(queryWrapper));
    }

    @Override
    public AjaxResult getFollowRecordByBusinessId(Integer businessId) {
        QueryWrapper<FollowRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FollowRecord::getType, TienchinConstants.BUSINESS_TYPE).eq(FollowRecord::getAssignId,businessId).orderByDesc(FollowRecord::getCreateTime );
        return AjaxResult.success(list(queryWrapper));
    }
}
