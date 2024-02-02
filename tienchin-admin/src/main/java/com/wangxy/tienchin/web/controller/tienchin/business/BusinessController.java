package com.wangxy.tienchin.web.controller.tienchin.business;

import com.wangxy.tienchin.activity.domain.vo.ActivityVO;
import com.wangxy.tienchin.activity.service.IActivityService;
import com.wangxy.tienchin.business.domain.Business;
import com.wangxy.tienchin.business.domain.BusinessFollow;
import com.wangxy.tienchin.business.domain.BusinessSummaryVO;
import com.wangxy.tienchin.business.service.IBusinessService;
import com.wangxy.tienchin.channel.domain.vo.ChannelVO;
import com.wangxy.tienchin.channel.service.IChannelService;
import com.wangxy.tienchin.clue.validator.EditGroup;
import com.wangxy.tienchin.common.annotation.Log;
import com.wangxy.tienchin.common.core.controller.BaseController;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.core.page.TableDataInfo;
import com.wangxy.tienchin.common.enums.BusinessType;
import com.wangxy.tienchin.course.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangxy
 * @since 2024-01-22
 */
@RestController
@RequestMapping("/tienchin/business")
public class BusinessController extends BaseController {
    @Autowired
    IBusinessService businessService;
    @Autowired
    IActivityService activityService;
    @Autowired
    IChannelService channelService;
    @Autowired
    ICourseService courseService;
    @PreAuthorize("hasAnyPermission('tienchin:business:list')")
    @GetMapping("/list")
    public TableDataInfo businessList(BusinessSummaryVO businessSummaryVO){
        startPage();
        List<BusinessSummaryVO> list=businessService.selectBusinessList(businessSummaryVO);
        return getDataTable(list);
    }
    @PreAuthorize("hasPermission('tienchin:business:add')")
    @Log(title = "添加商机", businessType = BusinessType.INSERT)
    @PostMapping//这个AjaxResult字段是专门定义的一个用于返回给前端的类。不再采用User、List<User>这种方式返回
    public AjaxResult add(@Validated @RequestBody Business business) { //@Validated 用于校验domain模型中上某些字段的注解，如@NotBlank、@Size等
        return businessService.addBusiness(business);
    }


    @PreAuthorize("hasPermission('tienchin:business:create')")
    @GetMapping("/channel/list")
    public AjaxResult channelList(){
        return AjaxResult.success(channelService.selectChannelList(new ChannelVO()));
    }

    /*根据前端返回的渠道有选择性的返回对应的活动，而不是全部活动都返回*/
    @PreAuthorize("hasPermission('tienchin:business:create')")
    @GetMapping("/activity/list/{channelId}")
    public AjaxResult activityList(@PathVariable Integer channelId){
        ActivityVO activityVO=new ActivityVO();
        activityVO.setChannelId(channelId);
        List<ActivityVO> list=activityService.selectActivityList(activityVO);
        return AjaxResult.success(list);
    }
    @PreAuthorize("hasAnyPermission('tienchin:business:follow')")
    @PostMapping("/follow")
    public AjaxResult businessFollow(@RequestBody Business business){
        return null;
    }
    @PreAuthorize("hasAnyPermission('tienchin:business:follow')")
    @GetMapping("/course/{typeId}")
    public AjaxResult getCourseByType(@PathVariable Integer typeId){
        return courseService.getCourseByType(typeId);
    }
    @PreAuthorize("hasAnyPermission('tienchin:business:view','tienchin:business:follow','tienchin:business:edit')")
    @GetMapping("/{businessId}")
    public AjaxResult getBusinessById(@PathVariable Integer businessId){
        return businessService.getBusinessById(businessId);
    }
    @PreAuthorize("hasAnyPermission('tienchin:business:follow')")
    @PostMapping("/save")
    public AjaxResult follow(@RequestBody BusinessFollow businessFollow){
        return businessService.follow(businessFollow);
    }
    @PreAuthorize("hasAnyPermission('tienchin:business:edit')")
    @Log(title = "修改商机", businessType = BusinessType.OTHER)
    @PutMapping()
    public AjaxResult updateClue(@Validated(EditGroup.class) @RequestBody Business business){
        return businessService.updateBusiness(business);
    }
    @PreAuthorize("hasAnyPermission('tienchin:business:remove')")
    @Log(title = "删除商机", businessType = BusinessType.DELETE)
    @DeleteMapping("/{businessIds}")
    public AjaxResult deleteBusiness(@PathVariable Long[] businessIds ){
        return toAjax(businessService.deleteBusinessByIds(businessIds));
    }

}
