package ru.itq.ordermanagement.entity;

import lombok.Data;
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
@Data
@Table(name = "orders",schema = "orders")
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

    public OrdersEntity(String orderNumber, int totalOrderAmount, LocalDate orderDate, String address, String deliveryAddress, String paymentType, String deliveryType, List<OrdersDetailsEntity> ordersDetails) {
        this.orderNumber = orderNumber;
        this.totalOrderAmount = totalOrderAmount;
        this.orderDate = orderDate;
        this.address = address;
        this.deliveryAddress = deliveryAddress;
        this.paymentType = paymentType;
        this.deliveryType = deliveryType;
        this.ordersDetails = ordersDetails;

    }

    public OrdersEntity() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setTotalOrderAmount(int totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public void setOrdersDetails(List<OrdersDetailsEntity> ordersDetails) {
        this.ordersDetails = ordersDetails;
    }

    public int getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public int getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getAddress() {
        return address;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public List<OrdersDetailsEntity> getOrdersDetails() {
        return ordersDetails;
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
