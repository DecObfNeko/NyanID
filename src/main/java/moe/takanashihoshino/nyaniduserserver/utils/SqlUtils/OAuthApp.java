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
public class OAuthApp implements Serializable {
    @Id
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
