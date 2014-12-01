package com.awesome.pro.db.redis;

import org.apache.log4j.Logger;

import com.awesome.pro.db.redis.client.RedisClientPool;

/**
 * Manager class for Redis server and client.
 * @author siddharth.s
 */
public class RedisManager {

	/**
	 * Root logger instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(RedisManager.class);

	/**
	 * Denotes the initialization status.
	 */
	private static boolean status = false;

	/**
	 * Initializes Redis server and client.
	 * @param configFile Configuration file for Redis.
	 */
	public static synchronized void start(final String configFile) {
		if (!status) {
			RedisServerHolder.startRedisServer(configFile);
			RedisClientPool.initializeClientPool(configFile);
			LOGGER.info("Redis initialized.");
		}
		else {
			LOGGER.warn("Redis already initialized. Ignoring.");
		}
	}

	/**
	 * Shuts down Redis server and client.
	 */
	public static synchronized void stop() {
		if (status) {
			RedisClientPool.shutDownClientPool(true);
			RedisServerHolder.stopRedisServer();
			status = false;
			LOGGER.info("Redis shut down.");
		}
		else {
			LOGGER.warn("Redis hasn't been initialized. Ignoring.");
		}
	}

}
