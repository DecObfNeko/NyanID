package moe.takanashihoshino.nyaniduserserver.SqlUtils;


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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition="varchar(150)",nullable = false)
    private String uuid;

    @Column
    private String playername;

    @Column(columnDefinition="varchar(150)",nullable = false)
    private String nyanuid;

    @Column(columnDefinition="text")
    private String accessToken;

    @Column
    private String type;

}
