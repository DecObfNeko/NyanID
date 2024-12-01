package moe.takanashihoshino.nyaniduserserver.utils;


import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.Error;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.Ygg.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse>  handleException(Exception e) {
        Error errorResponse = new Error();
        errorResponse.setStatus(400);
        errorResponse.setMessage(e.getMessage());
        errorResponse.setError("Bad Request");
        errorResponse.setTimestamp(LocalDateTime.now());
        ErrorResponse error = new ErrorResponse(errorResponse.getError(),errorResponse.getMessage(),errorResponse.getTimestamp().toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>  Exception(Exception e) {
        Error errorResponse = new Error();
        errorResponse.setStatus(500);
        errorResponse.setMessage(e.getMessage());
        errorResponse.setError("Internal Server Error");
        errorResponse.setTimestamp(LocalDateTime.now());
        ErrorResponse error = new ErrorResponse(errorResponse.getError(),errorResponse.getMessage(),errorResponse.getTimestamp().toString());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
