package com.wangxy.tienchin.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangxy
 * @since 2024-01-22
 */
@TableName("tienchin_business")
@ApiModel(value = "Business对象", description = "")
public class Business implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "business_id", type = IdType.AUTO)
    private Integer businessId;

    @ApiModelProperty("客户姓名")
    @NotBlank(message = "name不能为空")
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

    private String education;
    private Double money;

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @ApiModelProperty("客户QQ")
    private String qq;

    @ApiModelProperty("客户电话")
    @NotBlank(message = "客户电话不得为空")
    private String phone;

    @ApiModelProperty("客户意向等级")
    private Integer level;

    @ApiModelProperty("意向课程")
    private String subject;

    @ApiModelProperty("线索状态：是否已分配1已分配2跟进中3回收4伪线索")
    private Integer status;

    @ApiModelProperty("伪线索失败次数，最大3次")
    private Integer failCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @ApiModelProperty("下次跟进时间")
    private LocalDateTime nextTime;

    @ApiModelProperty("线索失效时间：一定时限内所属销售业绩")
    private LocalDateTime endTime;

    @ApiModelProperty("线索是否需要转派")
    private Boolean transfer;

    @ApiModelProperty("备注")
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    @ApiModelProperty("城市/市")
    private String city;

    @ApiModelProperty("区域/区")
    private String area;

    private String updateBy;

    private Integer delFlag;

    @ApiModelProperty("省份/省")
    private String province;

    @ApiModelProperty("课程id")
    private Integer courseId;

    @ApiModelProperty("职业")
    private String occupation;



    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @ApiModelProperty("身高")
    private Double height;

    @ApiModelProperty("体重")
    private Double weight;

    @ApiModelProperty("锻炼原因")
    private String reason;

    @ApiModelProperty("每周预计锻炼时间")
    private Integer hours;

    @ApiModelProperty("其他意向")
    private String otherIntention;

    @ApiModelProperty("线索ID")
    private Integer clueId;

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Double getHeight() {
        return height;
    }

    public Double getWeight() {
        return weight;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public String getOtherIntention() {
        return otherIntention;
    }

    public void setOtherIntention(String otherIntention) {
        this.otherIntention = otherIntention;
    }

    public Integer getClueId() {
        return clueId;
    }

    public void setClueId(Integer clueId) {
        this.clueId = clueId;
    }

    @Override
    public String toString() {
        return "Business{" +
            "businessId = " + businessId +
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
            ", city = " + city +
            ", area = " + area +
            ", updateBy = " + updateBy +
            ", delFlag = " + delFlag +
            ", province = " + province +
            ", courseId = " + courseId +
            ", occupation = " + occupation +
            ", height = " + height +
            ", weight = " + weight +
            ", reason = " + reason +
            ", hours = " + hours +
            ", otherIntention = " + otherIntention +
            ", clueId = " + clueId +
        "}";
    }
}
