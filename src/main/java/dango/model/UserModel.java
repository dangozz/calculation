package dango.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserModel implements Serializable {

    private static final long serialVersionUID = 5231134212346077681L;

    private Integer id;
    private String username;
    private String password;
    private String salt;

    public UserModel() {
    }

    public UserModel(Integer id, String username, String password, String salt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public String getRealSalt(){
        return username+salt;
    }

}
