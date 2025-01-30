package ru.itq.ordermanagement.repository;


import ru.itq.ordermanagement.dto.OrdersDto;
import ru.itq.ordermanagement.dto.OrdersDtoWithoutProduct;
import ru.itq.ordermanagement.entity.OrdersEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс для доступа к методам создания прямых запросов в БД
 */
public interface OrdersRepository {
    void save(OrdersEntity ordersEntity);
    List<OrdersDto> findId(int id);
    List<OrdersDto> findAll(LocalDate localDate, int minTotal);
    List<OrdersDtoWithoutProduct> withoutProduct(LocalDate startDate, LocalDate endDate, int articleProduct);
}
