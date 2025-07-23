/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.autoconfigure.redisson;

import org.junit.jupiter.api.Test;
import org.redisson.client.DefaultNettyHook;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.connection.SequentialDnsAddressResolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@SpringJUnitConfig
@TestPropertySource(properties = { "spring.redisson.config.single-server-config.address=redis://livk.com:6379",
		"spring.redisson.config.codec=!<org.redisson.codec.JsonJacksonCodec> {}" })
class RedissonPropertiesTests {

	@Autowired
	ConfigurableEnvironment environment;

	@Test
	void test() {
		RedissonProperties properties = RedissonProperties.load(environment);
		assertThat(properties.getConfig().useSingleServer().getAddress()).isEqualTo("redis://livk.com:6379");
		assertThat(properties.getConfig().getCodec()).isInstanceOf(JsonJacksonCodec.class);
		assertThat(properties.getConfig().getNettyHook()).isInstanceOf(DefaultNettyHook.class);
		assertThat(properties.getConfig().getAddressResolverGroupFactory())
			.isInstanceOf(SequentialDnsAddressResolverFactory.class);
	}

}
