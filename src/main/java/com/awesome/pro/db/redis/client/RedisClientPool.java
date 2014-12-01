package com.awesome.pro.db.redis.client;

import org.apache.log4j.Logger;

import com.awesome.pro.db.redis.references.RedisConfigReferences;
import com.awesome.pro.pool.IObjectPool;
import com.awesome.pro.pool.ObjectPool;

/**
 * Maintains a pool of Redis clients as configured in the specified
 * properties file.
 */
public class RedisClientPool {

	/**
	 * Pool of Redis clients.
	 */
	private static IObjectPool<WrappedRedisClient> REDIS_CLIENT_POOL = null;

	/**
	 * Root logger instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(
			RedisClientPool.class);

	/**
	 * @param configFile File containing configurations for object pooling.
	 */
	public static final void initializeClientPool(final String configFile) {
		RedisConfigReferences.getRedisPort(configFile);
		if (REDIS_CLIENT_POOL == null) {
			synchronized (RedisClientPool.class) {
				if (REDIS_CLIENT_POOL == null)  {
					REDIS_CLIENT_POOL = new ObjectPool<>(configFile,
							new AcquireRedisConnection());
					LOGGER.info("Redis client pool initialized.");
					cleanUpData();
				}
			}
		}
	}

	/**
	 * Deletes all the keys maintained in Redis.
	 */
	private static final void cleanUpData() {
		WrappedRedisClient client = REDIS_CLIENT_POOL.checkOutResource();
		client.getResource().flushAll();
		if (client.getResource().keys("*").size() != 0) {
			LOGGER.warn("Redis data couldn't be couldn't be cleaned up.");
		} else {
			LOGGER.info("Redis data has been cleaned up.");
		}
	}

	/**
	 * Disconnects all clients and shuts down the pool.
	 * @param forceClose Whether the pool is to be closed forcibly.
	 */
	public static final void shutDownClientPool(final boolean forceClose) {
		cleanUpData();
		REDIS_CLIENT_POOL.closePool(forceClose);
		LOGGER.info("Redis client pool has been shut down.");
	}

	/**
	 * @return Redis client instance.
	 */
	public static WrappedRedisClient getRedisClient() {
		return REDIS_CLIENT_POOL.checkOutResource();
	}

	/**
	 * @param client Redis client to be returned.
	 */
	public static void returnRedisClient(final WrappedRedisClient client) {
		REDIS_CLIENT_POOL.checkInResource(client);
	}

}
