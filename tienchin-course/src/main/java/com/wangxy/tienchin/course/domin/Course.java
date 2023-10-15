package com.wangxy.tienchin.course.domin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.wangxy.tienchin.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangxy
 * @since 2023-10-15
 */
@TableName("tienchin_course")
@ApiModel(value = "Course对象", description = "")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "course_id", type = IdType.AUTO)
    @Excel(name = "课程编号",cellType = Excel.ColumnType.NUMERIC)
    private Integer courseId;

    @ApiModelProperty("课程类型：1舞蹈2游泳")
    @NotNull(message = "{course.type.notnull}")
    @Excel(name = "课程类型")
    private Integer type;

    @ApiModelProperty("课程名")
    @NotBlank(message = "{course.name.notblank}")
    @Excel(name = "课程名")
    private String name;

    @ApiModelProperty("课程价格")
    @NotNull(message = "{course.price.notnull}")
    @Min(value = 0,message = "{course.price.min}")
    @Excel(name = "课程价格",cellType = Excel.ColumnType.NUMERIC)
    private Double price;

    @ApiModelProperty("课程适用人群")
    @NotNull(message = "{course.applyto.notnull}")
    @Excel(name = "适用人群")
    private Integer applyTo;

    @ApiModelProperty("简介")
    @Excel(name = "课程简介")
    private String info;

    @ApiModelProperty("备注信息")
    private String remark;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private Integer delFlag;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(Integer applyTo) {
        this.applyTo = applyTo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    public void setCreateBy(String createBy) {
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
        return "Course{" +
            "courseId = " + courseId +
            ", type = " + type +
            ", name = " + name +
            ", price = " + price +
            ", applyTo = " + applyTo +
            ", info = " + info +
            ", remark = " + remark +
            ", createTime = " + createTime +
            ", createBy = " + createBy +
            ", updateTime = " + updateTime +
            ", updateBy = " + updateBy +
            ", delFlag = " + delFlag +
        "}";
    }
}
