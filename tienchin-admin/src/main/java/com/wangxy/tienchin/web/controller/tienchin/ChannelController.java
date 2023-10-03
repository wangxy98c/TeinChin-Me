package com.wangxy.tienchin.web.controller.tienchin;

import com.wangxy.tienchin.channel.domain.Channel;
import com.wangxy.tienchin.channel.domain.vo.ChannelVO;
import com.wangxy.tienchin.channel.service.IChannelService;
import com.wangxy.tienchin.common.annotation.Log;
import com.wangxy.tienchin.common.core.controller.BaseController;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.core.domain.entity.SysRole;
import com.wangxy.tienchin.common.core.domain.model.LoginUser;
import com.wangxy.tienchin.common.core.page.TableDataInfo;
import com.wangxy.tienchin.common.enums.BusinessType;
import com.wangxy.tienchin.common.utils.StringUtils;
import com.wangxy.tienchin.common.utils.poi.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Controller继承自BaseController。其中封装了一些基础的方法。比如
 * 1.分页（来自github的分页插件，pageHelper？）
 * 2.
 */
@RestController
@RequestMapping("/tienchin/channel")
public class ChannelController extends BaseController {
    @Autowired
    IChannelService channelService;
    public static final Logger logger= LoggerFactory.getLogger(ChannelController.class);
    @PreAuthorize("hasPermission('tienchin:channel:list')")//菜单管理，按钮级别权限
    @GetMapping("/list")
    public TableDataInfo list(ChannelVO channelVO){
        startPage();//继承自BaseController，自动从 前端的请求 里提取出来相关信息，其实不需要自己再传。来自github的分页插件
        List<Channel> list=channelService.selectChannelList(channelVO);
        logger.debug("list:{}",list);
        return getDataTable(list);//也是分页插件的用法。
        //总之就是用分页工具把查询出来的结果用进行分页处理后返回（但是否会引发性能问题？因为要全查出来？）
    }
    @PreAuthorize("hasPermission('tienchin:channel:add')")
    @Log(title = "添加渠道", businessType = BusinessType.INSERT)
    @PostMapping//这个AjaxResult字段是专门定义的一个用于返回给前端的类。不再采用User、List<User>这种方式返回
    public AjaxResult add(@Validated @RequestBody ChannelVO channelVO) { //@Validated 用于校验domain模型中上某些字段的注解，如@NotBlank、@Size等
        return channelService.addChannel(channelVO);
    }

    @PreAuthorize("hasPermission('tienchin:channel:edit')")
    @Log(title = "渠道管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ChannelVO channelVO) {
        return channelService.updateChannel(channelVO);
    }

    @PreAuthorize("hasPermission('tienchin:channel:list')")
    @GetMapping("/{channelId}")
    public AjaxResult getInfo(@PathVariable Long channelId){//根据渠道Id查询
        return AjaxResult.success(channelService.getById(channelId));//##note getById是IService提供的
    }

    @PreAuthorize("hasPermission('tienchin:channel:remove')")
    @Log(title = "渠道管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{channelIds}")
    public AjaxResult remove(@PathVariable Long[] channelIds) {
        return toAjax(channelService.deleteChannelByIds(channelIds));
    }

    @Log(title = "渠道导出", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasPermission('tienchin:channel:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChannelVO channelVO) {
        List<Channel> list = channelService.selectChannelList(channelVO);
        ExcelUtil<Channel> util = new ExcelUtil<Channel>(Channel.class);
        util.exportExcel(response, list, "渠道数据");//封装好（EasyUI那套）的导出工具
    }
}
