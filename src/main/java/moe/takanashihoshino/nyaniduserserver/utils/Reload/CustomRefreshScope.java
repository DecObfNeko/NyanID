package moe.takanashihoshino.nyaniduserserver.utils.Reload;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomRefreshScope implements Scope, ApplicationContextAware {
    private ApplicationContext context;
    private final Map<String, Object> beanMap = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Runnable> destructionCallbacks = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (!beanMap.containsKey(name)) {
            synchronized (this.beanMap) {
                if (!beanMap.containsKey(name)) {
                    beanMap.put(name, objectFactory.getObject());
                }
            }
        }
        return beanMap.get(name);
    }

    @Override
    public Object remove(String name) {
        destructionCallbacks.remove(name);
        return beanMap.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        destructionCallbacks.put(name, callback);
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }

    public void refreshAll() {
        destructionCallbacks.values().forEach(Runnable::run);
        beanMap.clear();
        destructionCallbacks.clear();
        System.out.println("All refreshable beans have been refreshed!");
    }

    public void refreshBean(String beanName) {
        if (beanMap.containsKey(beanName)) {
            Runnable callback = destructionCallbacks.get(beanName);
            if (callback != null) {
                callback.run();
            }
            beanMap.remove(beanName);
            destructionCallbacks.remove(beanName);
            System.out.println("Bean '" + beanName + "' has been refreshed!");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}