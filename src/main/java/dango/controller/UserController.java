package dango.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: DANGO
 * @date 2018/7/4 10:59
 * @Description:
 */
@Controller
@RequestMapping("/")
public class UserController {

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    @RequestMapping("login.do")
    public String Login(HttpServletRequest request){
        String errorException=(String)request.getAttribute("shiroLoginFailure");
        logger.info("Exception__"+errorException);
        String errorString=null;
        if(UnknownAccountException.class.getName().equals(errorException)) {
            errorString = "用户名/密码错误";
        } else if(IncorrectCredentialsException.class.getName().equals(errorException)) {
            errorString = "用户名/密码错误";
        } else if(ExcessiveAttemptsException.class.getName().equals(errorException)){
            errorString = "登录失败多次，账户锁定,请五分钟后重试";
        }else if("jCaptcha.error".equals(errorException)){
            errorString = "验证码错误，请重新输入";
        }
        else if(errorException != null) {
            errorString = "其他错误：" + errorException;
        }
        if(errorString!=null)
            request.getSession().setAttribute("err",errorString);
//        System.out.println("tttttttttttttttttttttttttttttttttttttttttt"+errorException);
        return "login";
    }
}
