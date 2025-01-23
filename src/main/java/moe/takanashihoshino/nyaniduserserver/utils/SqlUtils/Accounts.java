package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
@Entity
@Data
public class Accounts implements Serializable {

    @Id
    @Column(columnDefinition="varchar(150)",nullable = false)
    private String uid;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String bind;

    @Column
    private String username;

    @Column
    private Boolean isActive;

    @Column
    private String SecretKey;

}
