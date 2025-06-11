package moe.takanashihoshino.nyaniduserserver.utils.Command;


import jakarta.annotation.PostConstruct;
import moe.takanashihoshino.nyaniduserserver.utils.Command.CommandList.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@Component
@RestController
public class CmdMain {


    private final RedisCommand redisCommand;
    private final UserManagerCommand userManagerCommand;
    private final SystemctlCommand systemctlCommand;

    private final Reload reload;



    @Value("${NyanidSetting.EnableCommand}")
    private boolean EnableCommand;

    public CmdMain(RedisCommand redisCommand, UserManagerCommand userManagerCommand, SystemctlCommand systemctlCommand, Reload reload) {
        this.redisCommand = redisCommand;
        this.userManagerCommand = userManagerCommand;
        this.systemctlCommand = systemctlCommand;
        this.reload = reload;
    }

    @PostConstruct
    public void run() {
        if (EnableCommand){
        try {
            CommandManager commandManager = new CommandManager();
            //Register Commands
            commandManager.registerCommand(new HelloCommand());
            commandManager.registerCommand(new HelpCommand());
            commandManager.registerCommand(new StopCommand());
            commandManager.registerCommand(systemctlCommand);
            commandManager.registerCommand(redisCommand);
            commandManager.registerCommand(userManagerCommand);
            commandManager.registerCommand(reload);
            //END Register
            Thread consoleThread = new Thread(new ConsoleInputHandler(commandManager));
            consoleThread.start();
            Logger.getLogger("NyanID").info("NyanID-UserServer加载完成,您可以在控制台输入/help命令查看指令喵~");
        }catch (Exception e){
            Logger.getLogger("NyanID").warning("Console Thread Error :"+e);
        }
    }else {
            Logger.getLogger("NyanID").warning("NyanID-UserServer加载完成,控制台指令已在配置文件中禁用,你可以手动修改application.yml中的相关选项以启用喵~");
        }
    }
}
