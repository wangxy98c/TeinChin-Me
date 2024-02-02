package com.wangxy.tienchin.course.service;

import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.course.domin.Course;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangxy
 * @since 2023-10-15
 */
public interface ICourseService extends IService<Course> {

    List<Course> selectCourseList(Course course);

    AjaxResult addCourse(Course course);

    AjaxResult updateCourse(Course course);

    boolean deleteCourseByIds(Long[] courseIds);

    AjaxResult getCourseByType(Integer typeId);
}
