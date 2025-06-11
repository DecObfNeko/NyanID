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

    private int exp;

    private boolean IsDeveloper;

    private String Description;

    private String nickname;

    private Boolean IsGIFAvatar;

    private Boolean EnableGIFAvatar;

    private int GIFAvatarID;
}
