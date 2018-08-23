package dango.dao;

import dango.model.PermissionModel;
import dango.model.RoleModel;
import dango.model.UserModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: DANGO
 * @date 2018/7/3 15:42
 * @Description:
 */
public interface PermissionDao extends BaseDao<PermissionModel> {

    List<PermissionModel> findRolePermissions(@Param("role")RoleModel role);
    List<PermissionModel> findUserPermissions(@Param("user")UserModel user);
}
