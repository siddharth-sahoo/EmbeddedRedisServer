package com.awesome.pro.db.redis.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import redis.clients.jedis.Jedis;

import com.awesome.pro.pool.WrappedResource;

/**
 * Wrapper class for Redis client to make it compatible with object pooling.
 * @author siddharth.s
 */
public class WrappedRedisClient implements WrappedResource<Jedis> {

	/**
	 * Wrapped Redis client.
	 */
	private final Jedis jedis;

	/**
	 * @param redisClient Redis client instance to be wrapped.
	 */
	public WrappedRedisClient(final Jedis redisClient) {
		jedis = redisClient;
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.pool.WrappedResource#close()
	 */
	@Override
	public void close() {
		jedis.close();
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.pool.WrappedResource#isClosed()
	 */
	@Override
	public boolean isClosed() {
		//FIXME: Handle with a try-catch block.
		return false;
	}

	/* (non-Javadoc)
	 * @see com.awesome.pro.pool.WrappedResource#getResource()
	 */
	@Override
	public Jedis getResource() {
		return jedis;
	}

	/**
	 * @param key Key to be set.
	 * @param value Corresponding value in pair.
	 */
	public final void storeData(String key, String value) {
		jedis.set(key, value);
	}

	public final String storeOrReplaceData(final String key, final String value) {
		return jedis.getSet(key, value);
	}

	/**
	 * @param key Key to be queried for.
	 * @return Corresponding value stored in Redis.
	 */
	public final String getData(String key) {
		return jedis.get(key);
	}

	/**
	 * @param key Name of unique set.
	 * @param data Data to be added.
	 */
	public final void storeOrAddUniqueData(final String key, final String... data) {
		jedis.sadd(key, data);
	}

	/**
	 * @param key Name of the unique set.
	 * @param data Data to be added.
	 */
	public final void storeOrAddUniqueData(final String key, final Collection<String> data) {
		final Iterator<String> iter = data.iterator();
		while (iter.hasNext()) {
			final String curr = iter.next();
			jedis.sadd(key, curr);
		}
	}

	/**
	 * @param key Name of unique set.
	 */
	public final int getUniqueCount(final String key) {
		return Integer.parseInt(jedis.scard(key).toString());
	}

	/**
	 * @param pattern Pattern to match for.
	 * @return Set of matching keys.
	 */
	public final Set<String> queryKeys(final String pattern) {
		return jedis.keys(pattern);
	}

	/**
	 * @param pattern Pattern to match keys to be deleted.
	 */
	public final void deleteKeyPatterns(final String pattern) {
		Iterator<String> iter = jedis.keys(pattern).iterator();
		while (iter.hasNext()) {
			jedis.del(iter.next());
		}
		iter = null;
	}

	/**
	 * @param keys Specific keys to be deleted.
	 */
	public final void deleteKeys(final String... keys) {
		jedis.del(keys);
	}

	/**
	 * @param key Key to be checked.
	 * @return Whether Redis contains the specified key.
	 */
	public final boolean containsKey(final String key) {
		return jedis.exists(key);
	}

}
