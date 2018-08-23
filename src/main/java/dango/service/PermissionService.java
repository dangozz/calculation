package dango.service;

import dango.model.PermissionModel;
import dango.model.RoleModel;
import dango.model.UserModel;

import java.util.List;

/**
 * @author: DANGO
 * @date 2018/7/3 15:47
 * @Description:
 */
public interface PermissionService extends BaseService<PermissionModel> {

    List<PermissionModel> findRolePermissions(RoleModel role);
    List<PermissionModel> findUserPermissions(UserModel user);
}
