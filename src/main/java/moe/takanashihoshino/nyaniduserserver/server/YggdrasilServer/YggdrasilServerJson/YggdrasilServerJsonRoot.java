package moe.takanashihoshino.nyaniduserserver.server.YggdrasilServer.YggdrasilServerJson;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class YggdrasilServerJsonRoot {
    private YggdrasilServerJsonMeta meta;
    private String[] skinDomains;
    private String signaturePublickey;

}
