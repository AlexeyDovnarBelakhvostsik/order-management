package ru.itq.ordermanagement.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Класс для работы с Redis
 */
@Service
public class NumberServiceImpl implements NumberService {

    private final RedisTemplate<String, String> redisTemplate;

    public NumberServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Генерация уникального ключа
    private String generateKey() {
        return UUID.randomUUID().toString();
    }

    // Сохранение номера в Redis
    public void saveNumber(String number) {
        String key = generateKey();
        redisTemplate.opsForValue().set(key, number);
    }

    // Получение номера по ключу
    public String getNumber(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
