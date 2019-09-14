package com.kookeries.shop.models;

public class Wallet {
    private static final String TAG = "Model/Wallet";

    private Double balance;

    public Wallet(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
