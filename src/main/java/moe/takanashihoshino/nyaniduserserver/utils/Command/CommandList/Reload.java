package moe.takanashihoshino.nyaniduserserver.utils.Command.CommandList;

import moe.takanashihoshino.nyaniduserserver.utils.Command.Command;
import moe.takanashihoshino.nyaniduserserver.utils.Reload.CustomRefreshScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
public class Reload  implements Command  {

    private final CustomRefreshScope refreshScope; // 直接注入CustomRefreshScope

    @Autowired
    public Reload(CustomRefreshScope refreshScope) {
        this.refreshScope = refreshScope;
    }
    @Override
    public String getName() {
        return "/reload";
    }

    @Override
    public String getDescription() {
        return "重载";
    }

    @Override
    public void execute(String[] args) {
        refreshScope.refreshAll();
         }
    }
