package com.awesome.pro.db.redis.test;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.awesome.pro.db.redis.RedisManager;
import com.awesome.pro.db.redis.client.RedisClientPool;
import com.awesome.pro.db.redis.client.WrappedRedisClient;

/**
 * Basic transaction test for sanity.
 * @author siddharth.s
 */
public class SanityTest {

	/**
	 * Root logger instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(SanityTest.class);

	/**
	 * Redis client.
	 */
	private WrappedRedisClient client;

	@BeforeClass
	public void setup() {
		RedisManager.start("redis.properties");
		client = RedisClientPool.getRedisClient();
	}

	@AfterClass
	public void tearDown() {
		RedisClientPool.returnRedisClient(client);
		RedisManager.stop();
	}

	@Test(groups = { "sanity" })
	public void test1() {
		client.storeData("key1", "val1");
		Assert.assertEquals(client.getData("key1"), "val1");
		client.storeOrAddUniqueData("key2", "qwe", "asd", "asd", "qwe", "zxc");
		LOGGER.info("Unique Count: " + client.getUniqueCount("key2"));
		Assert.assertEquals(client.getUniqueCount("key2"), 3);
	}

}
