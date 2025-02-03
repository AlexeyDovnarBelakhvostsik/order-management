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

    public List<OrdersDetailsEntity> getOrdersDetails() {
        return ordersDetails;
    }

    public void setOrdersDetails(List<OrdersDetailsEntity> ordersDetails) {
        this.ordersDetails = ordersDetails;
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
