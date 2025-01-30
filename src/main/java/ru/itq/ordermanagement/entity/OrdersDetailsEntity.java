package ru.itq.ordermanagement.entity;


import lombok.Setter;

/**
 * Класс основной, является прямым отражение таблицы в БД orderdetails,
 * используется для запросов create.
 * Отсутствует конструктор по умолчанию, создан только один с конкретными аргументами
 * Геттеры и Сеттеры созданы библиотекой Lombok
 * toString переопределен в этом же классе стандартным методом
 */

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

    public void setId(int id) {
        this.id = id;
    }

    public void setArticleProduct(int articleProduct) {
        this.articleProduct = articleProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitCost(int unitCost) {
        this.unitCost = unitCost;
    }

    public void setIdOrders(int idOrders) {
        this.idOrders = idOrders;
    }

    public int getId() {
        return id;
    }

    public int getArticleProduct() {
        return articleProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitCost() {
        return unitCost;
    }

    public int getIdOrders() {
        return idOrders;
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
