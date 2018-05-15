package com.example.noytse.loginfacebook.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String email;
    private float totalPurchase;
    private List<String> myBags = new ArrayList<>();

    public User(){}

    public User(String email, int totalPurchase, List<String> myBags) {
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

    public List<String> getMyBags() {
        return myBags;
    }

    /*public static final Creator<User> CREATOR = new Creator<User>() {
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
        parcel.writeStringList(myBags);
    }*/
}
