package com.example.noytse.loginfacebook.model;

import com.example.noytse.loginfacebook.Review;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NoyTse on 14/05/2018.
 */

public class Product implements Serializable {
    private String name;
    private String category;
    private String color;
    private String availableInStock;
    private String size;
    private String material;
    private String photoURL;
    private String price;
    private ArrayList<Review> reviewList = new ArrayList<>();

    public Product(){}
    public Product(String name, String category, String color, String availableInStock, String size, String material, String photoURL, String price, ArrayList<Review> reviewList) {
        this.name = name;
        this.category = category;
        this.color = color;
        this.availableInStock = availableInStock;
        this.size = size;
        this.material = material;
        this.photoURL = photoURL;
        this.price = price;
        this.reviewList = reviewList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAvailableInStock() {
        return availableInStock;
    }

    public void setAvailableInStock(String availableInStock) {
        this.availableInStock = availableInStock;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
    }
}
