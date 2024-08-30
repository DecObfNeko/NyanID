package moe.takanashihoshino.nyaniduserserver.utils.Command;


import jakarta.annotation.PostConstruct;
import moe.takanashihoshino.nyaniduserserver.utils.Command.CommandList.HelloCommand;
import moe.takanashihoshino.nyaniduserserver.utils.Command.CommandList.HelpCommand;
import moe.takanashihoshino.nyaniduserserver.utils.Command.CommandList.RedisCommand;
import moe.takanashihoshino.nyaniduserserver.utils.Command.CommandList.StopCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@Component
@RestController
public class CmdMain {

    @Autowired
    private RedisCommand redisCommand;

    @Value("${NyanidSetting.EnableCommand}")
    private boolean EnableCommand;

    @PostConstruct
    public void run() {
        if (EnableCommand){
        try {
            CommandManager commandManager = new CommandManager();
            //Register Commands
            commandManager.registerCommand(new HelloCommand());
            commandManager.registerCommand(new HelpCommand());
            commandManager.registerCommand(new StopCommand());
            commandManager.registerCommand(redisCommand);
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
