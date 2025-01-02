package moe.takanashihoshino.nyaniduserserver.API;

import java.util.UUID;

public interface Plugin {
    void onLoad();
    void onEnable();

    void onDisable();

    String PluginName();

    UUID PluginUUID(String author, String version );
}
