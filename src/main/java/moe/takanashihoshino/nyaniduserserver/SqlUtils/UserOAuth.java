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
public class UserOAuth  implements Serializable {
    @Id
    @Column(columnDefinition="varchar(150)",nullable = false)
    private String Appid;

    @Column(columnDefinition="varchar(150)",nullable = false)
    private String ClientID;

    @Column(nullable = false)
    private Boolean IsActive;

    @Column(columnDefinition="varchar(150)",nullable = false)
    private String NyanUID;
}
