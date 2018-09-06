package dango.aop.test;

import java.lang.annotation.*;

/**
 * @author: DANGO
 * @date 2018/9/5 9:39
 * @Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisComponent {
    String redisKey();
}
