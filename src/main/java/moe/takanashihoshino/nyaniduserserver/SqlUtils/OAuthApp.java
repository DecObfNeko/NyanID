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
public class OAuthApp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String Appid;

    @Column
    private String CreateUser;

    @Column
    private String CallBackUrl;

    @Column
    private Boolean IsActive;

    @Column
    private String SecretKey;

    @Column
    private String AppName;

    @Column
    private String AppDesc;
}
