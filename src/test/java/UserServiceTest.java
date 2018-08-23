import dango.model.RoleModel;
import dango.model.UserModel;
import dango.service.PermissionService;
import dango.service.RoleService;
import dango.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author: DANGO
 * @date 2018/7/3 14:51
 * @Description:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Test
    public void findAllModelTest(){
        System.out.println(userService.findAllModel());
        System.out.println("****************************************************");
        System.out.println(roleService.findAllModel());
        System.out.println("****************************************************");
        UserModel userModel=new UserModel();
        userModel.setId(1);
        System.out.println(roleService.findUserRoles(userModel));
        System.out.println("****************************************************");
        System.out.println(permissionService.findAllModel());
        System.out.println("****************************************************");
        RoleModel roleModel=new RoleModel();
        roleModel.setId(1);
        System.out.println(permissionService.findRolePermissions(roleModel));
        System.out.println("****************************************************");
        System.out.println(userService.findUserByName("hufeif"));
        System.out.println("****************************************************");
        System.out.println(permissionService.findUserPermissions(userModel));

    }
}
