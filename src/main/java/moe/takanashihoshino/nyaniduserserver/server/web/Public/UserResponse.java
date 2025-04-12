package moe.takanashihoshino.nyaniduserserver.server.web.Public;

public class UserResponse {
    private String value;
    private String uid;

    public UserResponse(String value, String link) {
        this.value = value;
        this.uid = link;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String link) {
        this.uid = link;
    }
}