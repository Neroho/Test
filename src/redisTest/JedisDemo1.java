package redisTest;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisDemo1 {

	@Test
	public void demo1(){
		Jedis jedis = new Jedis("192.168.217.130",6379);
		jedis.set("jdbc", "jedis insert success");
		String value = jedis.get("jdbc");
		System.out.println(value);
		jedis.close();
	}
	
	
	@Test
	public void demo2(){
		JedisPoolConfig	config = new JedisPoolConfig();
		//最大连接数
		config.setMaxTotal(30);
		//最大空闲连接数
		config.setMaxIdle(10);
		
		JedisPool pool = new JedisPool(config,"192.168.217.130",6379);
		Jedis jedis=null;
		
		try {
			jedis = pool.getResource();
			jedis.set("name", "张三李四王五");
			String value = jedis.get("name");
			System.out.println(value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				jedis.close();
			}
			if(pool != null){
				pool.close();
			}
		}
		
	}
}
