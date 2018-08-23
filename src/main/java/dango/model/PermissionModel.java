package dango.model;

/**
 * @author: DANGO
 * @date 2018/7/3 15:43
 * @Description:
 */
public class PermissionModel {
    private Integer id;
    private String permissionName;

    public PermissionModel() {
    }

    public PermissionModel(Integer id, String permissionName) {
        this.id = id;
        this.permissionName = permissionName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public String toString() {
        return "PermissionModel{" +
                "id=" + id +
                ", permissionName='" + permissionName + '\'' +
                '}';
    }
}
