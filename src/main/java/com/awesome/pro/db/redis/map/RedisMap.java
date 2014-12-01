package com.awesome.pro.db.redis.map;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.awesome.pro.db.redis.client.RedisClientPool;
import com.awesome.pro.db.redis.client.WrappedRedisClient;
import com.awesome.pro.db.redis.references.RedisConfigReferences;

/**
 * Map implementation using Redis.
 * @author siddharth.s
 */
public class RedisMap<K> implements Map<K, String> {

	/**
	 * Unique ID to maintain different key spaces.
	 */
	private final String uniqueId;

	/**
	 * Constructor.
	 */
	public RedisMap() {
		uniqueId = getValidUUID();
	}

	private final String getValidUUID() {
		String temp = UUID.randomUUID().toString();
		final WrappedRedisClient client = RedisClientPool.getRedisClient();

		// Ensure there is no collision.
		while (!client.queryKeys(temp + RedisConfigReferences.QUERY_WILD_CARD)
				.isEmpty()) {
			temp = UUID.randomUUID().toString();
		}

		RedisClientPool.returnRedisClient(client);
		return temp + RedisConfigReferences.NAMESPACE_SEPARATOR;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		WrappedRedisClient client = RedisClientPool.getRedisClient();
		client.deleteKeyPatterns(uniqueId + RedisConfigReferences.QUERY_WILD_CARD);
		RedisClientPool.returnRedisClient(client);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(final Object key) {
		final WrappedRedisClient client = RedisClientPool.getRedisClient();
		final boolean result = client.containsKey(uniqueId + key);
		RedisClientPool.returnRedisClient(client);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(final Object value) {
		return values().contains(value);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<K, String>> entrySet() {
		final WrappedRedisClient client = RedisClientPool.getRedisClient();
		Set<String> keys = client.queryKeys(
				uniqueId + RedisConfigReferences.QUERY_WILD_CARD);

		Set<Map.Entry<K, String>> entries = new HashSet<>();
		if (keys == null || keys.size() == 0) {
			RedisClientPool.returnRedisClient(client);
			return entries;
		}

		final Iterator<String> iter = keys.iterator();
		while (iter.hasNext()) {
			final String key = iter.next();
			entries.add(new AbstractMap.SimpleEntry<K, String>(
					(K) key.substring(uniqueId.length()),
					client.getData(key)
					));
		}

		RedisClientPool.returnRedisClient(client);
		return entries;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public String get(final Object key) {
		final WrappedRedisClient client = RedisClientPool.getRedisClient();
		final String value = client.getData(uniqueId + key);
		RedisClientPool.returnRedisClient(client);
		return value;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		final WrappedRedisClient client = RedisClientPool.getRedisClient();
		final boolean result = client.queryKeys(
				uniqueId + RedisConfigReferences.QUERY_WILD_CARD)
				.isEmpty();
		RedisClientPool.returnRedisClient(client);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<K> keySet() {
		final WrappedRedisClient client = RedisClientPool.getRedisClient();
		Iterator<String> iter = client.queryKeys(
				uniqueId + RedisConfigReferences.QUERY_WILD_CARD)
				.iterator();
		RedisClientPool.returnRedisClient(client);
		Set<K> keys = new HashSet<>();

		while (iter.hasNext()) {
			final String key = iter.next().substring(uniqueId.length());
			keys.add((K) key);
		}

		return keys;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public String put(final K key, final String value) {
		final WrappedRedisClient client = RedisClientPool.getRedisClient();
		final String old = client.storeOrReplaceData(uniqueId + key.toString(), value);
		RedisClientPool.returnRedisClient(client);
		return old;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(final Map<? extends K, ? extends String> map) {
		final Map<K, String> addMap = (Map<K, String>) map;
		final Iterator<java.util.Map.Entry<K, String>> iter = addMap.entrySet().iterator();

		while (iter.hasNext()) {
			final java.util.Map.Entry<K, String> entry = iter.next();
			put(entry.getKey(), entry.getValue());
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public String remove(final Object key) {
		final WrappedRedisClient client = RedisClientPool.getRedisClient();

		if (!client.containsKey(uniqueId + key.toString())) {
			RedisClientPool.returnRedisClient(client);
			return null;
		}

		final String old = client.getData(uniqueId + key);
		client.deleteKeys(uniqueId + key);
		RedisClientPool.returnRedisClient(client);

		return old;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		final WrappedRedisClient client = RedisClientPool.getRedisClient();
		final int size = client.queryKeys(
				uniqueId + RedisConfigReferences.QUERY_WILD_CARD)
				.size();
		RedisClientPool.returnRedisClient(client);
		return size;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<String> values() {
		final WrappedRedisClient client = RedisClientPool.getRedisClient();
		final Set<String> keys = client.queryKeys(
				uniqueId + RedisConfigReferences.QUERY_WILD_CARD);

		final Collection<String> values = new HashSet<>();
		if (keys == null || keys.size() == 0) {
			RedisClientPool.returnRedisClient(client);
			return values;
		}

		final Iterator<String> iter = keys.iterator();

		while (iter.hasNext()) {
			values.add(client.getData(iter.next()));
		}

		return values;
	}

}
