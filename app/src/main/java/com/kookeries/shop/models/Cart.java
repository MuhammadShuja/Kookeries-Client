package com.kookeries.shop.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static final String TAG = "Model/Cart";

    private static List<Product> items = new ArrayList<>();
    private static double shipping = 0;
    private static double subTotal = 0;
    private static double total = 0;

    public static void addItem(Product product){
        items.add(product);
        shipping += 10;
        subTotal += Integer.parseInt(product.getPrice());
        total += Integer.parseInt(product.getPrice());
    }

    public void removeItem(int index){
        items.remove(index);
    }

    public static Product getItem(int index){
        return items.get(index);
    }

    public static List<Product> getItems() {
        return items;
    }

    public static double getShipping() {
        return shipping;
    }

    public static double getSubTotal() {
        return subTotal;
    }

    public static double getTotal() {
        return total;
    }

    public static int count(){
        return items.size();
    }
}