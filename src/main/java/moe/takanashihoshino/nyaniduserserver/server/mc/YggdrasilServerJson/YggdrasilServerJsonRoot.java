package moe.takanashihoshino.nyaniduserserver.server.mc.YggdrasilServerJson;

public class YggdrasilServerJsonRoot {
    private YggdrasilServerJsonMeta meta;
    private String[] skinDomains;
    private String signaturePublickey;

    public YggdrasilServerJsonMeta getMeta() {
        return meta;
    }

    public void setMeta(YggdrasilServerJsonMeta meta) {
        this.meta = meta;
    }

    public String[] getSkinDomains() {
        return skinDomains;
    }

    public void setSkinDomains(String[] skinDomains) {
        this.skinDomains = skinDomains;
    }

    public String getSignaturePublickey() {
        return signaturePublickey;
    }

    public void setSignaturePublickey(String signaturePublickey) {
        this.signaturePublickey = signaturePublickey;
    }
}
