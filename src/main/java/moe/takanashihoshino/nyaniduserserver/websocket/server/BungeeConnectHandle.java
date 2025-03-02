package moe.takanashihoshino.nyaniduserserver.websocket.server;


import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import moe.takanashihoshino.nyaniduserserver.utils.Config;
import moe.takanashihoshino.nyaniduserserver.utils.RedisUtils.RedisService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.ServerListRepository;
import moe.takanashihoshino.nyaniduserserver.utils.UUIDHelper;
import moe.takanashihoshino.nyaniduserserver.websocket.Messages.BungeeSuccessConnect;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@ServerEndpoint("/api/zako/v3/websocket/bungee/{sid}")
public class BungeeConnectHandle implements ApplicationContextAware {

    private static ServerListRepository serverListRepository;
    private static RedisService redisService;
    private static Config config;

    // 实例计数器与集合
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    private static CopyOnWriteArraySet<BungeeConnectHandle> webSocketSet = new CopyOnWriteArraySet<>();

    private Session session;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 静态字段通过应用上下文初始化
        serverListRepository = applicationContext.getBean(ServerListRepository.class);
        redisService = applicationContext.getBean(RedisService.class);
        config = applicationContext.getBean(Config.class);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        // 确保静态字段已初始化
        if (serverListRepository == null) {
            log.error("serverListRepository 未初始化");
            closeSession(session);
            return;
        }
        String token = serverListRepository.findTokenByServerUid(sid);
        try {
            log.info("检测到BungeeCord连接请求，Sid: [{}]，当前连接数: {}", sid, onlineCount.get());
            if (token != null) {
                config.BungeeConnect =true;
                onlineCount.incrementAndGet();
                webSocketSet.add(this);
                handleAuthentication(sid, session);
            } else {
                log.warn("Sid 验证失败: [{}]", sid);
                closeSession(session);
            }
        } catch (Exception e) {
            log.error("处理连接时异常", e);
            closeSession(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        redisService.setValue(message, true);
        // 处理消息
    }

    @OnClose
    public void onClose(Session session) {
        onlineCount.decrementAndGet();
        webSocketSet.remove(this);
        log.warn("Bungee连接断开，Session: {}", session.getId());
    }

    private void handleAuthentication(String sid, Session session) {
        String sessionId = UUIDHelper.generatesessionid(sid).replaceAll("-", "");
        redisService.setValueWithExpiration(sessionId, false, 10, TimeUnit.SECONDS);
        try {
            BungeeSuccessConnect response = new BungeeSuccessConnect();
            response.setServerUid(sid);
            response.setS29(false);
            response.setS30(sessionId);
            session.getBasicRemote().sendText(JSONObject.toJSONString(response)); // 发送验证信息


            CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS).execute(() -> {
                Object value = redisService.getValue(sessionId);
                if (value == null || !(Boolean) value) {
                    log.warn("验证超时或失败，SessionID: {}", sessionId);
                    closeSession(session);
                } else {
                    log.info("验证成功，SessionID: {}", sessionId);
                }
            });
        } catch (IOException e) {
            log.error("发送验证信息失败", e);
            closeSession(session);
        }
    }

    private void closeSession(Session session) {
        try {
            if (session.isOpen()) {
                session.close();
            }
        } catch (IOException e) {
            log.error("关闭会话异常", e);
        }
    }

}