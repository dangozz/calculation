package dango.service.impl;

import dango.dao.UserDao;
import dango.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author: DANGO
 * @date 2018/7/16 11:16
 * @Description:
 */
@Service
public class TestService {

    @Autowired
    private UserDao userDao;

    @Cacheable(cacheNames = "dango",key = "#root.target+'_'+#name")
    public UserModel test(String name){
        return  userDao.findUserByName(name);
    }

}
