package moe.takanashihoshino.nyaniduserserver.SqlUtils;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class NyanIDServer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(columnDefinition="text")
    private String KeyPub;

    @Column(columnDefinition="text")
    private String KeyPri;


}
