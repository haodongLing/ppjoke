package com.haodong.practice.lib_network.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * created by linghaoDo on 2020/9/8
 * description:
 * <p>
 * version:
 */
public class CacheManager {
    public static Object toObject(byte[] data) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private static <T>byte[] toByteArray(T body){
        ByteArrayOutputStream baos=null;
        ObjectOutputStream oos=null;
        try{
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(body);
            oos.flush();
            return baos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }
    public static <T> void delete(String key, T body) {
        Cache cache = new Cache();
        cache.key = key;
        cache.data = toByteArray(body);
        CacheDatabase.get().getCache().delete(cache);
    }

    public static <T> void save(String key, T body) {
        Cache cache = new Cache();
        cache.key = key;
        cache.data = toByteArray(body);

        CacheDatabase.get().getCache().save(cache);
    }

    public static Object getCache(String key) {
        Cache cache = CacheDatabase.get().getCache().getCache(key);
        if (cache != null && cache.data != null) {
            return toObject(cache.data);
        }
        return null;
    }
}
