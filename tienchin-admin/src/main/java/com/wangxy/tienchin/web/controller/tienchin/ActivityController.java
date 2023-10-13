package com.wangxy.tienchin.web.controller.tienchin;

import com.wangxy.tienchin.activity.domain.Activity;
import com.wangxy.tienchin.activity.domain.vo.ActivityVO;
import com.wangxy.tienchin.activity.service.IActivityService;
import com.wangxy.tienchin.activity.service.impl.ActivityServiceImpl;
import com.wangxy.tienchin.activity.validator.CreateGruop;
import com.wangxy.tienchin.activity.validator.EditGroup;
import com.wangxy.tienchin.channel.domain.Channel;
import com.wangxy.tienchin.channel.domain.vo.ChannelVO;
import com.wangxy.tienchin.channel.service.IChannelService;
import com.wangxy.tienchin.common.annotation.Log;
import com.wangxy.tienchin.common.core.controller.BaseController;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.core.page.TableDataInfo;
import com.wangxy.tienchin.common.enums.BusinessType;
import org.aspectj.weaver.loadtime.Aj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

import static com.wangxy.tienchin.common.utils.PageUtils.startPage;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangxy
 * @since 2023-10-03
 */
@RestController
@RequestMapping("/tienchin/activity")
public class ActivityController extends BaseController {
    public static final Logger logger=LoggerFactory.getLogger(ActivityController.class);
    @Autowired
    IActivityService activityService;
    @Autowired
    IChannelService channelService;
    @PreAuthorize("hasPermission('tienchin:activity:create')")
    @GetMapping("/channel/list")
    public AjaxResult channelList(){
        return AjaxResult.success(channelService.selectChannelList(new ChannelVO()));
    }
    @PreAuthorize("hasPermission('tienchin:activity:list')")//菜单管理，按钮级别权限
    @GetMapping("/list")
    public TableDataInfo list(){
        startPage();//继承自BaseController，自动从 前端的请求 里提取出来相关信息，其实不需要自己再传。来自github的分页插件
        List<ActivityVO> list=activityService.selectActivityList();
        return getDataTable(list);//也是分页插件的用法。
        //总之就是用分页工具把查询出来的结果用进行分页处理后返回（但是否会引发性能问题？因为要全查出来？）
    }
    @PreAuthorize("hasPermission('tienchin:activity:edit')")
    @GetMapping("/{activityId}")
    public AjaxResult getInfo(@PathVariable Long activityId){//根据活动Id查询详细（最新）信息

        Activity activity = activityService.getById(activityId);
        // 等待删除这两行（因为JsonFormat的问题临时解决一下）
        activity.setCreateTime(null);
        activity.setUpdateTime(null);
        logger.info("activity info ==> "+activity);
        return AjaxResult.success(activity);//##note getById是IService提供的
    }
    @PreAuthorize("hasPermission('tienchin:activity:add')")
    @Log(title = "添加活动", businessType = BusinessType.INSERT)
    @PostMapping//这个AjaxResult字段是专门定义的一个用于返回给前端的类。不再采用User、List<User>这种方式返回
    public AjaxResult add(@Validated(CreateGruop.class) @RequestBody ActivityVO activityVO) { //@Validated 用于校验domain模型中上某些字段的注解，如@NotBlank、@Size等
        logger.info("====activity:add");
        return activityService.addActivity(activityVO);
    }
    @PreAuthorize("hasPermission('tienchin:activity:edit')")
    @Log(title = "活动管理",businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated(EditGroup.class) @RequestBody ActivityVO activityVO){
        logger.info("====activity:edit");
        return activityService.updateActivity(activityVO);
    }

}
