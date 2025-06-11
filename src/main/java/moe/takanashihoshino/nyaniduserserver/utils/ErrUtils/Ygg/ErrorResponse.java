package moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.Ygg;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String error;
    private String errorMessage;
    private String cause;

}