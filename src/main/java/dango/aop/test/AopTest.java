package dango.aop.test;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author: DANGO
 * @date 2018/8/24 15:38
 * @Description:
 */
@Aspect
@Component
public class AopTest {

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Before("within(dango..*) && @annotation(m1)")
    public void myBefore(JoinPoint jp,MyComponent m1){
        logger.info("MyComponent_MyComponent_MyComponent");
        logger.info(m1.desc());
        logger.info("**********************************************");
    }

}
