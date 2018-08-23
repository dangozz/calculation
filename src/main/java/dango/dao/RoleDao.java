package dango.dao;

import dango.model.RoleModel;
import dango.model.UserModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: DANGO
 * @date 2018/7/3 15:13
 * @Description:
 */
public interface RoleDao extends BaseDao<RoleModel> {

    List<RoleModel> findUserRoles(@Param("user")UserModel user);
}
