package com.wangxy.tienchin.web.controller.tienchin;

import com.wangxy.tienchin.common.annotation.Log;
import com.wangxy.tienchin.common.core.controller.BaseController;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.core.page.TableDataInfo;
import com.wangxy.tienchin.common.enums.BusinessType;
import com.wangxy.tienchin.common.utils.poi.ExcelUtil;
import com.wangxy.tienchin.course.domin.Course;
import com.wangxy.tienchin.course.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangxy
 * @since 2023-10-15
 */
@RestController
@RequestMapping("/tienchin/course")
public class CourseController extends BaseController {
    @Autowired
    ICourseService courseService;
    @PreAuthorize("hasPermission('tienchin:course:list')")//菜单管理，按钮级别权限
    @GetMapping("/list")
    public TableDataInfo list(Course course){
        System.out.println("=====>controller");
        System.out.println(course.toString());
        startPage();//继承自BaseController，自动从 前端的请求 里提取出来相关信息，其实不需要自己再传。来自github的分页插件
        List<Course> list=courseService.selectCourseList(course);
        return getDataTable(list);
    }
    @PreAuthorize("hasPermission('tienchin:course:add')")
    @Log(title = "添加课程", businessType = BusinessType.INSERT)
    @PostMapping//这个AjaxResult字段是专门定义的一个用于返回给前端的类。不再采用User、List<User>这种方式返回
    public AjaxResult add(@Validated @RequestBody Course course) { //@Validated 用于校验domain模型中上某些字段的注解，如@NotBlank、@Size等
        return courseService.addCourse(course);
    }
    @PreAuthorize("hasPermission('tienchin:course:edit')")
    @Log(title = "课程管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Course course) {
        return courseService.updateCourse(course);
    }
    @PreAuthorize("hasPermission('tienchin:course:edit')")
    @GetMapping("/{courseId}")
    public AjaxResult getInfo(@PathVariable Long courseId){//根据渠道Id查询
        return AjaxResult.success(courseService.getById(courseId));//##note getById是IService提供的
    }
    @PreAuthorize("hasPermission('tienchin:course:remove')")
    @Log(title = "课程管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{courseIds}")
    public AjaxResult remove(@PathVariable Long[] courseIds) {
        return toAjax(courseService.deleteCourseByIds(courseIds));
    }
    @Log(title = "课程导出", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasPermission('tienchin:course:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, Course course) {
        List<Course> list = courseService.selectCourseList(course);
        ExcelUtil<Course> util = new ExcelUtil<Course>(Course.class);
        util.exportExcel(response, list, "渠道数据");//封装好（EasyUI那套）的导出工具
    }
}
