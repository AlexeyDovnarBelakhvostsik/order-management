package ru.itq.ordermanagement.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Интерфейс для связи в Redis
 */
@Repository
public interface NumberRepository extends CrudRepository<Number, String> {
}
