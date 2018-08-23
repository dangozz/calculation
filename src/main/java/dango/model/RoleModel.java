package dango.model;

/**
 * @author: DANGO
 * @date 2018/7/3 14:56
 * @Description:
 */
public class RoleModel {

    private Integer id;
    private String roleName;

    public RoleModel() {
    }

    public RoleModel(Integer id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleModel{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
