package dango.service.impl;

import dango.dao.PermissionDao;
import dango.model.PermissionModel;
import dango.model.RoleModel;
import dango.model.UserModel;
import dango.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: DANGO
 * @date 2018/7/3 15:48
 * @Description:
 */
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionModel> implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public void setBaseDao() {
        super.setBaseDao(permissionDao);
    }

    @Override
    public List<PermissionModel> findAllModel() {
        return permissionDao.findAllModel();
    }

    @Override
    public List<PermissionModel> findRolePermissions(RoleModel role) {
        return permissionDao.findRolePermissions(role);
    }

    @Override
    public List<PermissionModel> findUserPermissions(UserModel user) {
        return permissionDao.findUserPermissions(user);
    }
}
