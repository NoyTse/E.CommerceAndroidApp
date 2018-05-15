package com.example.noytse.loginfacebook;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductDetails extends AppCompatActivity {
    Product mProduct;
    private ListView mReviewListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        mProduct = (Product)this.getIntent().getSerializableExtra("Product");
        //asds
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
        mReviewListView = findViewById(R.id.prodDetail_reviewList);

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
        mReviewListView.setAdapter(new ReviewAdapter(mProduct.getReviewList(),this));

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReviewFormDialog();
            }
        });
    }

    private void showReviewFormDialog() {
        final Dialog addReviewDialog = new Dialog(this);
        addReviewDialog.setContentView(R.layout.review_add_form);
        addReviewDialog.findViewById(R.id.addReview_btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtReviewContent = addReviewDialog.findViewById(R.id.addReview_txtContent);
                if (txtReviewContent.getText().toString().trim().length() > 0) {
                    addReviewToStorage(txtReviewContent.getText().toString().trim());
                    addReviewToList(txtReviewContent.getText().toString().trim());
                    mReviewListView.setAdapter(new ReviewAdapter(mProduct.getReviewList(),getApplicationContext()));
                }
            }
        });
    }

    private void addReviewToList(String reviewContent) {
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if (userName != null){
            mProduct.getReviewList().add(new Review(userName,reviewContent));

        }
    }

    private void addReviewToStorage(String reviewContent) {
        //TODO
        //here should be the code that save the review to firbase storage (dont forget to add the userID from firbaseUser)

    }

}
