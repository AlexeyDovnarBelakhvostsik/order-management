package ru.itq.ordermanagement.service;

import ru.itq.ordermanagement.dto.OrdersDto;
import ru.itq.ordermanagement.dto.OrdersDtoWithoutProduct;
import ru.itq.ordermanagement.entity.OrdersEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс для доступа к методам для проведения дополнительных преобразований с поступившими данными
 */
public interface OrdersService {

    void save(OrdersEntity ordersEntity);
    List<OrdersDto> findId(int id);
    List<OrdersDto> findAll(LocalDate localDate, int minTotal);
    List<OrdersDtoWithoutProduct> withoutProduct(LocalDate startDate, LocalDate endDate,int articleProduct);
}
