package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Data
public class BanUserList implements Serializable {

    @Id
    @Column(columnDefinition="varchar(150)",nullable = false)
    private String BanID;

    @Column(columnDefinition="varchar(150)",nullable = false)
    private String uid;

    private String Reason;

    private boolean isActive;

    private int Type;  //1限制修改昵称//2限制从Oauth登录//3限制修改头像及头图//4限制上传配置文件

    private LocalDateTime BanTime;

    private String BannedBy;


}
