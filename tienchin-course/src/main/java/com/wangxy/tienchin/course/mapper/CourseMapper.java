package com.wangxy.tienchin.course.mapper;

import com.wangxy.tienchin.course.domin.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangxy
 * @since 2023-10-15
 */
public interface CourseMapper extends BaseMapper<Course> {

    List<Course> selectCourseList(Course course);
}
