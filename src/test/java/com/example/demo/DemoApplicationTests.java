package com.example.demo;

import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisTest {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;

	@Test
	void testRedis() {

		User user = new User();
		user.setId(1L);
		user.setUsername("guan yi");

		redisTemplate.opsForValue().set("user:1", user);

		Object value = redisTemplate.opsForValue().get("user:1");

		System.out.println(value);
	}
}
