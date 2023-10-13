package com.wangxy.tienchin.activity.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wangxy.tienchin.activity.validator.EditGroup;
import com.wangxy.tienchin.common.core.domain.BaseEntity;

import javax.validation.constraints.*;
import java.util.Date;

public class ActivityVO extends BaseEntity {
    @NotNull(message = "活动Id不能为空",groups = EditGroup.class)//添加操作不加注解，而修改操作必须有注解。矛盾了，使用分组校验group来解决
    private Integer activityId;
    @NotBlank(message = "{activity.name.notblank}")
    @Size(min=0,max=20,message = "${activity.name.size}")
    private String name;
    @NotNull(message = "{activity.channelid.notnull}")
    private Integer channelId;
    private String channelName;
    @Size(min=0,max=255,message = "{activity.info.size}")
    private String info;
    @NotNull(message = "活动类型不能为空")
    private Integer type;
    @Max(value = 10,message = "折扣不能超过10折")
    @Min(value = 0,message = "折扣最低为0折,即不打折")
    private Double discount;
    @Min(value= 0,message = "代金券最小为0")
    private Double voucher;
    @Max(value = 1,message = "状态1代表活动中，0代表已过期")
    @Min(value = 0,message = "状态1代表活动中，0代表已过期")
    private Integer status;
    private  Integer delFlag;
    @NotNull(message = "活动开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date beginTime;
    @NotNull(message = "活动结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date endTime;
    public Date getBeginTime() {
        return beginTime;
    }
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getVoucher() {
        return voucher;
    }

    public void setVoucher(Double voucher) {
        this.voucher = voucher;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
