package moe.takanashihoshino.nyaniduserserver.utils.Command.CommandList;

import com.alibaba.fastjson2.JSONObject;
import com.google.gson.annotations.JsonAdapter;
import moe.takanashihoshino.nyaniduserserver.utils.Command.Command;
import moe.takanashihoshino.nyaniduserserver.utils.RedisUtils.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@Component
@RestController
public class SystemctlCommand implements Command {


    @Autowired
    private RedisService redisService;




    @Override
    public String getName() {
        return "/systemctl";
    }

    @Override
    public String getDescription() {
        return "管理系统[/systemctl alert NotificationType NotificationData NotificationTypeName Time]";
    }

    @Override
    public void execute(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
        if (args.length > 0){
            switch (args[0]) {
                case "alert":
                    if (args.length == 5){
                        JSONObject data = new JSONObject();
                        data.put("NotificationType", args[1]);
                        data.put("NotificationData",args[2]);
                        data.put("NotificationTypeName", args[3]);
                        Logger.getLogger("NyanID").info("设置成功");
                        redisService.setValueWithExpiration("ServerInfo",data,Long.parseLong(args[4]),java.util.concurrent.TimeUnit.SECONDS);

                    }else {
                        Logger.getLogger("NyanID").info("设置全站通知失败");
                    }



                    }
            }
    }
}
