package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Data
public class UserDevices implements Serializable {

    @Id
    @Column(columnDefinition="varchar(150)",nullable = false)
    private String clientid;

    private String DeviceName;

    private String DeviceID;

    private String Token;

    private String Ip;

    private String Session;

    private boolean IsActive;

    private String hwid;

    private String uid;

    private LocalDateTime CreateTime;
}
