package com.example.noytse.loginfacebook.model;

public class ProductWithKey {
    private Product bag;

    public ProductWithKey(Product bag, String key) {
        this.bag = bag;
        this.key = key;
    }

    public Product getBag() {
        return bag;
    }

    public void setBag(Product bag) {
        this.bag = bag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;
}
