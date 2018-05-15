package com.example.noytse.loginfacebook;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.noytse.loginfacebook.model.Product;
import com.example.noytse.loginfacebook.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by NoyTse on 14/05/2018.
 */

public class ProductsAdapter extends BaseAdapter {
    private List<Product> mProductList;
    private Context mContext;
    private User user;

    public ProductsAdapter(List<Product> mProductList, Context mContext, User user) {
        this.mProductList = mProductList;
        this.mContext = mContext;
        this.user = user;
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int i) {
        return mProductList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_element,viewGroup,false);

        TextView txtProductName = view.findViewById(R.id.lblProdName);
        TextView txtCategory = view.findViewById(R.id.lblProdCategory);
        TextView txtPrice = view.findViewById(R.id.lblProdPrice);
        ImageView imgProductPhoto = view.findViewById(R.id.imgProductPhoto);


        final Product currentProduct = mProductList.get(i);
        txtProductName.setText(currentProduct.getName());
        txtCategory.setText(currentProduct.getCategory());
        txtPrice.setText(currentProduct.getPrice());
        if (currentProduct.getPhotoURL() != null)
            Picasso.with(mContext).load(currentProduct.getPhotoURL()).into(imgProductPhoto);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prodDetailIntent = new Intent(mContext,ProductDetails.class);
                prodDetailIntent.putExtra("Product",currentProduct);
                prodDetailIntent.putExtra("user", user);
                mContext.startActivity(prodDetailIntent);
            }
        });

        return view;
    }
}
