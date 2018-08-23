package dango.shiro;

import dango.model.UserModel;
import dango.service.UserService;
import dango.shiro.redis.RedisCache;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: DANGO
 * @date 2018/7/11 15:31
 * @Description:
 */
@Component
public class LogoutAdviceFilter extends LogoutFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisCache redisCache;

    private String redirectUrl="/";

    @Override
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        ServletContext context= ((HttpServletRequest)request).getSession().getServletContext();
        try {

            Subject subject=getSubject(request,response);
            //在这里执行退出系统前需要清空的数据
            String userName=subject.getPrincipal().toString();
            UserModel user=userService.findUserByName(userName);
            redisCache.evict(userName);
            redisCache.evict("user"+userName);
            redisCache.evict("permission"+user.getId());

            subject.logout();
            context.removeAttribute("error");
        }catch (SessionException e){
            e.printStackTrace();
        }
        issueRedirect(request,response,redirectUrl);
        return false;
    }

}
