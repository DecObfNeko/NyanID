package moe.takanashihoshino.nyaniduserserver.utils.QQbotUtils.impl;

import io.github.kloping.qqbot.api.message.MessageEvent;
import io.github.kloping.qqbot.entities.ex.MessageAsyncBuilder;
import io.github.kloping.qqbot.entities.qqpd.message.RawMessage;
import io.github.kloping.qqbot.impl.ListenerHost;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class onMessage extends ListenerHost {
//.replaceAll("\\[", "").replaceAll("]", "")

    private static String[] processString(String originalData) {
        // 移除字符串两端的方括号
        String withoutBrackets = originalData.replaceAll("^\\[|]$", "");

        // 分割字符串，并去除每个元素的前后空格
        List<String> trimmedList = Arrays.stream(withoutBrackets.split("\\s+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty()) // 过滤掉空字符串
                .collect(Collectors.toList());

        // 将列表转换为数组
        return trimmedList.toArray(new String[0]);
    }
    @EventReceiver
    public void onEvent(MessageEvent event) {
     RawMessage message = event.getRawMessage();
        String[] processedData = processString(String.valueOf(message));
      if (Arrays.toString(processedData).isEmpty()){
          event.send("请输入内容");
      }else {
          MessageAsyncBuilder builder = new MessageAsyncBuilder()
                  .text("你好")
                  .image("https://www.runoob.com/wp-content/uploads/2013/06/image-icon.png")
                  .append(message.getContent());
          event.send(builder.build() );
      }
    }
}
