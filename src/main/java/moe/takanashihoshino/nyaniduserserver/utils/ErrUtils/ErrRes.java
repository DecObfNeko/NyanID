package moe.takanashihoshino.nyaniduserserver.utils.ErrUtils;

import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

public class ErrRes {
    public static Error IllegalClientException(String message, HttpServletResponse response){
        response.setStatus(ErrorCode.IllegalClient.getCode());
        Error error = new Error();
        error.setStatus(ErrorCode.IllegalClient.getCode());
        error.setError(ErrorCode.IllegalClient.getMessage());
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }
    public static Error IllegalRequestException(String message, HttpServletResponse response){
        response.setStatus(ErrorCode.IllegalRequest.getCode());
        Error error = new Error();
        error.setStatus(ErrorCode.IllegalRequest.getCode());
        error.setError(ErrorCode.IllegalRequest.getMessage());
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }

    public static Error NotFoundResourceException(String message, HttpServletResponse response){
        response.setStatus(ErrorCode.NotFoundResource.getCode());
        Error error = new Error();
        error.setStatus(ErrorCode.NotFoundResource.getCode());
        error.setError(ErrorCode.NotFoundResource.getMessage());
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }

    public static Error UNKNOWN_ERRORException(String message, HttpServletResponse response ){
        response.setStatus(ErrorCode.UNKNOWN_ERROR.getCode());
        Error error = new Error();
        error.setStatus(ErrorCode.UNKNOWN_ERROR.getCode());
        error.setError(ErrorCode.UNKNOWN_ERROR.getMessage());
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }

    public static Error NotFoundAccountException(String message, HttpServletResponse response){
        response.setStatus(ErrorCode.NotFoundAccount.getCode());
        Error error = new Error();
        error.setStatus(ErrorCode.NotFoundAccount.getCode());
        error.setError(ErrorCode.NotFoundAccount.getMessage());
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }

    public static Error MethodNotAllowedException(String message, HttpServletResponse response){
        response.setStatus(ErrorCode.MethodNotAllowed.getCode());
        Error error = new Error();
        error.setStatus(ErrorCode.MethodNotAllowed.getCode());
        error.setError(ErrorCode.MethodNotAllowed.getMessage());
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }

    public static Error UnauthorizedException(String message, HttpServletResponse response){
        response.setStatus(ErrorCode.Unauthorized.getCode());
        Error error = new Error();
        error.setStatus(ErrorCode.Unauthorized.getCode());
        error.setError(ErrorCode.Unauthorized.getMessage());
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }

    public static Error Dimples1337Exception(String message, HttpServletResponse response){
        response.setStatus(ErrorCode.Dimples1337.getCode());
        Error error = new Error();
        error.setStatus(ErrorCode.Dimples1337.getCode());
        error.setError(ErrorCode.Dimples1337.getMessage());
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }

}
