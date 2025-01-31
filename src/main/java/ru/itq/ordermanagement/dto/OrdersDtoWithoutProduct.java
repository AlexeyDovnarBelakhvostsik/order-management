package ru.itq.ordermanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Класс DTO паттерна, хранит поля для вывода данных из БД, в соответствии с переданным запросом
 * используется для запросов withoutProduct.
 * Геттеры и Сеттеры созданы библиотекой Lombok
 * toString переопределен в этом же классе стандартным методом
 */

@Setter
@Getter
public class OrdersDtoWithoutProduct {
    private int id;
    private String orderNumber;
    private int totalOrderAmount;
    private LocalDate orderDate;
    private String address;
    private String deliveryAddress;
    private String paymentType;
    private String deliveryType;

    public OrdersDtoWithoutProduct(int id, String orderNumber, int totalOrderAmount, LocalDate orderDate, String address, String deliveryAddress, String paymentType, String deliveryType) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.totalOrderAmount = totalOrderAmount;
        this.orderDate = orderDate;
        this.address = address;
        this.deliveryAddress = deliveryAddress;
        this.paymentType = paymentType;
        this.deliveryType = deliveryType;
    }

    public OrdersDtoWithoutProduct(int id, String orderNumber, LocalDate orderDate) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "OrdersDtoWithoutProduct{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", totalOrderAmount=" + totalOrderAmount +
                ", orderDate=" + orderDate +
                ", address='" + address + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", deliveryType='" + deliveryType + '\'' +
                '}';
    }
}
