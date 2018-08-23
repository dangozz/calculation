package dango.dao;

import dango.model.UserModel;
import org.apache.ibatis.annotations.Param;

/**
 * @author: DANGO
 * @date 2018/7/314:37
 * @Description:
 */
public interface UserDao extends BaseDao<UserModel> {

    UserModel findUserByName(@Param("username")String username);
}
