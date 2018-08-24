package dango.aop.test;

import java.lang.annotation.*;

/**
 * @author: DANGO
 * @date 2018/8/24 15:35
 * @Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MyComponent {
    String desc() default "自定义注解测试";
}
