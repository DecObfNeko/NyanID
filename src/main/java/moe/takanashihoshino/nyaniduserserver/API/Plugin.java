package moe.takanashihoshino.nyaniduserserver.API;

import java.util.UUID;
import java.util.logging.Logger;

public interface Plugin {
    void onLoad();
    void onEnable();

    void onDisable();

    String PluginName();

    UUID PluginUUID(String author, String version );

    Logger getLogger();
}
