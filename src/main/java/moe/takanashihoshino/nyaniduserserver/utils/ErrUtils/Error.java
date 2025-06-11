package moe.takanashihoshino.nyaniduserserver.utils.ErrUtils;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class Error {
    private int status;
    private  String error;
    private  String message;
    private LocalDateTime timestamp;

}
