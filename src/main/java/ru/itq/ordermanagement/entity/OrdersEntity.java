package ru.itq.ordermanagement.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;

/**
 * Класс основной, является прямым отражение таблицы в БД orders,
 * используется для запросов create.
 * Геттеры и Сеттеры созданы библиотекой Lombok(Lombok иногда отваливается, поэтому пока по старинке созданы)
 * toString переопределен в этом же классе стандартным методом
 */
@Setter
@Getter
@Data
@Table(name = "orders", schema = "orders")
public class OrdersEntity {
    @Id
    private int id;
    private String orderNumber;
    private int totalOrderAmount;
    private LocalDate orderDate;
    private String address;
    private String deliveryAddress;
    private String paymentType;
    private String deliveryType;
    private List<OrdersDetailsEntity> ordersDetails;

    public OrdersEntity() {
    }

    @Override
    public String toString() {
        return "OrdersEntity{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", totalOrderAmount=" + totalOrderAmount +
                ", orderDate=" + orderDate +
                ", address='" + address + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", deliveryType='" + deliveryType + '\'' +
                ", ordersDetails=" + ordersDetails +
                '}';
    }
}
