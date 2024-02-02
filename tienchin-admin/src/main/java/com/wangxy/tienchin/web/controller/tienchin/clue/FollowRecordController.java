package com.wangxy.tienchin.web.controller.tienchin.clue;

import com.wangxy.tienchin.common.core.controller.BaseController;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.follow.service.IFollowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 线索、商机的跟踪表。 前端控制器
 * </p>
 *
 * @author wangxy
 * @since 2023-10-16
 */
@RestController
@RequestMapping("tienchin/follow/record")
public class FollowRecordController extends BaseController {
    @Autowired
    IFollowRecordService followRecordService;
    @PreAuthorize("hasAnyPermission('tienchin:clue:view','tienchin:clue:follow')")
    @GetMapping("/clue/{clueId}")
    public AjaxResult getFollowRecordByClueId(@PathVariable Integer clueId){
        return followRecordService.getFollowRecordByClueId(clueId);
    }
    @PreAuthorize("hasAnyPermission('tienchin:business:view','tienchin:business:follow')")
    @GetMapping("/business/{businessId}")
    public AjaxResult getFollowRecordByBusinessId(@PathVariable Integer businessId){
        return followRecordService.getFollowRecordByBusinessId(businessId);
    }
}
