package com.wangxy.tienchin.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.utils.SecurityUtils;
import com.wangxy.tienchin.course.domin.Course;
import com.wangxy.tienchin.course.mapper.CourseMapper;
import com.wangxy.tienchin.course.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * @since 2023-10-15
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    CourseMapper courseMapper;
    @Override
    public List<Course> selectCourseList(Course course) {
        System.out.println("=====>Impl");
        //这里确实不能用QueryWrapper。会出问题（因为分页插件）。但其实Mybatis-Plus也有分页功能。
        return courseMapper.selectCourseList(course);
    }

    @Override
    public AjaxResult addCourse(Course course) {
        course.setCreateBy(SecurityUtils.getUsername());
        course.setCreateTime(LocalDateTime.now());
        return save(course)?AjaxResult.success("添加成功"):AjaxResult.error("添加失败");
    }

    @Override
    public AjaxResult updateCourse(Course course) {
        course.setUpdateBy(SecurityUtils.getUsername());
        course.setUpdateTime(LocalDateTime.now());
        course.setCreateBy(null);
        course.setCreateTime(null);
        course.setDelFlag(null);
        return updateById(course)?AjaxResult.success("更新成功"):AjaxResult.error("更新失败");
    }

    @Override
    public boolean deleteCourseByIds(Long[] courseIds) {
        UpdateWrapper<Course> uw = new UpdateWrapper<>();
        uw.lambda().set(Course::getDelFlag,1).in(Course::getCourseId,courseIds);//把delFlag设为1（where）所有channelIds
        return update(uw);
    }

    @Override
    public AjaxResult getCourseByType(Integer typeId) {
        List<Course> list = null;
        try {
            QueryWrapper<Course> qw = new QueryWrapper<>();
            qw.lambda().eq(Course::getType,typeId).ne(Course::getDelFlag,1);
            list = list(qw);
            return AjaxResult.success(list);
        } catch (Exception e) {
            return AjaxResult.error("查询失败"+e.getMessage());
        }

    }
}
