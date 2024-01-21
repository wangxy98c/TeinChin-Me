package com.wangxy.tienchin.clue.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wangxy.tienchin.common.core.domain.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class ClueVO extends BaseEntity {//##note BaseEntity主要用语分页的实现
    private String name;
    private String phone;
    private Integer channelId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date endDate;
    private Integer Status;
    private String owner;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }



    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "ClueVO{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", channelId=" + channelId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", Status=" + Status +
                ", owner='" + owner + '\'' +
                '}';
    }
}
