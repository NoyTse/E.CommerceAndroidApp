package com.example.noytse.loginfacebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.noytse.loginfacebook.model.Product;
import com.example.noytse.loginfacebook.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Iterator;

public class ProductDetails extends AppCompatActivity {
    User user;
    String key;
    Product mProduct;
    boolean mBagWasPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        mProduct = (Product)this.getIntent().getSerializableExtra("Product");
        user = getIntent().getParcelableExtra("User");
        key = getIntent().getStringExtra("Key");

        ImageView imgProdPhoto = findViewById(R.id.prodDetail_prodPhoto);
        TextView lblProdName = findViewById(R.id.prodDetail_prodName);
        TextView lblCategory = findViewById(R.id.prodDetail_category);
        TextView lblColor = findViewById(R.id.prodDetail_color);
        TextView lblExist = findViewById(R.id.prodDetail_exist);
        TextView lblSize = findViewById(R.id.prodDetail_size);
        TextView lblMaterial = findViewById(R.id.prodDetail_material);
        TextView lblPrice = findViewById(R.id.prodDetail_price);
        final Button btnPurchase = findViewById(R.id.prodDetail_btnPurchase);
        Button btnAddReview = findViewById(R.id.prodDetail_btnAddReview);
        ListView reviewListView = findViewById(R.id.prodDetail_reviewList);

        if (mProduct.getPhotoURL() != null)
            Picasso.with(this).load(mProduct.getPhotoURL()).into(imgProdPhoto);
        lblProdName.setText(mProduct.getName());
        lblCategory.setText(mProduct.getCategory());
        lblColor.setText(mProduct.getColor());
        lblExist.setText(mProduct.getAvailableInStock());
        lblSize.setText(mProduct.getSize());
        lblMaterial.setText(mProduct.getMaterial());
        lblPrice.setText(mProduct.getPrice());

        //TODO set adapter to reviewListView


        btnPurchase.setText("BUY $" + mProduct.getPrice());
        if(user != null) {
            Iterator i = user.getMyBags().iterator();
            while (i.hasNext()) {
                if (i.next().equals(key)) {
                    mBagWasPurchase = true;
                    btnPurchase.setText("On The Way To You");
                    break;
                }
            }
        }

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if(mAuth.getCurrentUser().isAnonymous()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("isFromProductDetails", true);
                    intent.putExtra("isAnonymouse", true);
                    startActivity(intent);
                }
                else{
                    if (mBagWasPurchase) {

                    } else {
                        user.getMyBags().add(key);
                        user.upgdateTotalPurchase(Integer.parseInt(mProduct.getPrice()));
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
                        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                        mBagWasPurchase = true;
                        btnPurchase.setText("On The Way To You");
                    }
                }
            }
        });
    }
}
