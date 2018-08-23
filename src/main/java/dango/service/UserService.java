package dango.service;

import dango.model.UserModel;

/**
 * @author: DANGO
 * @date 2018/7/3 14:43
 * @Description:
 */
public interface UserService extends BaseService<UserModel> {

    UserModel findUserByName(String username);
}
