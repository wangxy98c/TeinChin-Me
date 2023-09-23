package com.wangxy.tienchin.channel.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wangxy.tienchin.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/*删掉了createby、updateby、createtime、createtime字段，因为它继承自BaseEntity，而BaseEntity中有这些字段，故不需要
为什么不用Channel而新创建一个类呢？因为Channel类是为了MybatisPlus。如果多出若干字段，会导致出错。
这个类到底干嘛：1.用于@Validated 2.不给前端返回不必要的字段
 */
public class ChannelVO extends BaseEntity {
    private Integer channelId;
    @Range(min = 0,max=1,message = "渠道状态只能是0或1,代表禁用和正常")
    private Byte status;//Byte类型不能使用@NotBlank
    @NotBlank(message = "渠道名不能为空")//@NotBlank是给字符串类型的注解
    private String channelName;
    private String remark;
    @Range(min = 1,max=2,message = "渠道类型只能是1或2,代表线上和线下")
    private Integer type;
    private Integer delFlag;

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }


    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
