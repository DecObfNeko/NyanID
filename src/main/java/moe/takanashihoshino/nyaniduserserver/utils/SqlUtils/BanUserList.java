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

    private LocalDateTime BanTime;

    private String BannedBy;


}
