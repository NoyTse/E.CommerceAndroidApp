package com.ochos.noytse.loginfacebook.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User implements Serializable, Parcelable {
    public void setEmail(String email) {
        this.email = email;
    }

    public float getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(float totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public void setMyBags(ArrayList<Integer> myBags) {
        this.myBags = myBags;
    }

    private String email;
    private float totalPurchase;
    private ArrayList<Integer> myBags;

    public User(){}

    public User(String email, float totalPurchase, ArrayList<Integer> myBags) {
        this.email = email;
        this.totalPurchase = totalPurchase;
        this.myBags = myBags;
    }

    public User(Parcel in) {
        this.email = in.readString();
        in.readList(myBags,String.class.getClassLoader());
    }

    public String getEmail() {
        return email;
    }


    public void upgdateTotalPurchase(float newPurcahsePrice) {
        this.totalPurchase += newPurcahsePrice;
    }

    public ArrayList<Integer> getMyBags() {
        return myBags;
    }


    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeFloat(totalPurchase);
        parcel.writeList(myBags);
    }
}
