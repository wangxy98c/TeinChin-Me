package com.wangxy.tienchin.clue.service;

import com.wangxy.tienchin.clue.domain.FollowRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangxy.tienchin.common.core.domain.AjaxResult;

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
}
