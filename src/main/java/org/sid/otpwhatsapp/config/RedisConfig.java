package org.sid.otpwhatsapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    /**
     * Defines a RedisCacheManager bean for Spring caching abstraction.
     * Configures cache entries with a 2-minute TTL, disables caching of null values,
     * and serializes values as JSON using Jackson2JsonRedisSerializer.
     * This ensures efficient, readable, and consistent cache storage in Redis.
     */

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(2))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class)));
        return RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    /**
     * En appelant setConnectionFactory, on associe le RedisTemplate à cette connexion Redis. Sans cette ligne, le template ne saurait pas où se connecter.
     * Par défaut, si on ne configure pas, RedisTemplate utiliserait un sérialiseur JDK binaire pour la clé, ce qui conduit à stocker des clés au format binaire illisible.
     * En choisissant StringRedisSerializer, on convertit/décode les clés en UTF-8, stockées en clair dans Redis.
     * on choisit de sérialiser les valeurs (les objets Java) en JSON en utilisant Jackson, via Jackson2JsonRedisSerializer<Object>.
     *
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return template;
    }
}
