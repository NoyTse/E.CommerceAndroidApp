package com.example.noytse.loginfacebook;

import android.app.Activity;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.noytse.loginfacebook.model.Product;
import com.example.noytse.loginfacebook.model.ProductWithKey;
import com.google.android.gms.common.util.ProcessUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsDatabase{
    List<ProductWithKey> m_productsList = new ArrayList<>();
    private final String TAG = "Database";

    public  ProductsDatabase(){

        DatabaseReference productsReference = FirebaseDatabase.getInstance().getReference("products");

        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                updateProductsList(snapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled(Products) >>" + databaseError.getMessage());
            }
        });

    }
    private void updateProductsList(DataSnapshot snapshot) {

        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            Product product = dataSnapshot.getValue(Product.class);
            Log.e(TAG, "updateProductList >> adding product: " + product.getName());
            String key = dataSnapshot.getKey();
            m_productsList.add(new ProductWithKey(product,key));
        }
        //recyclerView.getAdapter().notifyDataSetChanged();


       // recyclerView.getAdapter().notifyDataSetChanged();

    }
}
