package com.wangxy.tienchin.channel.mapper;

import com.wangxy.tienchin.channel.domain.Channel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangxy.tienchin.channel.domain.vo.ChannelVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangxy
 * @since 2023-09-19
 */
@Mapper
public interface ChannelMapper extends BaseMapper<Channel> {
    List<Channel> selectChannelList(ChannelVO channelVO);
}
