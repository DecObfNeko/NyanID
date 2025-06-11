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
public class UserPermissions implements Serializable {

    @Id
    @Column(columnDefinition="varchar(33)",nullable = false)
    private String uid;

    @Column(columnDefinition="varchar(15)",nullable = false)
    private String AdminKey;

    @Column(columnDefinition="varchar(15)",nullable = false)
    private String Action;

    @Column(columnDefinition="varchar(15)",nullable = false)
    private String UserGroup;
}
