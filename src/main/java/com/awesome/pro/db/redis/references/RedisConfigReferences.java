package com.awesome.pro.db.redis.references;

import com.awesome.pro.utilities.PropertyFileUtility;

/**
 * References related to the embdedded Redis server.
 * @author siddharth.s
 */
public final class RedisConfigReferences {

	// Redis Server Configuration Parameters
	public static final String PARAMETER_REDIS_PORT = "RedisPort";

	// Default Redis Server Configuration
	public static final int DEFAULT_REDIS_PORT = 6379;

	// Redis Server Configurations
	public static final String REDIS_HOST = "localhost";
	private static int REDIS_PORT = -1;

	public static final String NAMESPACE_SEPARATOR = "||";
	public static final char QUERY_WILD_CARD = '*';

	/**
	 * @param Redis configuration file.
	 * @return Redis port configured in property file.
	 */
	public static final int getRedisPort(final String configFile) {
		if (configFile == null && REDIS_PORT == -1) {
			throw new IllegalStateException("Redis port is not configured. "
					+ "This may be because you are starting the client first.");
		}
		if (REDIS_PORT == -1) {
			PropertyFileUtility config = new PropertyFileUtility(configFile);
			REDIS_PORT = config.getIntegerValue(PARAMETER_REDIS_PORT,
					DEFAULT_REDIS_PORT);
		}
		return REDIS_PORT;
	}

	/**
	 * Private constructor.
	 */
	private RedisConfigReferences() { }

}
