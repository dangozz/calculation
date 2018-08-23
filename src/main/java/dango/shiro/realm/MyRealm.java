package dango.shiro.realm;

import dango.model.PermissionModel;
import dango.model.RoleModel;
import dango.model.UserModel;
import dango.service.PermissionService;
import dango.service.RoleService;
import dango.service.UserService;
import dango.shiro.redis.RedisCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: DANGO
 * @date 2018/7/4 10:24
 * @Description:
 */
@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RedisCache redisCache;

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String)principalCollection.getPrimaryPrincipal();
        UserModel user=null;
        if(redisCache.get("user"+username)!=null){
            user=(UserModel)redisCache.get("user"+username);
        }else {
            user = userService.findUserByName(username);
            redisCache.put("user"+username,user);
        }
        if(user!=null) {
            if(redisCache.get("permission"+user.getId())!=null && !"".equals(SecurityUtils.getSubject().getSession().getAttribute("permission"+user.getId()))) {
                return (AuthorizationInfo) redisCache.get("permission" + user.getId()).get();
            }else {
                System.out.println(user);
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                List<RoleModel> roleModels = roleService.findUserRoles(user);
                for (int i = 0; i < roleModels.size(); i++) {
                    info.addRole(roleModels.get(i).getRoleName());
                }
                List<PermissionModel> permissionModels = permissionService.findUserPermissions(user);
                for (int i = 0; i < permissionModels.size(); i++) {
                    info.addStringPermission(permissionModels.get(i).getPermissionName());
                }
                redisCache.put("permission"+user.getId(), info);
                return info;
            }
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username=(String)authenticationToken.getPrincipal();
        UserModel user=userService.findUserByName(username);
        System.out.println("--------------------"+user);
        logger.info(username+"登录---"+user);
        logger.warn("warn_test");
        if(user!=null){
            return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(),ByteSource.Util.bytes(user.getRealSalt()),getName());
        }
        return null;
    }
}
