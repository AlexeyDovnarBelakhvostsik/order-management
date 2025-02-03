package ru.itq.ordermanagement.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.itq.ordermanagement.dto.OrdersDto;
import ru.itq.ordermanagement.dto.OrdersDtoWithoutProduct;
import ru.itq.ordermanagement.entity.OrdersEntity;
import ru.itq.ordermanagement.service.NumberService;
import ru.itq.ordermanagement.service.OrdersService;

import java.time.LocalDate;
import java.util.List;

/**
 * Класс Контроллер, хранит входные точки для запросов
 */
@RestController
@RequestMapping(value = "/orders")
public class OrdersController {

    private final OrdersService ordersService;

    private final NumberService numberService;

    public OrdersController(OrdersService ordersService, NumberService numberService) {
        this.ordersService = ordersService;
        this.numberService = numberService;
    }


    /**
     * Метод для создания заказа
     */
    @PostMapping("/create")
    public String create(@RequestBody OrdersEntity ordersEntity) {

        ordersService.save(ordersEntity);
        return "Success";
    }

    /**
     * Метод для взятия из БД заказов по переданной в парамметры Id
     */
    @GetMapping("/find/{id}")
    public List<OrdersDto> findID(@PathVariable int id) {

        return ordersService.findId(id);
    }

    /**
     * Метод для взятия из БД всех заказов по указанной в параметрах дате и минимальной общей сумме
     */
    @GetMapping("/findAll")
    public List<OrdersDto> findAll(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date,
                                   @RequestParam int minTotal) {

        return ordersService.findAll(date, minTotal);
    }

    /**
     * Метод для взятия из БД заказов за указанный в параметрах срок, но без товаров с указанным артиклем
     */
    @GetMapping("/without-product")
    public List<OrdersDtoWithoutProduct> withoutProduct(@RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
                                                        @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate,
                                                        @RequestParam int articleProduct) {

        return ordersService.withoutProduct(startDate, endDate, articleProduct);
    }
    /**
     * Метод для взятия из Redis номер заказа по ключу(адрес доставки)
     */
    @GetMapping("/{key}")

    public ResponseEntity<String> getNumber(@PathVariable String key) {
        return ResponseEntity.ok(numberService.getNumber(key));
    }
}
