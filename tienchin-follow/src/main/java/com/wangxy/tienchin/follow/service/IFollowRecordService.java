package com.wangxy.tienchin.follow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.follow.domain.FollowRecord;

/**
 * <p>
 * 线索、商机的跟踪表。 服务类
 * </p>
 *
 * @author wangxy
 * @since 2023-10-16
 */
public interface IFollowRecordService extends IService<FollowRecord> {

    AjaxResult getFollowRecordByClueId(Integer clueId);

    AjaxResult getFollowRecordByBusinessId(Integer businessId);
}
