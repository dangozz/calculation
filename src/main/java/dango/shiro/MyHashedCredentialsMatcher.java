package dango.shiro;

/**
 * @author: DANGO
 * @date 2018/7/4 16:55
 * @Description:
 */

import java.util.concurrent.atomic.AtomicInteger;

import dango.model.UserModel;
import dango.shiro.redis.RedisCache;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyHashedCredentialsMatcher extends HashedCredentialsMatcher {
    private Cache<String, AtomicInteger> passwordRetryCache;

    @Autowired
    private RedisCache redisCache;

    /*public MyHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if(retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if(retryCount.incrementAndGet() > 5) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }

        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            //clear retry count
            passwordRetryCache.remove(username);
            redisCache.evict("user"+username);
        }
        return matches;
    }*/

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = (AtomicInteger) redisCache.get2("number_"+username);
        if(retryCount == null) {
            retryCount = new AtomicInteger(0);
        }
        if(retryCount.incrementAndGet() > 5) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }else {
            redisCache.evict("number_"+username);
            redisCache.putWithTime("number_"+username, retryCount,180);
        }
        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            //clear retry count
            redisCache.evict("number_"+username);
            redisCache.evict("user"+username);
            redisCache.evict("permission" + username);
        }
        return matches;
    }

   /* @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        //retry count + 1
        System.out.println("*********1");
        Integer retryCount = (Integer) redisCache.get2("number_" + username);
        System.out.println("*********2");
        if (retryCount == null) {
            retryCount = 0;
        }
        System.out.println("*********3");
        if ((retryCount+=1) > 5) {
            //if retry count > 5 throw
            System.out.println("*********4");
            throw new ExcessiveAttemptsException();
        } else {
            System.out.println("*********5");
            redisCache.putWithTime("number_" + username, retryCount, 300);
        }
        System.out.println("*********6");
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //clear retry count
            redisCache.evict("number_" + username);
            redisCache.evict("user" + username);
            redisCache.evict("permission" + username);
        }
        return matches;
    }
*/
}
