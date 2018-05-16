package com.example.noytse.loginfacebook.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User implements Serializable, Parcelable {
    private String email;
    private float totalPurchase;
    private Map<String,String> myBags;

    public User(){}

    public User(String email, int totalPurchase, Map<String,String> myBags) {
        this.email = email;
        this.totalPurchase = totalPurchase;
        this.myBags = myBags;
    }

    public User(Parcel in) {
        this.email = in.readString();
        in.readMap(myBags,String.class.getClassLoader());
    }

    public String getEmail() {
        return email;
    }


    public void upgdateTotalPurchase(float newPurcahsePrice) {
        this.totalPurchase += newPurcahsePrice;
    }

    public Map<String,String> getMyBags() {
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
        parcel.writeMap(myBags);
    }
}
