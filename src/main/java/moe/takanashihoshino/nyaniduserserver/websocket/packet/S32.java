package moe.takanashihoshino.nyaniduserserver.websocket.packet;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class S32 {
    public String packet = "S32";

    public Boolean bind;

    public String uuid;

    public String muid;

    public String username;


}
