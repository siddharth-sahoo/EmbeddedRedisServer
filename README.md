EmbeddedRedisServer
===================

Embdedded Redis server and pooled redis clients.
All data that may have been stored is deleted at startup and shutdown.

Sample code for initialization:

````java
RedisManager.start("redis.properties");
WrappedRedisClient client = RedisClientPool.getRedisClient();

client.storeData("key1", "val1");
client.getData("key1");

// Important to return the client back to the pool after usage.
RedisClientPool.returnRedisClient(client);
RedisManager.stop();
````

Sample configuration file: ![redis.properties](http://github.opslab.sv2.tellme.com/OnlineAutomation/EmbeddedRedisServer/raw/master/conf/redis.properties)
It doesn't have to be named redis.properties.
As long as the parameter names are same, any file could be specified during initialization.

## Redis Map
Redis implementation of Java's native Map interface.
It is dependent on the Redis server, so it is necessary that the server is started
as mentioned above before using the map.
It only stores strings as values.

Sample code:
````java
Map<String, String> map1 = new RedisMap<String>();
Map<Integer, String> map2 = new RedisMap<Integer>();

map1.put("key1", "val1");
map1.get("key1");

map2.put(1, "val1");
map2.get(1);
````
