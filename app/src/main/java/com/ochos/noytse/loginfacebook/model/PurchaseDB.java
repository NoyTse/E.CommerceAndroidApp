package com.ochos.noytse.loginfacebook.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class PurchaseDB implements Serializable {

    public PurchaseDB(/*ProductWithKey m_purchasedProduct, User m_user, */long m_time, String m_SKU, String m_orderID, String m_token, Integer m_amount) {
        //this.m_purchasedProduct = m_purchasedProduct;
       // this.m_user = m_user;
        this.m_time = m_time;
        this.m_SKU = m_SKU;
        this.m_orderID = m_orderID;
        this.m_token = m_token;
        this.m_amount = m_amount;
    }

    //ProductWithKey m_purchasedProduct;
    //User m_user;
    long m_time;
    String m_SKU;
    String m_orderID;
    String m_token;
    Integer m_amount;
}
