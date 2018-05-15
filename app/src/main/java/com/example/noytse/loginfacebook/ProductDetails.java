package com.example.noytse.loginfacebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductDetails extends AppCompatActivity {
    Product mProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        mProduct = (Product)this.getIntent().getSerializableExtra("Product");

        ImageView imgProdPhoto = findViewById(R.id.prodDetail_prodPhoto);
        TextView lblProdName = findViewById(R.id.prodDetail_prodName);
        TextView lblCategory = findViewById(R.id.prodDetail_category);
        TextView lblColor = findViewById(R.id.prodDetail_color);
        TextView lblExist = findViewById(R.id.prodDetail_exist);
        TextView lblSize = findViewById(R.id.prodDetail_size);
        TextView lblMaterial = findViewById(R.id.prodDetail_material);
        TextView lblPrice = findViewById(R.id.prodDetail_price);
        Button btnPurchase = findViewById(R.id.prodDetail_btnPurchase);
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

    }
}
