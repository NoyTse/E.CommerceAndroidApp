package com.example.noytse.loginfacebook;

/**
 * Created by NoyTse on 15/05/2018.
 */

public class Review {
    private String userName;
    private String reviewTxt;

    public Review(String userName, String reviewTxt) {
        this.userName = userName;
        this.reviewTxt = reviewTxt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReviewTxt() {
        return reviewTxt;
    }

    public void setReviewTxt(String reviewTxt) {
        this.reviewTxt = reviewTxt;
    }
}
