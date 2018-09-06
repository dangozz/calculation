package dango.shiro.redis;

//import org.springframework.beans.factory.annotation.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * @author: DANGO
 * @date 2018/7/11 10:32
 * @Description:
 */
public class RedisCache implements Cache {

    private RedisTemplate<String, Object> redisTemplate;
    private String name;
//    @Value("${redis.expireTime}")
    private long expireTime;

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public void setExpireTime(long expireTime){
        this.expireTime=expireTime;
    }

    @Override
    public void clear() {

        System.out.println("-------清理缓存------");
        redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    @Override
    public void evict(Object key) {
        logger.info("-------删除缓存------"+key.toString());
        System.out.println("-------删除缓存------");
        final String keyf=key.toString();
        System.out.println(keyf);
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.del(keyf.getBytes());
            }

        });

    }

    @Override
    public ValueWrapper get(Object key) {
        logger.info("------获取缓存1-------"+key.toString());
        System.out.println("------获取缓存-------"+key.toString());
        final String keyf = key.toString();
        Object object = null;
        object = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] key = keyf.getBytes();
                byte[] value = connection.get(key);
                if (value == null) {
                    logger.info("------缓存1不存在-------");
                    System.out.println("------缓存不存在-------");
                    return null;
                }
                logger.info("获取1---------------------"+value);
                System.out.println(SerializeUtils.deserialize(value));
                return SerializeUtils.deserialize(value);
            }
        });
        ValueWrapper obj=(object != null ? new SimpleValueWrapper(object) : null);
        logger.info("------获取到1-------"+obj);
        System.out.println("------获取到-------"+obj);
        return  obj;
    }

    @Override
    public void put(Object key, Object value) {
        logger.info("-------缓存增加------"+key+value);
        System.out.println("-------缓存增加------");
        System.out.println("redisKey----:"+key);
        System.out.println("value----:"+value);
        final String keyString = key.toString();
        final Object valuef = value;
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyb = keyString.getBytes();
                byte[] valueb = SerializeUtils.serialize((Serializable) valuef);
                connection.set(keyb, valueb);
                if (expireTime > 0) {
                    connection.expire(keyb, expireTime);
                }
                return 1L;
            }
        });
    }


    public Object get2(Object key) {
        logger.info("------获取缓存2-------"+key.toString());
        System.out.println("------获取缓存-------"+key.toString());
        final String keyf = key.toString();
        Object object = null;
        object = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] key = keyf.getBytes();
                byte[] value = connection.get(key);
                if (value == null) {
                    logger.info("------缓存不存在2-------");
                    System.out.println("------缓存不存在-------");
                    return null;
                }
                Object obj = null;
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream (value);
                    ObjectInputStream ois = new ObjectInputStream (bis);
                    obj = ois.readObject();
                    ois.close();
                    bis.close();
                } catch (IOException ex) {
                    logger.info("------获取异常!!!-------");
                    System.out.println("获取异常!!!");
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    logger.info("------获取异常~~~-------");
                    System.out.println("获取异常~~~");
                    ex.printStackTrace();
                }
                return obj;
            }
        });
        logger.info("------获取到-------"+object);
        return  object;
    }

    public void putWithTime(Object key, Object value,final long time){
        logger.info("-------缓存增加------"+key+value+time);
        System.out.println("-------缓存增加------");
        System.out.println("redisKey----:"+key);
        System.out.println("value----:"+value);
        System.out.println("time----:"+time);
        final String keyString = key.toString();
        final Object valuef = value;
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyb = keyString.getBytes();

                byte[] valueb = null;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(valuef);
                    oos.flush();
                    valueb = bos.toByteArray ();
                    oos.close();
                    bos.close();
                } catch (IOException ex) {
                    System.out.println("增加异常@@@");
                    ex.printStackTrace();
                }

                connection.set(keyb, valueb);
                if (time > 0) {
                    connection.expire(keyb, time);
                }
                return 1L;
            }
        });
    }

    @Override
    public <T> T get(Object arg0, Class<T> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.redisTemplate;
    }

    @Override
    public ValueWrapper putIfAbsent(Object arg0, Object arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public <T> T get(Object arg0, Callable<T> arg1) {
        // TODO Auto-generated method stub
        return null;
    }




}