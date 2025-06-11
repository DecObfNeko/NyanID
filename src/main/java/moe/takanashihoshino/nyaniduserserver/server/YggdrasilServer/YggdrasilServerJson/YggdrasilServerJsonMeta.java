package moe.takanashihoshino.nyaniduserserver.server.YggdrasilServer.YggdrasilServerJson;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YggdrasilServerJsonMeta {
    private String implementationName;
    private String implementationVersion;
    private String serverName;
    private YggdrasilServerJsonLinks links;
    private boolean feature_non_email_login;

}
