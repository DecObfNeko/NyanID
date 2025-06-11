package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Data
@Entity
public class Yggdrasil implements Serializable {

    @Id
    @Column(columnDefinition="varchar(150)",nullable = false)
    private String uuid;

    private Boolean useSkin;

    private Boolean useCAPE;

    @Column(columnDefinition="varchar(20)",nullable = false)
    private String playername;

    @Column(columnDefinition="varchar(150)",nullable = false)
    private String nyanuid;

    private int type;

}
