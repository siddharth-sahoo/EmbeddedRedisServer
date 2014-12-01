package com.awesome.pro.db.redis.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.awesome.pro.db.redis.RedisManager;
import com.awesome.pro.db.redis.map.RedisMap;

public class TestWithHashMap {
	
	private Map<String, String> redisMap;
	private Map<String, String> hashMap;
	private int count;
	
	@BeforeClass
	public void suiteSetup() {
		RedisManager.start("redis.properties");
		redisMap = new RedisMap<>();
		if (!redisMap.isEmpty())
			Assert.assertEquals("Redis map is not empty.", "Collision alert.");
		hashMap = new HashMap<>();
		count = 0;
	}
	
	//@AfterClass
	public void tearDown() {
		
	}
	
	private void insertPair() {
		count ++;
		redisMap.put(getCurrentKey(), getCurrentValue());
		hashMap.put(getCurrentKey(), getCurrentValue());
	}
	
	private String getCurrentKey() {
		return "key" + count;
	}
	
	private String getCurrentValue() {
		return "val" + count;
	}

	@Test
	public void clear() {
		insertPair();
		redisMap.clear();
		hashMap.clear();
		Assert.assertEquals(redisMap.isEmpty(), true);
	}

	@Test
	public void containsKey() {
		insertPair();
		Assert.assertEquals(redisMap.containsKey(getCurrentKey()), true);
	}

	@Test
	public void containsValue() {
		insertPair();
		Assert.assertEquals(redisMap.containsValue(getCurrentValue()), true);
	}

	@Test
	public void entrySet() {
		insertPair();
		Set<Entry<String, String>> hashMapEntries = hashMap.entrySet();
		Set<Entry<String, String>> redisMapEntries = redisMap.entrySet();
		
		Iterator<Entry<String, String>> iter = hashMapEntries.iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			Assert.assertEquals(redisMapEntries.contains(entry), true);
		}
		
		iter = redisMapEntries.iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			Assert.assertEquals(hashMapEntries.contains(entry), true);
		}
	}

	@Test
	public void get() {
		insertPair();
		Assert.assertEquals(redisMap.get(getCurrentKey()), getCurrentValue());
	}

	@Test
	public void isEmpty() {
		redisMap.clear();
		hashMap.clear();
		Assert.assertEquals(redisMap.isEmpty(), true);
	}

	@Test
	public void keySet() {
		insertPair();
		Set<String> redisKeys = redisMap.keySet();
		Set<String> hashKeys = hashMap.keySet();
		
		Iterator<String> iter = redisKeys.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Assert.assertEquals(hashKeys.contains(key), true);
		}
		
		iter = hashKeys.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Assert.assertEquals(redisKeys.contains(key), true);
		}
	}

	@Test
	public void put() {
		insertPair();
		Assert.assertEquals(redisMap.get(getCurrentKey()), getCurrentValue());
	}

	@Test
	public void putAll() {
		Map<String, String> temp = new HashMap<>();
		temp.put("randk1", "randv1");
		temp.put("randk2", "randv2");
		redisMap.putAll(temp);
		hashMap.putAll(temp);
		
		Assert.assertEquals(redisMap.containsKey("randk1"), true);
		Assert.assertEquals(redisMap.containsKey("randk2"), true);
		Assert.assertEquals(redisMap.containsValue("randv1"), true);
		Assert.assertEquals(redisMap.containsValue("randv2"), true);
	}

	@Test
	public void remove() {
		insertPair();
		redisMap.remove(getCurrentKey());
		hashMap.remove(getCurrentKey());
		Assert.assertEquals(redisMap.containsKey(getCurrentKey()), false);
	}

	@Test
	public void size() {
		insertPair();
		Assert.assertEquals(redisMap.size(), hashMap.size());
	}

	@Test
	public void values() {
		insertPair();
		Collection<String> redisValues = redisMap.values();
		Collection<String> hashValues = hashMap.values();
		
		Iterator<String> iter = redisValues.iterator();
		while (iter.hasNext()) {
			String value = iter.next();
			Assert.assertEquals(hashValues.contains(value), true);
		}
		
		iter = hashValues.iterator();
		while (iter.hasNext()) {
			String value = iter.next();
			Assert.assertEquals(redisValues.contains(value), true);
		}
	}

}
