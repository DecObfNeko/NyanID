package moe.takanashihoshino.nyaniduserserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;

@SpringBootApplication
@EnableScheduling
public class NyanIdUserserverApplication {
    public static  Path configPath = Paths.get("config");
    public static  Path DataPath = Paths.get("Data");
    public static Path UserAvatar = Paths.get("Data/UserAvatar");
    public static Path UserDataPath = Paths.get("Data/UserData");
    public static Path PluginsPath = Paths.get("plugins");
    private HashMap <Class<?>,Class> Clazz = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Logger.getLogger("NyanID").info("[NyanID-UserServer] ["+ LocalDateTime.now() +"] ConfigPath: /config");
        if (!Files.exists(configPath) || !Files.exists(DataPath) || !Files.exists(UserAvatar) || !Files.exists(UserDataPath) || !Files.exists(PluginsPath)) {
            Files.createDirectories(configPath);
            Files.createDirectories(DataPath);
            Files.createDirectories(UserAvatar);
            Files.createDirectories(UserDataPath);
            Files.createDirectories(PluginsPath);
        }else {
            Resource resource = new ClassPathResource("application.yml");
            Path targetPath = configPath.resolve(Objects.requireNonNull(resource.getFilename()));
            if (!Files.exists(targetPath)) {
                Files.copy(resource.getInputStream(), targetPath);
                Logger.getLogger("NyanID").info("[NyanID-UserServer] ["+ LocalDateTime.now() +"] : 配置文件已复制到config文件夹,请修改配置文件再运行喵~");
                Logger.getLogger("NyanID").info("[NyanID-UserServer] ["+ LocalDateTime.now() +"] : Code By TakanashiHoshino");
                Logger.getLogger("NyanID").info("[NyanID-UserServer] ["+ LocalDateTime.now() +"] : 爱来自ABYDOS喵~");
            } else if (Files.exists(targetPath)) {
                SpringApplication.run(NyanIdUserserverApplication.class, args);
            }
        }
    }

    }

