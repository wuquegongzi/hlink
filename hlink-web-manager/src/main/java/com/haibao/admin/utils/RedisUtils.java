package com.haibao.admin.utils;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/*
 * @Author ml.c
 * @Description redis工具类
 * @Date 00:07 2020-04-05
 **/
@Slf4j
@Component
public class RedisUtils {

    /**
     * 从 spring 容器中注入自定义的 redisTemplate
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 切换数据库
     * @param index 游标
     */
    public void changeDatebase(Integer index){

        LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory)redisTemplate.getConnectionFactory();
        //设置数据库
        lettuceConnectionFactory.setDatabase(index);
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        //重启连接
        lettuceConnectionFactory.resetConnection();
    }
    /**
     * 对 redis 中指定键对应的数据设置失效时间
     *
     * @param key  键
     * @param time 时间（秒），time要大于0
     * @return
     */
    public boolean expire(String key, long time) {
        boolean result = false;

        try {
            if (StringUtils.isNotBlank(key) && time > 0) {
                result = this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return result;
    }

    /**
     * 从 redis 中根据指定的 key 获取已设置的过期时间
     *
     * @param key 键，不能为null
     * @return 时间（秒），返回0代表为永久有效
     */
    public long getExpire(String key) {
        long time = 0L;

        try {
            if (StringUtils.isNotBlank(key)) {
                time = this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return time;
    }

    /**
     * 判断 redis 中是否存在指定的 key
     *
     * @param key 键，不能为null
     * @return true表示存在，false表示不存在
     */
    public boolean exists(String key) {
        boolean result = false;

        try {
            if (StringUtils.isNotBlank(key)) {
                result = this.redisTemplate.hasKey(key);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return result;
    }

    /**
     * 从 redis 中移除指定 key 对应的数据
     *
     * @param keys 可以传一个值或多个
     */
    @SuppressWarnings("unchecked")
    public long remove(String... keys) {
        long count = 0L;

        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                boolean result = this.redisTemplate.delete(keys[0]);

                if (result) {
                    count = keys.length;
                }
            } else {
                count = this.redisTemplate.delete(CollectionUtils.arrayToList(keys));
            }
        }
        return count;
    }


    // ============================ String =============================
    /**
     * 从 redis 中获取指定 key 对应的 string 数据
     *
     * @param key 键，不能为null
     * @return key 对应的字符串数据
     */
    public <T> T get(String key) {
        T t = null;

        try {
            if (StringUtils.isNotBlank(key)) {
                t = (T) this.redisTemplate.opsForValue().get(key);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return t;
    }


    /**
     * 从 redis 中获取指定 key 对应的 string 数据，并转换为 T 类型
     *
     * @param key   键，不能为null
     * @param clazz 类型，从 redis 获取后的对象直接转换为 T 类型
     * @return key 对应的数据
     */
    public <T> T get(String key, Class<T> clazz) {
        T t = null;

        try {
            if (StringUtils.isNotBlank(key)) {
                String str = (String) this.redisTemplate.opsForValue().get(key);

                t = this.stringToBean(str, clazz);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }

        return t;
    }

    /**
     * 判断 redis 中指定 key 的数据对应偏移位置的 bit 位是否为 1
     *
     * @param key    键，不能为null
     * @param offset 偏移位置
     * @return true表示存在，false表示不存在
     */
    public boolean getBit(String key, long offset) {
        boolean result = false;

        try {
            if (StringUtils.isNotBlank(key)) {
                result = this.redisTemplate.opsForValue().getBit(key, offset);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return result;
    }

    /**
     * 将指定的 key, value 放到 redis 中
     *
     * @param key   键，不能为null
     * @param value 值，不能为null
     * @return true表示成功，false表示失败
     */
    public <T> boolean set(String key, T value) {
        boolean result = false;

        try {
            if (StringUtils.isNotBlank(key)) {
                this.redisTemplate.opsForValue().set(key, value);

                result = true;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return result;
    }

    /**
     * 将指定的 key, value 放到 redis 中，并设置过期时间
     *
     * @param key   键，不能为null
     * @param value 值，不能为null
     * @param time  时间（秒），time要大于0，如果time小于等于0，将设置无限期
     * @return true表示成功，false表示失败
     */
    public <T> boolean set(String key, T value, long time) {
        try {
            if (time > 0) {
                this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                this.set(key, value);
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 将 redis 中指定 key 对应数据的偏移位置的 bit 位设置为 0/1
     *
     * @param key    键，不能为null
     * @param offset 偏移位置
     * @param flag   true表示设置为1，false表示设置为0
     * @return true表示成功，false表示失败
     */
    public boolean setBit(String key, long offset, boolean flag) {
        boolean result = false;

        try {
            if (StringUtils.isNotBlank(key)) {
                this.redisTemplate.opsForValue().setBit(key, offset, flag);

                result = true;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return result;
    }


    /**
     * 对 redis 中指定 key 的数据递增，并返回递增后的值
     *
     * @param key   键，不能为null
     * @param delta 要增加几（大于0）
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0。");
        }

        return this.redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 对 redis 中指定 key 的数据递减，并返回递减后的值
     *
     * @param key   键，不能为null
     * @param delta 要减少几（大于0）
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0。");
        }

        return this.redisTemplate.opsForValue().decrement(key, delta);
    }


    // ================================ hashmap=================================

    /**
     * 判断 redis 中指定 key 对应的 hash 表中是否有 hashKey
     *
     * @param key     键，不能为null
     * @param hashKey hash表中的键，不能为null
     * @return true表示存在，false表示不存在
     */
    public boolean hexists(String key, String hashKey) {
        return this.redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 从 redis 中获取指定 key 对应的 hash 表中的指定 hashKey 所对应的值
     *
     * @param key     键，不能为null
     * @param hashKey hash表中的键，不能为null
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T hget(String key, String hashKey) {
        return (T) this.redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 向 redis 中指定 key 对应的 hash 表（如果 hash 表不存在则自动创建）中放入 hashKey,value 数据
     *
     * @param key     键，不能为null
     * @param hashKey hash表中的键，不能为null
     * @param value   值，不能为null
     * @return true表示成功，false表示失败
     */
    public <T> boolean hset(String key, String hashKey, T value) {
        try {
            this.redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 向 redis 中指定 key 对应的 hash 表（如果 hash 表不存在则自动创建）中放入 hashKey,value 数据，并设置过期时间
     *
     * @param key     键，不能为null
     * @param hashKey hash表中的键，不能为null
     * @param value   值，不能为null
     * @param time    过期时间（秒），注意:如果已存在的hash表有时间，这里将会替换原有的时间
     * @return true表示成功，false表示失败
     */
    public <T> boolean hset(String key, String hashKey, T value, long time) {
        try {
            this.redisTemplate.opsForHash().put(key, hashKey, value);
            if (time > 0) {
                this.expire(key, time);
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 删除 redis 中指定 key 对应的 hash 表中等于 hashKeys 的数据
     *
     * @param key      键，不能为null
     * @param hashKeys hash表中的键，可以使多个，不能为null
     */
    public <T> void hdel(String key, T... hashKeys) {
        this.redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 从 redis 中获取指定 key 对应的 hash 表，并返回相应的 map 对象
     *
     * @param key 键，不能为null
     * @return map对象，包含所有的键值对
     */
    public Map<?, ?> hmget(String key) {
        return this.redisTemplate.opsForHash().entries(key);
    }

    /**
     * 向 redis 中放入指定 key，并设置对应的数据类型为 map
     *
     * @param key 键，不能为null
     * @param map 多个键值对应的map，不能为null
     * @return true表示成功，false表示失败
     */
    public boolean hmset(String key, Map<String, ?> map) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 向 redis 中放入指定 key，并设置对应的数据类型为 map 以及过期时间
     *
     * @param key  键，不能为null
     * @param map  对应多个键值，不能为null
     * @param time 时间（秒），time要大于0，如果time小于等于0，将设置无限期
     * @return true表示成功，false表示失败
     */
    public boolean hmset(String key, Map<String, ?> map, long time) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                this.expire(key, time);
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 对 redis 中指定 key 对应的 hash 表（如果 hash 表不存在则自动创建）递增，并返回新增后的值
     *
     * @param key   键，不能为null
     * @param item  项，不能为null
     * @param delta 要增加几（大于0）
     * @return 新增后的值
     */
    public double hincr(String key, String item, double delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0。");
        }

        return this.redisTemplate.opsForHash().increment(key, item, delta);
    }

    /**
     * 对 redis 中指定 key 对应的 hash 表（如果 hash 表不存在则自动创建）递增，并返回新增后的值
     *
     * @param key   键，不能为null
     * @param item  项，不能为null
     * @param delta 要减少几（大于0）
     * @return
     */
    public double hdecr(String key, String item, double delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0。");
        }

        return this.redisTemplate.opsForHash().increment(key, item, -delta);
    }

    /**
     * 从 redis 中移除指定 key 对应 hash 表中键为 values 的数据
     *
     * @param key    键，不能为null
     * @param values 值，不能为null
     * @return 移除的个数
     */
    public <T> long hremove(String key, T... values) {
        try {
            Long count = this.redisTemplate.opsForHash().delete(key, values);
            return count;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return 0;
        }
    }


    // ============================set=============================

    /**
     * 判断 redis 中是否存在指定 key 对应的 set 对象
     *
     * @param key   键，不能为null
     * @param value 值，不能为null
     * @return true表示存在，false表示不存在
     */
    public <T> boolean sexists(String key, T value) {
        try {
            return this.redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 从 redis 中根据指定 key 对应的值，并返回 set 对象
     *
     * @param key 键，不能为null
     * @return
     */
    public Set<?> sget(String key) {
        try {
            return this.redisTemplate.opsForSet().members(key);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 向 redis 中放入指定 key 和数据，并设置其数据类型为 set
     *
     * @param key    键，不能为null
     * @param values 值，不能为null
     * @return 成功个数
     */
    public <T> long sset(String key, T... values) {
        try {
            return this.redisTemplate.opsForSet().add(key, values);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return 0;
        }
    }

    /**
     * 向 redis 中放入指定 key 和数据，并设置其数据类型为 set 以及过期时间
     *
     * @param key    键，不能为null
     * @param time   过期时间（秒），注意:如果已存在的hash表有时间，这里将会替换原有的时间
     * @param values 值，不能为null
     * @return 成功个数
     */
    public <T> long sset(String key, long time, T... values) {
        try {
            Long count = this.redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return 0;
        }
    }

    /**
     * 获取 redis 中指定 key 对应 set 的大小
     *
     * @param key 键，不能为null
     * @return
     */
    public long ssize(String key) {
        try {
            return this.redisTemplate.opsForSet().size(key);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return 0;
        }
    }

    /**
     * 从 redis 中移除指定 key 对应 set 中键为 values 的数据
     *
     * @param key    键，不能为null
     * @param values 值，不能为null
     * @return 移除的个数
     */
    public <T> long sremove(String key, T... values) {
        try {
            Long count = this.redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return 0;
        }
    }


    // =============================== list =================================

    /**
     * 从 redis 中获取指定 key 对应 list 的大小
     *
     * @param key 键，不能为null
     * @return
     */
    public long lsize(String key) {
        try {
            return this.redisTemplate.opsForList().size(key);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return 0;
        }
    }

    /**
     * 从 redis 中获取指定 key 对应 list 中 index 位置的值
     *
     * @param key   键，不能为null
     * @param index 当index>=0时，0为表头，1为第二个元素，依次类推；当index<0时，-1为表尾，-2为倒数第二个元素，依次类推
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T lindex(String key, long index) {
        try {
            return (T) this.redisTemplate.opsForList().index(key, index);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 从 redis 中获取指定 key 对应 list 指定范围的值（start~end设置为0~-1将返回所有值）
     *
     * @param key   键，不能为null
     * @param start 起始位置，0表示起始位置
     * @param end   结束位置，-1表示结束位置
     * @return
     */
    public List<?> lget(String key, long start, long end) {
        try {
            return this.redisTemplate.opsForList().range(key, start, end);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 向 redis 中放入指定 key，并设置数组类型为 list，将 value 加入到 list 尾部
     *
     * @param key   键，不能为null
     * @param value 值，不能为null
     * @return
     */
    public <T> boolean lset(String key, T value) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 向 redis 中放入指定 key，并设置数组类型为 list，将 value 加入到 list 尾部，同时设置过期时间
     *
     * @param key   键，不能为null
     * @param value 值，不能为null
     * @param time  时间（秒），time要大于0，如果time小于等于0，将设置无限期
     * @return
     */
    public <T> boolean lset(String key, T value, long time) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                this.expire(key, time);
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 向 redis 中放入指定 key，并设置数组类型为 list，并以 value 填入 list
     *
     * @param key   键，不能为null
     * @param value 值，不能为null
     * @return
     */
    public boolean lset(String key, List<?> value) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 向 redis 中放入指定 key，并设置数组类型为 list，并以 value 填入 list，同时设置过期时间
     *
     * @param key   键，不能为null
     * @param value 值，不能为null
     * @param time  时间（秒），time要大于0，如果time小于等于0，将设置无限期
     * @return
     */
    public boolean lset(String key, List<?> value, long time) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                this.expire(key, time);
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 将 redis 中指定 key 对应的 list 数据中指定 index 位置的数据更新为 value
     *
     * @param key   键，不能为null
     * @param index 索引
     * @param value 值，不能为null
     * @return
     */
    public <T> boolean lupdate(String key, long index, T value) {
        try {
            this.redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 从 redis 中指定 key 对应的 list 中移除 n 个值为 value 的数据
     *
     * @param key   键，不能为null
     * @param count 移除多少个
     * @param value 值，不能为null
     * @return 移除的个数
     */
    public <T> long lremove(String key, long count, T value) {
        try {
            Long remove = this.redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return 0;
        }
    }


    /**
     * 根据指定的类型转换字符串/JSON字符串为相应的对象
     *
     * @param value 字符串/JSON字符串
     * @param clazz 类型
     * @param <T>   任意类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T stringToBean(String value, Class<T> clazz) {
        if (value == null || value.length() <= 0 || clazz == null) {
            return null;
        }

        if (clazz == byte.class || clazz == Byte.class) {
            return (T) Byte.valueOf(value);
        } else if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(value);
        } else if (clazz == double.class || clazz == Double.class) {
            return (T) Double.valueOf(value);
        } else if (clazz == float.class || clazz == Float.class) {
            return (T) Float.valueOf(value);
        } else if (clazz == String.class) {
            return (T) value;
        } else {
            return JSONUtil.toBean(value, clazz);
        }
    }

    /**
     * 将 bean 转换为字符串/JSON字符串
     *
     * @param value 任意类型对象
     * @return 字符串/JSON字符串
     */
    public <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }

        Class<?> clazz = value.getClass();
        if (clazz == byte.class || clazz == Byte.class) {
            return "" + value;
        } else if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else if (clazz == double.class || clazz == Double.class) {
            return "" + value;
        } else if (clazz == float.class || clazz == Float.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSONUtil.toJsonStr(value);
        }
    }
}
