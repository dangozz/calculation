package dango.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * @author: DANGO
 * @date 2018/8/7 14:59
 * @Description:
 */
@Aspect
public class LogAop {

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Pointcut("execution(* dango.controller.TestController.* (..))")
    private void allMethod(){}

    @Before("allMethod()")
    public void before(JoinPoint call){
        String className=call.getTarget().getClass().getName();
        String methodName=call.getSignature().getName();
        Object[] args=call.getArgs();
        StringBuilder argString=new StringBuilder();
        for(Object arg:args){
            argString.append(arg.toString()+"__");
        }
        logger.info(className+"."+methodName+"开始执行---入参:"+argString);
    }

    @AfterReturning(value = "allMethod()",returning = "returnValue")
    public void after(JoinPoint call,Object returnValue){
        String className=call.getTarget().getClass().getName();
        String methodName=call.getSignature().getName();
        logger.info(className+"."+methodName+"执行完毕---返回参数:"+returnValue.toString());
    }


}
