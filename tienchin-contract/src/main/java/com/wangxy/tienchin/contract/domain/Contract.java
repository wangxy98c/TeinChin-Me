package com.wangxy.tienchin.contract.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangxy
 * @since 2024-02-03
 */
@TableName("tienchin_contract")
@ApiModel(value = "Contract对象", description = "")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer approveDeptId;
    private String approveDeptName;
    private Integer approveUserId;
    private String approveUserName;

    public Integer getApproveDeptId() {
        return approveDeptId;
    }

    public void setApproveDeptId(Integer approveDeptId) {
        this.approveDeptId = approveDeptId;
    }

    public String getApproveDeptName() {
        return approveDeptName;
    }

    public void setApproveDeptName(String approveDeptName) {
        this.approveDeptName = approveDeptName;
    }

    public Integer getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(Integer approveUserId) {
        this.approveUserId = approveUserId;
    }

    public String getApproveUserName() {
        return approveUserName;
    }

    public void setApproveUserName(String approveUserName) {
        this.approveUserName = approveUserName;
    }

    @ApiModelProperty("合同编号")
    @TableId(value = "contract_id", type = IdType.AUTO)
    private Integer contractId;

    @ApiModelProperty("客户手机号码")
    private String phone;

    @ApiModelProperty("客户姓名")
    private String name;

    @ApiModelProperty("课程分类")
    private Integer type;

    @ApiModelProperty("参加的活动id")
    private Integer activityId;

    @ApiModelProperty("参加的活动名称")
    private String activityName;

    @ApiModelProperty("课程id")
    private Integer courseId;

    @ApiModelProperty("课程名")
    private String courseName;

    @ApiModelProperty("渠道id")
    private Integer channelId;

    @ApiModelProperty("渠道名称")
    private String channelName;

    @ApiModelProperty("合同状态1待审核2通过3驳回")
    private Integer status;

    @ApiModelProperty("合同文件存档地址")
    private String filePath;

    @ApiModelProperty("合同额/实际成交价")
    private Double contractPrice;

    @ApiModelProperty("课程原本价格")
    private Double coursePrice;

    @ApiModelProperty("折扣类型")
    private Integer discountType;

    @ApiModelProperty("followable实例流程id")
    private String processInstanceId;

    @ApiModelProperty("对应的商机id")
    private Integer businessId;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private String remark;

    private Integer delFlag;

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Double getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(Double contractPrice) {
        this.contractPrice = contractPrice;
    }

    public Double getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(Double coursePrice) {
        this.coursePrice = coursePrice;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
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
        return "Contract{" +
                "approveDeptId=" + approveDeptId +
                ", approveDeptName='" + approveDeptName + '\'' +
                ", approveUserId=" + approveUserId +
                ", approveUserName='" + approveUserName + '\'' +
                ", contractId=" + contractId +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", channelId=" + channelId +
                ", channelName='" + channelName + '\'' +
                ", status=" + status +
                ", filePath='" + filePath + '\'' +
                ", contractPrice=" + contractPrice +
                ", coursePrice=" + coursePrice +
                ", discountType=" + discountType +
                ", processInstanceId=" + processInstanceId +
                ", businessId=" + businessId +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", remark='" + remark + '\'' +
                ", delFlag=" + delFlag +
                '}';
    }
}
