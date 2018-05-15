package com.example.noytse.loginfacebook.model;

import java.io.Serializable;

public class ProductWithKey implements Serializable {
    private Product product;
    private boolean purchased;

    public ProductWithKey(Product product, boolean purchased, String key) {
        this.product = product;
        this.purchased = purchased;
        this.key = key;
    }


    public Product getproduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }
}
