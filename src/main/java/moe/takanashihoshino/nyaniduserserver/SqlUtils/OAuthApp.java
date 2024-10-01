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
    @Column(columnDefinition="varchar(150)",nullable = false)
    private String Appid;

    @Column(columnDefinition="varchar(150)",nullable = false)
    private String CreateUser;

    private String CallBackUrl;

    @Column(nullable = false)
    private Boolean IsActive;

    private String SecretKey;

    @Column(columnDefinition="varchar(150)",nullable = false)
    private String AppName;

    @Column(columnDefinition="text")
    private String AppDesc;
}
