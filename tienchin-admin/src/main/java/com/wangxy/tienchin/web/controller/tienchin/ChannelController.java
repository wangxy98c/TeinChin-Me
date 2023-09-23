package com.wangxy.tienchin.web.controller.tienchin;

import com.wangxy.tienchin.channel.domain.Channel;
import com.wangxy.tienchin.channel.domain.vo.ChannelVO;
import com.wangxy.tienchin.channel.service.IChannelService;
import com.wangxy.tienchin.common.annotation.Log;
import com.wangxy.tienchin.common.core.controller.BaseController;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.core.domain.entity.SysRole;
import com.wangxy.tienchin.common.core.page.TableDataInfo;
import com.wangxy.tienchin.common.enums.BusinessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public TableDataInfo list(){
        startPage();//继承自BaseController
        List<Channel> list=channelService.selectChannelList();
        logger.debug("list:{}",list);
        return getDataTable(list);
    }
    @PreAuthorize("hasPermission('tienchin:channel:add')")
    @Log(title = "添加渠道", businessType = BusinessType.INSERT)
    @PostMapping//这个AjaxResult字段是专门定义的一个用于返回给前端的类。不再采用User、List<User>这种方式返回
    public AjaxResult add(@Validated @RequestBody ChannelVO channelVO) { //@Validated 用于校验domain模型中上某些字段的注解，如@NotBlank、@Size等
        return channelService.addChannel(channelVO);
    }

}
