package moe.takanashihoshino.nyaniduserserver.server.ygg.YggdrasilServerJson;

public class YggdrasilServerJsonMeta {
    private String implementationName;
    private String implementationVersion;
    private String serverName;
    private YggdrasilServerJsonLinks links;
    private boolean feature_non_email_login;

    public String getImplementationName() {
        return implementationName;
    }

    public void setImplementationName(String implementationName) {
        this.implementationName = implementationName;
    }

    public String getImplementationVersion() {
        return implementationVersion;
    }

    public void setImplementationVersion(String implementationVersion) {
        this.implementationVersion = implementationVersion;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public YggdrasilServerJsonLinks getLinks() {
        return links;
    }

    public void setLinks(YggdrasilServerJsonLinks links) {
        this.links = links;
    }

    public boolean isFeature_non_email_login() {
        return feature_non_email_login;
    }

    public void setFeature_non_email_login(boolean feature_non_email_login) {
        this.feature_non_email_login = feature_non_email_login;
    }
}
