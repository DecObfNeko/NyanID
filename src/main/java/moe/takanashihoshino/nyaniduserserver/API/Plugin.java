package moe.takanashihoshino.nyaniduserserver.API;

import java.util.UUID;
import java.util.logging.Logger;

public interface Plugin {
    void onLoad();
    void onEnable();

    void onDisable();

    String PluginName();

    String PluginUUID(String author );

    Logger getLogger();
}
