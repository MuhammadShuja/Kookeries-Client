package com.kookeries.shop.models;

public class UserAddress {
    private static final String TAG = "Model/UserAddress";

    private int id;
    private String address = null;
    private boolean isDefault = false;

    public UserAddress(int id, String address, boolean isDefault) {
        this.id = id;
        this.address = address;
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
