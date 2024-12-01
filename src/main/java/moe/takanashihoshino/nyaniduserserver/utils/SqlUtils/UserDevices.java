package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Data
public class UserDevices implements Serializable {

    @Id
    @Column(columnDefinition="varchar(150)",nullable = false)
    private String uid;

    private String DeviceName;

    private String DeviceID;

    private String Token;

    private String Session;

    private boolean IsActive;

    private long ExpireTime;



}
