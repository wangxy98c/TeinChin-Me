package com.wangxy.tienchin.clue.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.wangxy.tienchin.clue.validator.CreateGroup;
import com.wangxy.tienchin.clue.validator.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
@TableName("tienchin_clue")
@ApiModel(value = "Clue对象", description = "")
public class Clue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "clue_id", type = IdType.AUTO)
    @NotNull(message = "线索ID不能为空")
    @NotNull(message = "线索ID不能为空",groups = EditGroup.class)
    private Integer clueId;

    @ApiModelProperty("客户姓名")
    @NotBlank(message = "{clue.name.notblank}")
    @NotBlank(message = "{clue.name.notblank}",groups = EditGroup.class)
    private String name;

    @ApiModelProperty("从哪个渠道来的")
    private Integer channelId;

    @ApiModelProperty("从哪个活动来的")
    private Integer activityId;

    @ApiModelProperty("性别：0男1女2未知")
    private Integer gender;

    @ApiModelProperty("客户年龄")
    private Integer age;

    @ApiModelProperty("客户微信")
    private String weixin;

    @ApiModelProperty("客户QQ")
    private String qq;

    @ApiModelProperty("客户电话")
    @NotBlank(message = "{clue.phone.notblank}")
    private String phone;

    @ApiModelProperty("客户意向等级")
    private Integer level;

    @ApiModelProperty("意向课程")
    private String subject;

    @ApiModelProperty("线索状态：是否已分配1已分配2跟进中3回收4伪线索")
    private Integer status;

    @ApiModelProperty("伪线索失败次数，最大3次")
    private Integer failCount;

    @ApiModelProperty("下次跟进时间")
    private LocalDateTime nextTime;

    @ApiModelProperty("线索失效时间：一定时限内所属销售业绩")
    private LocalDateTime endTime;

    @ApiModelProperty("线索是否需要转派")
    private Boolean transfer;

    @ApiModelProperty("备注")
    private String remark;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private Integer delFlag;

    public Integer getClueId() {
        return clueId;
    }

    public void setClueId(Integer clueId) {
        this.clueId = clueId;
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

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public LocalDateTime getNextTime() {
        return nextTime;
    }

    public void setNextTime(LocalDateTime nextTime) {
        this.nextTime = nextTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getTransfer() {
        return transfer;
    }

    public void setTransfer(Boolean transfer) {
        this.transfer = transfer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String  createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "Clue{" +
            "clueId = " + clueId +
            ", name = " + name +
            ", channelId = " + channelId +
            ", activityId = " + activityId +
            ", gender = " + gender +
            ", age = " + age +
            ", weixin = " + weixin +
            ", qq = " + qq +
            ", phone = " + phone +
            ", level = " + level +
            ", subject = " + subject +
            ", status = " + status +
            ", failCount = " + failCount +
            ", nextTime = " + nextTime +
            ", endTime = " + endTime +
            ", transfer = " + transfer +
            ", remark = " + remark +
            ", createTime = " + createTime +
            ", createBy = " + createBy +
            ", updateTime = " + updateTime +
            ", updateBy = " + updateBy +
            ", delFlag = " + delFlag +
        "}";
    }
}
