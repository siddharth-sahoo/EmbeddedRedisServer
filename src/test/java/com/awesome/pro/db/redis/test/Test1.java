package com.awesome.pro.db.redis.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.awesome.pro.db.redis.RedisManager;
import com.awesome.pro.db.redis.map.RedisMap;

public class Test1 {

	private Map<Integer, String> map;

	@BeforeClass
	public void setup() {
		RedisManager.start("redis.properties");
		map = new RedisMap<>();
	}

	@AfterClass
	public void tearDown() {
		RedisManager.stop();
	}

	@Test(groups = { "sanity" })
	public void test1() {
		map.put(0, "qwe");
		Assert.assertEquals(map.get(0), "qwe");
	}

}
