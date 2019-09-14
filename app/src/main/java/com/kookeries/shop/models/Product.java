package com.kookeries.shop.models;

import com.kookeries.shop.api.API;
import com.kookeries.shop.api.config.ApiResponse;

import org.json.JSONObject;

import java.util.List;

public class Product {
    private static final String TAG = "Model/Product";
    public static Product selected = null;

    private int id;
    private Category category;
    private String thumbnail = null;
    private String name = null;
    private String price = null;
    private String oldPrice = null;
    private String discount = null;
    private String earning = null;
    private String quantity = null;
    private String expiry = null;
    private String location = null;
    private String description = null;
    private List<ProductImage> images = null;

    /*
    ------------------------------------
    |   Product Instances
    ------------------------------------
    */

    public Product(){
    }

    public static Product Saleable(Category category, String name, String price, String earning, String quantity, String expiry, String location, String description, List<ProductImage> images) {
        Product product = new Product();
        product.setCategory(category);
        product.setName(name);
        product.setPrice(price);
        product.setEarning(earning);
        product.setQuantity(quantity);
        product.setExpiry(expiry);
        product.setLocation(location);
        product.setDescription(description);
        product.setImages(images);

        return product;
    }

    public static Product Buyable(int id, int category_id, String thumbnail, String name, String price, String quantity, String expiry, String location, String description, List<ProductImage> images) {
        Product product = new Product();
        product.setId(id);
        product.setCategoryById(category_id);
        product.setThumbnail(thumbnail);
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setExpiry(expiry);
        product.setLocation(location);
        product.setDescription(description);
        product.setImages(images);

        return product;
    }

    /*
    ------------------------------------
    |   Product Info
    ------------------------------------
    */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getEarning() {
        return earning;
    }

    public void setEarning(String earning) {
        this.earning = earning;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*
    ------------------------------------
    |   Product Images
    ------------------------------------
    */

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    /*
    ------------------------------------
    |   Product Category
    ------------------------------------
    */

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCategoryById(final int id){
        API.getCategories(new ApiResponse.CatalogListener<Category>() {
            @Override
            public void onSuccess(List<Category> data) {
                for(Category category : data){
                    if(category.getId() == id){
                        setCategory(category);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(JSONObject response) {

            }

            @Override
            public void onException(Exception e) {

            }
        });
    }
}