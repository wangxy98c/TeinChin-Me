package com.wangxy.tienchin.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wangxy.tienchin.common.enums.DataSourceType;

/**
 * 自定义多数据源切换注解
 * <p>
 * 优先级：先方法，后类，如果方法覆盖了类上的数据源类型，以方法的为准，否则以类上的为准
 * <p>
 * 注解加在Service层或类上，表示方法或者类中的所有方法都是用某一个数据源。
 * 如果某个方法上有这个注解，将该方法需要使用的数据源名称存入ThreadLocal。将来使用Mybatis获取数据源名称时从ThreadLocal中拿到
 * 自定义切面：在切面中解析@DataSource注解。将标记的数据源存入ThreadLocal
 * Mapper执行时，需要DataSource。自动去AbstractRoutingDataSource类中查找需要的数据源，我们只需要从中返回ThreadLocal即可
 *
 * @author tienchin
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource//
{
    /**
     * 切换数据源名称
     */
    public DataSourceType value() default DataSourceType.MASTER;
}
