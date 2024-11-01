package moe.takanashihoshino.nyaniduserserver.SqlUtils;


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
public class NekoNyanchoy implements Serializable {

    @Id
    @Column(columnDefinition="varchar(150)",nullable = false)
    private String UserUID;

    private String likability;

    private String TotalCatFood;


    @Column(columnDefinition="text")
    private String Data;

}
