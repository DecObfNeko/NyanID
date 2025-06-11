package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Data
public class ServerList {

    @Id
    @Column(columnDefinition="varchar(150)",nullable = false)
    private String ServerUid;

    private String ServerName;

    private String Token;

    @Column(columnDefinition="varchar(150)",nullable = false)
    private String SKey;

    private String SessionID;
}
