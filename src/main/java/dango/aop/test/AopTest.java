package dango.aop.test;


import dango.shiro.redis.RedisCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: DANGO
 * @date 2018/8/24 15:38
 * @Description:
 */
@Aspect
@Component
public class AopTest {

    @Autowired
    private RedisCache redisCache;

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Before("within(dango..*) && @annotation(m1)")
    public void myBefore(JoinPoint jp,MyComponent m1){
        logger.info("MyComponent_MyComponent_MyComponent");
        logger.info(m1.desc());
        logger.info("**********************************************");
    }

    @Around("within(dango..*) && @annotation(redisComponent)")
    public Object Around(ProceedingJoinPoint joinPoint,RedisComponent redisComponent) throws Throwable{
        logger.info("MyAround begin *****************************************************************");
        String key=redisComponent.redisKey();
        Object object=redisCache.get2(key);
        if(object!=null){
            logger.info("MyAround 从redis中获取 "+key+" 获取到 "+object);
            return object;
        }else {
            logger.info("MyAround redisKey:"+key+" 无缓存");
            logger.info("方法执行前");
            object=joinPoint.proceed();
            logger.info("方法执行完毕 返回值"+object);
            redisCache.put(key,object);
            logger.info("MyAround end *****************************************************************");
            return object;
        }
    }

}
