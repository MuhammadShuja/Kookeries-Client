package com.kookeries.shop.models;

public class OrderItem {
    private static final String TAG = "Model/OrderItem";
    public static OrderItem selected = null;

    private int id;
    private String thumbnail = null;
    private String name = null;
    private int quantity = 0;
    private String price = null;
    private String discount = null;

    public OrderItem(int id, String thumbnail, String name, int quantity, String price, String discount) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }
}
