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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String Appid;

    @Column
    private String ClientID;

    @Column
    private String IsActive;

    @Column
    private String NyanUID;
}
