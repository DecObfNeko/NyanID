package moe.takanashihoshino.nyaniduserserver.SqlUtils;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition="varchar(150)")
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
    private Boolean is_Active;

    @Column
    private Boolean isBanned;

}
