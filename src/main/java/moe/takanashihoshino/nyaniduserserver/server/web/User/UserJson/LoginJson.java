package moe.takanashihoshino.nyaniduserserver.server.web.User.UserJson;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoginJson {
    private String data;

    private String status;

    private String token;

    private LocalDateTime timestamp;

}
