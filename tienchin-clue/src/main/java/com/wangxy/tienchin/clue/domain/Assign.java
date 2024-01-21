package com.wangxy.tienchin.clue.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangxy
 * @since 2023-10-16
 */
@TableName("tienchin_assign")
@ApiModel(value = "Assign对象", description = "")
public class Assign implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "aid", type = IdType.AUTO)
    //@NotNull(message = "aID不能为空")
    private Integer aid;

    @ApiModelProperty("1线索2商机")
    private Integer type;

    @ApiModelProperty("线索或商机的Id")
    @NotNull(message = "assignId不能为空")
    private Integer assignId;

    @ApiModelProperty("分配给的用户")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @ApiModelProperty("分配给用户的用户名")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty("分配用户的所属部门id")
    @NotNull(message = "部门Id不能为空")
    private Long deptId;

    @ApiModelProperty("是否是当前的最新分配人")
    private Boolean latest;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;

    private String remark;

    private Integer delFlag;

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAssignId() {
        return assignId;
    }

    public void setAssignId(Integer assignId) {
        this.assignId = assignId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Boolean getLatest() {
        return latest;
    }

    public void setLatest(Boolean latest) {
        this.latest = latest;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "Assign{" +
            "aid = " + aid +
            ", type = " + type +
            ", assignId = " + assignId +
            ", userId = " + userId +
            ", userName = " + userName +
            ", deptId = " + deptId +
            ", latest = " + latest +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
            ", createBy = " + createBy +
            ", updateBy = " + updateBy +
            ", remark = " + remark +
            ", delFlag = " + delFlag +
        "}";
    }
}
