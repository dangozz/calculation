package dango.service;

import dango.model.RoleModel;
import dango.model.UserModel;

import java.util.List;

/**
 * @author: DANGO
 * @date 2018/7/3 15:14
 * @Description:
 */
public interface RoleService extends BaseService<RoleModel> {

    List<RoleModel> findUserRoles(UserModel user);
}
