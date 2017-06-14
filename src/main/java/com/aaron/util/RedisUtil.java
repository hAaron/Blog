package com.aaron.util;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.sun.org.apache.regexp.internal.recompile;

/**
 * Redis������ ע�⣺ʹ��redis�����࣬ʵ�����л��ӿڣ� ע��redis �汾
 * 
 * @author Aaron
 * @date 2017��6��12��
 * @version 1.0
 * @package_name com.aaron.util
 */
public class RedisUtil {
	// private Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	private static RedisTemplate<Serializable, Object> redisTemplate;

	static {
		redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
	}
	
	/**
	 * ����ɾ����Ӧ��value
	 * 
	 * @param keys
	 */
	public static void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * ����ɾ��key
	 * 
	 * @param pattern
	 */
	public static void removePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0)
			redisTemplate.delete(keys);
	}

	/**
	 * ɾ����Ӧ��value
	 * 
	 * @param key
	 */
	public static void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * �жϻ������Ƿ��ж�Ӧ��value
	 * 
	 * @param key
	 * @return
	 */
	public static boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * ��ȡ����
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(final String key) {
		Object result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate
				.opsForValue();
		result = operations.get(key);
		return result;
	}

	/**
	 * д�뻺��
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(final String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate
					.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * д�뻺��
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate
					.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
