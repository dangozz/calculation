package dango.service.impl;

import dango.dao.RoleDao;
import dango.model.RoleModel;
import dango.model.UserModel;
import dango.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: DANGO
 * @date 2018/7/3 15:20
 * @Description:
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleModel> implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public void setBaseDao() {
        super.setBaseDao(roleDao);
    }

    @Override
    public List<RoleModel> findAllModel() {
        return roleDao.findAllModel();
    }

    @Override
    public List<RoleModel> findUserRoles(UserModel user) {
        return roleDao.findUserRoles(user);
    }
}
