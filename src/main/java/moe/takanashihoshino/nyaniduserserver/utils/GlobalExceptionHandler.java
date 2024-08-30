package moe.takanashihoshino.nyaniduserserver.utils;


import moe.takanashihoshino.nyaniduserserver.ErrUtils.Ygg.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse>  handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse("Default error message, Just Do IT!!!!!", "这是一个通用的异常消息喵~","1145141919810");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
