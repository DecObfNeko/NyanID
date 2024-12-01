package moe.takanashihoshino.nyaniduserserver.utils.RedisUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置一个值
     * @param key     键
     * @param   value 值
     */
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    /**
     * 创建一个值
     * @param key     键
     * @return  value 值
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 创建一个时间性值
     * @param key     键
     * @param value   值
     * @param timeout 时间
     */
    public void setValueWithExpiration(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 删除
     * @param key     键
     */

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    public int getAll() {
        return Objects.requireNonNull(redisTemplate.keys("*")).toString().length();
    }
}