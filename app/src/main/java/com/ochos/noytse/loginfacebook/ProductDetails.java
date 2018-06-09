package com.ochos.noytse.loginfacebook;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.billingclient.api.BillingClient.BillingResponse;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.ochos.noytse.loginfacebook.Billing.BillingManager;
import com.ochos.noytse.loginfacebook.analytics.AnalyticsManager;
import com.ochos.noytse.loginfacebook.model.Product;
import com.ochos.noytse.loginfacebook.model.ProductWithKey;
import com.ochos.noytse.loginfacebook.model.PurchaseDB;
import com.ochos.noytse.loginfacebook.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProductDetails extends AppCompatActivity implements BillingManager.BillingUpdatesListener {
    public final static String TAG = "ProductDetails";
    User user;
    String key;
    ProductWithKey mProduct;
    boolean mBagWasPurchase;
    public static Date purchaseTime;
    private BillingManager mBillingManager;
    public final static String _BAG = "bag1";
    public final static String _TOWEL = "towel1";
    public final static String _SHOE = "shoe1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        mBillingManager = new BillingManager(this,this);

        mProduct = (ProductWithKey) this.getIntent().getSerializableExtra("Product");
        if(this.getIntent().hasExtra("Product")){
//            Product productFromService = (Product) this.getIntent().getSerializableExtra("FromService");
//            if(productFromService != null){
//                mProduct.setProduct(productFromService);
//            }
        }
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
        final Button btnAddReview = findViewById(R.id.prodDetail_btnAddReview);
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

        btnAddReview.setVisibility(mProduct.isPurchased()? View.VISIBLE : View.INVISIBLE);
        btnPurchase.setVisibility(mProduct.isPurchased()? View.INVISIBLE : View.VISIBLE);

        //set adapter to reviewListView
        List<Review> reviewsForCurrProduct =  mProduct.getproduct().getReviewList();
        if (reviewsForCurrProduct != null)
            reviewListView.setAdapter(new ReviewAdapter(reviewsForCurrProduct,this));


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
                        purchaseTime = new Date();
                        AnalyticsManager.getInstance().trackPurchase(mProduct.getproduct());
                        String billingType = "";
                        switch(mProduct.getproduct().getCategory()){
                            case "bags":{
                                billingType = _BAG;
                                break;
                            }
                            case "towels":{
                                billingType = _TOWEL;
                                break;
                            }
                            case "shoes":{
                                billingType = _SHOE;
                                break;
                            }
                        }
                        String sku = BillingClient.SkuType.INAPP;
                        mBillingManager.initiatePurchaseFlow(billingType,sku);
                    }
                }
            }
        });

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddReviewDialog();
            }
        });

    }

    private void showAddReviewDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.review_add_form);
        dialog.setTitle("Add Review");
        final EditText txtReviewContent = dialog.findViewById(R.id.addReview_txtContent);
        Button btnAdd = dialog.findViewById(R.id.addReview_btnSend);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reviewContent = txtReviewContent.getText().toString().trim();
                if (reviewContent.length() > 0) {
                    mProduct.getproduct().getReviewList().add(new Review(user.getEmail(), txtReviewContent.getText().toString().trim()));
                    DatabaseReference productListDB = FirebaseDatabase.getInstance().getReference("products");
                    Map<String,Object> updatedProductForSavingInDB = new HashMap<String, Object>();
                    updatedProductForSavingInDB.put(mProduct.getKey(),mProduct.getproduct());
                    productListDB.updateChildren(updatedProductForSavingInDB);
                    AnalyticsManager.getInstance().trackTimeBetweenPurchaseAndReview();
                }

                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @Override
    public void onUserLeaveHint() {
        AnalyticsManager.getInstance().trackTimeInsideTheApp();
    }

    @Override
    public void onBillingClientSetupFinished() {

    }

    @Override
    public void onConsumeFinished(String token, int result) {

    }

    @Override
    public void onPurchasesUpdated(int resultCode, List<Purchase> purchases) {
        Log.e(TAG,"onPurchasesUpdated() >> ");
        final Button btnPurchase = findViewById(R.id.prodDetail_btnPurchase);
        final Button btnAddReview = findViewById(R.id.prodDetail_btnAddReview);

        if (resultCode != BillingResponse.OK) {
            Log.e(TAG,"onPurchasesUpdated() << Error:"+resultCode);
            return;
        }

        for (Purchase purchase : purchases) {
            Log.e(TAG, "onPurchasesUpdated() >> " + purchase.toString());

            // displayMessage("onPurchasesUpdated() >> " + purchase.getSku());

                Log.e(TAG, "onPurchasesUpdated() >> consuming " + purchase.getSku());
                //Only consume  one time product (subscription can't be consumed).
                mBillingManager.consumeAsync(purchase.getPurchaseToken());

            //Update the server...
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mProduct.setPurchased(true);
            user.upgdateTotalPurchase(Float.parseFloat(mProduct.getproduct().getPrice().replace("$","")));
            if (user.getMyBags() == null)
                user.setMyBags(new ArrayList<Integer>());
            user.getMyBags().add(Integer.parseInt(mProduct.getKey()));
            //Save in db
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
            Map<String,Object> updateUser = new HashMap<>();
            updateUser.put(mAuth.getUid(),user);
            userRef.updateChildren(updateUser);
            btnPurchase.setEnabled(false);
            btnPurchase.setText("Purchased! :)");
            btnAddReview.setVisibility(View.VISIBLE);

            DatabaseReference purchaseRef = FirebaseDatabase.getInstance().getReference("Purchases");
            PurchaseDB purchaseDB = new PurchaseDB(/*mProduct,user, */purchase.getPurchaseTime(),mProduct.getproduct().getCategory(),purchase.getOrderId(),purchase.getPurchaseToken(),1);
            purchaseRef.setValue(purchaseDB);
        }

        Log.e(TAG,"onPurchasesUpdated() <<");
    }
}
