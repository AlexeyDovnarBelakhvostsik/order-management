package ru.itq.ordermanagement.service;


import org.springframework.stereotype.Component;
import ru.itq.ordermanagement.dto.OrdersDto;
import ru.itq.ordermanagement.dto.OrdersDtoWithoutProduct;
import ru.itq.ordermanagement.entity.OrdersEntity;
import ru.itq.ordermanagement.repository.OrdersRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс для реализации методов с поступившими данными
 * в данном случае реализован функционал для назначения полб orderDate локальной даты и
 * реализовано создание неповторимого идентификационного номера
 */
@Component
public class OrderServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;

    public OrderServiceImpl(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    public void save(OrdersEntity ordersEntity) {
        ordersEntity.setOrderDate(LocalDate.now());

        int randomNumber = ThreadLocalRandom.current().nextInt(0, 99999);

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyyMMdd");
        String localDate = LocalDate.now().format(formatters);

        String orderNumber = randomNumber + localDate;
        ordersEntity.setOrderNumber(orderNumber);

        ordersRepository.save(ordersEntity);
    }

    @Override
    public List<OrdersDto> findId(int id) {
        return ordersRepository.findId(id);
    }

    @Override
    public List<OrdersDto> findAll(LocalDate localDate, int minTotal) {
        return ordersRepository.findAll(localDate, minTotal);
    }

    @Override
    public List<OrdersDtoWithoutProduct> withoutProduct(LocalDate startDate, LocalDate endDate, int articleProduct) {
        return ordersRepository.withoutProduct(startDate, endDate, articleProduct);
    }


}
