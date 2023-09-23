package com.wangxy.tienchin.channel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangxy.tienchin.channel.domain.Channel;
import com.wangxy.tienchin.channel.domain.vo.ChannelVO;
import com.wangxy.tienchin.channel.mapper.ChannelMapper;
import com.wangxy.tienchin.channel.service.IChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangxy
 * @since 2023-09-19
 */
@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, Channel> implements IChannelService {
    public static final Logger logger= LoggerFactory.getLogger(ChannelServiceImpl.class);
    @Autowired
    ChannelMapper channelMapper;

    //用MybatisPlus，不需要再自己写mapper了
    @Override
    public AjaxResult addChannel(ChannelVO channelVO) {
        QueryWrapper<Channel> qw = new QueryWrapper<>();
        qw.lambda().eq(Channel::getChannelName, channelVO.getChannelName()).eq(Channel::getDelFlag, 0);
        //即查询： 第一个eq:Channel的某个字段为channelVO的值。第二个eq:且没被逻辑删除
        Channel c=getOne(qw);//MybatisPlus中的接口,按照名字查询一个渠道
        if(c!=null){
            //说明存在同名的渠道，并且没有被逻辑删除
            return AjaxResult.error("存在同名渠道，添加失败");
        }
//        插入时应该使用Channel类，而不是ChannelVO
        Channel channel = new Channel();
        BeanUtils.copyProperties(channelVO,channel);//拷贝各种属性到channel
        channel.setCreateBy(SecurityUtils.getUsername());//封装好的Utils
        channel.setCreateTime(LocalDateTime.now());
        channel.setDelFlag(0);//不设置也可以，数据库中设置了默认值
        return save(channel)?AjaxResult.success("添加成功"):AjaxResult.error("添加失败");//save也是MyBatisPlus的接口
    }

    //使用传统的Mybatis（需要mapper）
    @Override
    public List<Channel> selectChannelList(){
        List<Channel> channels = channelMapper.selectChannelList();
        for (Channel channel : channels) {
            logger.info("===>: {}",channel);
        }
        return channels;
    }

}
