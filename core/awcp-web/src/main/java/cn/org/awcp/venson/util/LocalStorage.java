package cn.org.awcp.venson.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 持久化数据存储对象，以JSON数据格式进行存储
 *
 * @author Venson
 */
public class LocalStorage {
    protected static final Log logger = LogFactory.getLog(LocalStorage.class);
    public static final String KEY_VALUE = "value";
    public static final String KEY_TIMEOUT = "timeout";
    public static final String KEY_SAVE_TIME = "saveTime";
    private static File awcpFile;
    private static JSONObject store;

    static {
        // 文件存放路径取jre路径
        String path = System.getProperty("user.dir") + File.separator + "awcp.json";
        // 创建平台属性文件
        awcpFile = new File(path);
        // 如果文件不存在则初始化
        if (!awcpFile.exists()) {
            init();
        } else {
            store = getStorage();
        }

    }

    /**
     * 将对象写入awcp.json
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, Object value) {
        set(key, value, -1);
    }

    /**
     * 将对象写入awcp.json
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间(秒)，值为<=0时则为永久
     */
    public static void set(String key, Object value, int timeout) {
        Map<String, Object> map = new HashMap<>(3);
        map.put(KEY_VALUE, value);
        map.put(KEY_TIMEOUT, timeout);
        //不设置过期时间
        if (timeout > 0) {
            map.put(KEY_SAVE_TIME, System.currentTimeMillis());
        }
        // 新增对象
        store.put(key, map);
        // 写入文件
        writeJSONToFile(store);

    }

    /**
     * 将对象转为JSON写入到指定的文件
     *
     * @param obj 写入对象
     */
    public static void writeJSONToFile(Object obj) {
        FileOutputStream writer = null;
        try {
            if (!awcpFile.exists()) {
                awcpFile.createNewFile();
            }
            // 创建文件写入对象
            writer = new FileOutputStream(awcpFile);
            // 开始写入
            JSON.writeJSONString(writer, obj);
        } catch (IOException e) {
            logger.debug("ERROR", e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * 从awcp.json获取对象
     *
     * @param key   键
     * @param clazz 泛型
     * @return T
     */
    public static <T> T get(String key, Class<T> clazz) {
        Object obj = store.get(key);
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof Map)||((Map)obj).get(KEY_TIMEOUT)==null) {
            //移除缓存
            remove(key);
            return null;
        }
        Map map = (Map) obj;
        T value = (T) map.get(KEY_VALUE);
        int timeout = (int) map.get(KEY_TIMEOUT);
        //如果是<=0则直接返回
        if (timeout <= 0) {
            return value;
        } else {
            //判断是否过期
            long saveTime = (long) map.get(KEY_SAVE_TIME) + timeout * 1000;
            if (saveTime > System.currentTimeMillis()) {
                return value;
            } else {
                //移除缓存
                remove(key);
                return null;
            }
        }
    }

    /**
     * 从awcp.json获取对象
     *
     * @param key 键
     * @return Object
     */
    public static Object get(String key) {
        return get(key, Object.class);
    }

    /**
     * 从awcp.json移除对象
     *
     * @param key 键
     * @return Object
     */
    public static Object remove(String key) {
        if (isEmpty()) {
            return null;
        }
        Object data = store.remove(key);
        writeJSONToFile(store);
        return data;
    }

    /**
     * 是否包含键值为key的映射
     *
     * @param key 键
     * @return boolean
     */
    public static boolean hasKey(String key) {
        if (store.containsKey(key)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是空的对象
     */
    public static boolean isEmpty() {
        return store.isEmpty();
    }

    /**
     * 清空awcp.json
     */
    public static void clear() {
        init();
    }

    /**
     * 初始化存储器
     */
    private static void init() {
        store = new JSONObject();
        writeJSONToFile(store);
    }

    /**
     * 获取平台JSON存储对象
     *
     * @return JSONObject
     */
    public static JSONObject getStorage() {
        FileInputStream in = null;
        try {
            in = new FileInputStream(awcpFile);
            return JSON.parseObject(in, JSONObject.class);
        } catch (Exception e) {
            logger.debug("ERROR", e);
            // 解析出错则创建新对象
            return new JSONObject();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

}
