package cn.org.awcp.venson.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.org.awcp.core.utils.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 缓存
 * 
 * @author Administrator
 *
 */
public class EhCacheBasedCache implements Cache {

	/**
	 * cache属性
	 */
	private net.sf.ehcache.Cache cache;

	/**
	 * 使用指定的名字构建一个缓存，name对应ehcache.xml中的配置
	 * 
	 * @param name
	 * @throws Exception
	 */
	public EhCacheBasedCache() {
		cache = CacheManager.create(this.getClass().getResource("/conf/ehcache.xml")).getCache("cacheData");
		if (cache == null) {
			throw new RuntimeException("缓存创建失败，未找到缓存配置文件");
		}
	}

	/**
	 * 从缓存中获取一个KEY值
	 */
	public Object get(String key) {
		Element el = (Element) cache.get(key);
		if (el != null) {
			return el.getObjectValue();
		}
		return null;
	}

	/**
	 * 传入一系列的KEY值，返回一个MAP
	 */
	public Map<String, Object> get(String... keys) {
		Map<String, Object> result = new HashMap<String, Object>();
		for (String key : keys) {
			Object val = get(key);
			result.put(key, val);
		}
		return result;
	}

	/**
	 * 判定一个KEY值是否在缓存中存在
	 */
	public boolean containsKey(String key) {
		return cache.isKeyInCache(key) && cache.get(key) != null;
	}

	/**
	 * 向缓存中存入一个键值对
	 */
	public void put(String key, Object obj) {
		cache.put(new Element(key, obj));
	}

	/**
	 * 向缓存中存入一个键值对，到指定的时间过期
	 * 
	 * @param key
	 *            指定对象的key
	 * @param obj
	 * @param expiredDate
	 */
	public void put(String key, Object obj, Date expiredDate) {
		Date now = new Date();
		long timeToLiveSeconds = (expiredDate.getTime() - now.getTime()) / 1000;
		put(key, obj, timeToLiveSeconds);
	}

	/**
	 * 向缓存中存入一个键值对，指定其生存的秒数
	 * 
	 * @param key
	 *            指定对象的key
	 * @param obj
	 * @param timeToLiveSeconds
	 */
	public void put(String key, Object obj, long timeToLiveSeconds) {
		int time = (int) timeToLiveSeconds;
		cache.put(new Element(key, obj, false, time, time));
	}

	/**
	 * 从缓存中将某一个KEY值移除出去
	 */
	public boolean remove(String key) {
		return cache.remove(key);
	}

}
