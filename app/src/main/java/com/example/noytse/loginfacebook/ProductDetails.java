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
import android.widget.Toast;

import com.example.noytse.loginfacebook.model.Product;
import com.example.noytse.loginfacebook.model.ProductWithKey;
import com.example.noytse.loginfacebook.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProductDetails extends AppCompatActivity {
    User user;
    String key;
    ProductWithKey mProduct;
    boolean mBagWasPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        mProduct = (ProductWithKey) this.getIntent().getSerializableExtra("Product");
        String userMail = this.getIntent().getStringExtra("user_email");
        float total = this.getIntent().getFloatExtra("user_total",0);
        ArrayList<Integer> purchased = (ArrayList<Integer>)this.getIntent().getSerializableExtra("user_ParchesedList");
        user = new User(userMail,total,purchased);

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

        Product prodDetails = mProduct.getproduct();
        if (prodDetails.getPhotoURL() != null)
            Picasso.with(this).load(prodDetails.getPhotoURL()).into(imgProdPhoto);
        lblProdName.setText(prodDetails.getName());
        lblCategory.setText(prodDetails.getCategory());
        lblColor.setText(prodDetails.getColor());
        lblExist.setText(prodDetails.getAvailableInStock());
        lblSize.setText(prodDetails.getSize());
        lblMaterial.setText(prodDetails.getMaterial());
        lblPrice.setText(prodDetails.getPrice());

        btnPurchase.setEnabled(!mProduct.isPurchased());
        //TODO set adapter to reviewListView

        /*if(user != null) {
            Iterator i = user.getMyBags().iterator();
            while (i.hasNext()) {
                if (i.next().equals(key)) {
                    mProduct.setPurchased(true);
                    btnPurchase.setEnabled(false);
                    Toast.makeText(this,"On The Way To You",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }*/

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
                    if (!mProduct.isPurchased()) {
                        mProduct.setPurchased(true);
                        user.upgdateTotalPurchase(Float.parseFloat(mProduct.getproduct().getPrice().replace("$","")));
                        user.getMyBags().add(Integer.parseInt(mProduct.getKey()));
                        //Save in db
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
                        Map<String,Object> updateUser = new HashMap<>();
                        updateUser.put(mAuth.getUid(),user);
                        userRef.updateChildren(updateUser);

                        btnPurchase.setEnabled(false);
                    }
                }
            }
        });
    }
}
