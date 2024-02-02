package com.wangxy.tienchin.web.controller.tienchin.clue;

import com.wangxy.tienchin.activity.domain.vo.ActivityVO;
import com.wangxy.tienchin.activity.service.IActivityService;
import com.wangxy.tienchin.channel.domain.vo.ChannelVO;
import com.wangxy.tienchin.channel.service.IChannelService;
import com.wangxy.tienchin.clue.domain.Clue;
import com.wangxy.tienchin.clue.domain.vo.ClueDetail;
import com.wangxy.tienchin.clue.domain.vo.ClueSummaryVO;
import com.wangxy.tienchin.clue.domain.vo.ClueVO;
import com.wangxy.tienchin.clue.service.IClueService;
import com.wangxy.tienchin.clue.validator.EditGroup;
import com.wangxy.tienchin.common.annotation.Log;
import com.wangxy.tienchin.common.core.controller.BaseController;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.core.page.TableDataInfo;
import com.wangxy.tienchin.common.enums.BusinessType;
import com.wangxy.tienchin.system.service.ISysUserService;
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
 * @since 2023-10-16
 */
@RestController
@RequestMapping("/tienchin/clue")
public class ClueController extends BaseController {
    @Autowired
    IClueService clueService;
    @Autowired
    IChannelService channelService;
    @Autowired
    IActivityService activityService;
    @Autowired
    ISysUserService userService;
    @PreAuthorize("hasPermission('tienchin:clue:add')")
    @Log(title = "添加线索", businessType = BusinessType.INSERT)
    @PostMapping//这个AjaxResult字段是专门定义的一个用于返回给前端的类。不再采用User、List<User>这种方式返回
    public AjaxResult add(@Validated @RequestBody Clue clue) { //@Validated 用于校验domain模型中上某些字段的注解，如@NotBlank、@Size等
        return clueService.addClue(clue);
    }
    @PreAuthorize("hasPermission('tienchin:clue:create')")
    @GetMapping("/channel/list")
    public AjaxResult channelList(){
        return AjaxResult.success(channelService.selectChannelList(new ChannelVO()));
    }

    /*根据前端返回的渠道有选择性的返回对应的活动，而不是全部活动都返回*/
    @PreAuthorize("hasPermission('tienchin:clue:create')")
    @GetMapping("/activity/list/{channelId}")
    public AjaxResult activityList(@PathVariable Integer channelId){
        ActivityVO activityVO=new ActivityVO();
        activityVO.setChannelId(channelId);
        List<ActivityVO> list=activityService.selectActivityList(activityVO);
        return AjaxResult.success(list);
    }
    @PreAuthorize("hasPermission('tienchin:clue:list')")//菜单管理，按钮级别权限
    @GetMapping("/list")
    public TableDataInfo list(ClueVO clueVO){//##note: 注意，此处不加@RequestBody。因为是get方法而不是put/post
        startPage();//继承自BaseController，自动从 前端的请求 里提取出来相关信息，其实不需要自己再传。来自github的分页插件
        List<ClueSummaryVO> list=clueService.selectClueList(clueVO);
        return getDataTable(list);//也是分页插件的用法。
        //总之就是用分页工具把查询出来的结果用进行分页处理后返回（但是否会引发性能问题？因为要全查出来？）
    }
    @PreAuthorize("hasPermission('tienchin:clue:assign')")
    @GetMapping("/users/{deptId}")
    public AjaxResult getUsersByDeptId(@PathVariable  Long deptId){
        return userService.getUsersByDeptId(deptId);
    }

    @PreAuthorize("hasAnyPermission('tienchin:clue:view','tienchin:clue:follow')")
    @Log(title = "跟进线索", businessType = BusinessType.INSERT)
    @GetMapping("/{clueId}")
    public AjaxResult getClueDetailByClueId(@PathVariable Integer clueId){
        return clueService.getClueDetailByClueId(clueId);
    }

    @PreAuthorize("hasAnyPermission('tienchin:clue:follow')")
    @PostMapping("/follow")
    public AjaxResult clueFollow(@RequestBody ClueDetail clueDetail){
        return clueService.clueFollow(clueDetail);
    }
    @PreAuthorize("hasAnyPermission('tienchin:clue:follow')")
    @Log(title = "线索转为商机", businessType = BusinessType.OTHER)
    @PostMapping("/tobusiness/{clueId}")
    public AjaxResult clue2Bussiness(@PathVariable Integer clueId){
        return clueService.clue2Bussiness(clueId);
    }

    @PreAuthorize("hasAnyPermission('tienchin:clue:follow')")
    @Log(title = "无效线索", businessType = BusinessType.INSERT)
    @PostMapping("/invalidate")
    public AjaxResult invalidateClueFollow(@RequestBody ClueDetail clueDetail){
        return clueService.invalidateClueFollow(clueDetail);
    }

    @PreAuthorize("hasAnyPermission('tienchin:clue:edit')")
    @GetMapping("/summary/{clueId}")
    public AjaxResult getClueSummaryByClueId(@PathVariable Integer clueId){
        return clueService.getClueSummaryByClueId(clueId);
    }
    @PreAuthorize("hasAnyPermission('tienchin:clue:edit')")
    @Log(title = "修改线索", businessType = BusinessType.OTHER)
    @PutMapping()
    public AjaxResult updateClue(@Validated(EditGroup.class) @RequestBody Clue clue){
        return clueService.updateClue(clue);
    }

    @PreAuthorize("hasAnyPermission('tienchin:clue:remove')")
    @Log(title = "删除线索", businessType = BusinessType.DELETE)
    @DeleteMapping("/{clueIds}")
    public AjaxResult deleteClue(@PathVariable Long[] clueIds ){
        return toAjax(clueService.deleteClueByIds(clueIds));
    }


}
