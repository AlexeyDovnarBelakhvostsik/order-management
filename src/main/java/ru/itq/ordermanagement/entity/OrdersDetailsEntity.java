package ru.itq.ordermanagement.entity;


import lombok.Getter;
import lombok.Setter;

/**
 * Класс основной, является прямым отражение таблицы в БД orderdetails,
 * используется для запросов create.
 * Отсутствует конструктор по умолчанию, создан только один с конкретными аргументами
 * Геттеры и Сеттеры созданы библиотекой Lombok
 * toString переопределен в этом же классе стандартным методом
 */

@Getter
@Setter
public class OrdersDetailsEntity {
    private int id;
    private  int articleProduct;
    private String nameProduct;
    private int quantity;
    private int unitCost;
    private int idOrders;

    public OrdersDetailsEntity(int articleProduct, String nameProduct, int quantity, int unitCost) {
        this.articleProduct = articleProduct;
        this.nameProduct = nameProduct;
        this.quantity = quantity;
        this.unitCost = unitCost;
    }

    @Override
    public String toString() {
        return "OrdersDetails{" +
                "id=" + id +
                ", articleProduct=" + articleProduct +
                ", nameProduct='" + nameProduct + '\'' +
                ", quantity=" + quantity +
                ", unitCost=" + unitCost +
                ", idOrders=" + idOrders +
                '}';
    }
}
