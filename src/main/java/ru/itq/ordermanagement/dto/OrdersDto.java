package ru.itq.ordermanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


/**
 * Класс DTO паттерна, хранит поля для вывода данных из БД, в соответствии с переданным запросом
 * используется для запросов findALL и find.
 * Геттеры и Сеттеры созданы библиотекой Lombok
 * toString переопределен в этом же классе стандартным методом
 */

public class OrdersDto {
    private int id;
    private String orderNumber;
    private int totalOrderAmount;
    private LocalDate orderDate;
    private String address;
    private String deliveryAddress;
    private String paymentType;
    private String deliveryType;


    public OrdersDto(int id, String orderNumber, int totalOrderAmount, LocalDate orderDate, String address, String deliveryAddress, String paymentType, String deliveryType) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.totalOrderAmount = totalOrderAmount;
        this.orderDate = orderDate;
        this.address = address;
        this.deliveryAddress = deliveryAddress;
        this.paymentType = paymentType;
        this.deliveryType = deliveryType;
    }

    public OrdersDto(int id, String orderNumber, int totalOrderAmount, LocalDate orderDate) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.totalOrderAmount = totalOrderAmount;
        this.orderDate = orderDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(int totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    @Override
    public String toString() {
        return "OrdersDto{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", totalOrderAmount='" + totalOrderAmount + '\'' +
                ", orderDate=" + orderDate +
                ", address='" + address + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", deliveryType='" + deliveryType + '\'' +
                '}';
    }
}
