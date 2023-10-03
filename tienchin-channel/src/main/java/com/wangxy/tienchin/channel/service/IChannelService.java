package com.wangxy.tienchin.channel.service;

import com.wangxy.tienchin.channel.domain.Channel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangxy.tienchin.channel.domain.vo.ChannelVO;
import com.wangxy.tienchin.common.core.domain.AjaxResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangxy
 * @since 2023-09-19
 */
public interface IChannelService extends IService<Channel> {

    List<Channel> selectChannelList(ChannelVO channelVO);

    AjaxResult addChannel(ChannelVO channelVO);

    AjaxResult updateChannel(ChannelVO channelVO);

    boolean deleteChannelByIds(Long[] channelIds);
}
