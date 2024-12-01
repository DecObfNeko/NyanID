package moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.Ygg;

public class ErrorResponse {
    private String error;
    private String errorMessage;

    private String cause
    ;

    public ErrorResponse(String error, String errorMessage,String cause) {
        this.error = error;
        this.errorMessage = errorMessage;
        this.cause = cause;

    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}