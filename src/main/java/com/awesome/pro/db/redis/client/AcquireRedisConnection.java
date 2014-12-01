package com.awesome.pro.db.redis.client;

import redis.clients.jedis.Jedis;

import com.awesome.pro.db.redis.references.RedisConfigReferences;
import com.awesome.pro.pool.AcquireResource;

/**
 * Specifies how to acquire a new Redis client connection.
 * @author siddharth.s
 */
public class AcquireRedisConnection implements AcquireResource<WrappedRedisClient> {

	// Constructor.
	public AcquireRedisConnection() { }

	/* (non-Javadoc)
	 * @see com.awesome.pro.pool.AcquireResource#acquireResource()
	 */
	@Override
	public WrappedRedisClient acquireResource() {
		return new WrappedRedisClient(new Jedis(
				RedisConfigReferences.REDIS_HOST,
				RedisConfigReferences.getRedisPort(null)));
	}

}
