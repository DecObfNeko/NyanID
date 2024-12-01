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
public class NyanIDuser implements Serializable {

    @Id
    @Column(columnDefinition="varchar(150)",nullable = false)
    private String uid;

    @Column
    private int exp;

    @Column
    private String hwid;

    @Column
    private String clientid;

    private boolean IsDeveloper;

    @Column
    private String BCookie;

    @Column
    private String nickname;


}
