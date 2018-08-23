package dango.service.impl;

import dango.dao.UserDao;
import dango.model.UserModel;
import dango.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: DANGO
 * @date 2018/7/3 14:44
 * @Description:
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserModel> implements UserService {

    @Autowired
    private UserDao userDao;

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Override
    public void setBaseDao() {
        super.setBaseDao(userDao);
    }

    @Override

    public List<UserModel> findAllModel() {
        return userDao.findAllModel();
    }

    @Override
    @Cacheable(cacheNames = "dango",key = "#root.target+'_'+#username")
    public UserModel findUserByName(String username) {

        logger.info("lllllllllllllllllllllllllllllllllllllllllllllllll");
        return userDao.findUserByName(username);
    }
}
