package com.example.noytse.loginfacebook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductListActivity extends AppCompatActivity {
    private enum eSort {
        NAME,
        PRICE
    };
    private List<Product> mProductList;
    private ListView mListView;
    private boolean mSearchVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        mProductList = getProductList();
        mListView = findViewById(R.id.listView_Products);
        mListView.setAdapter(new ProductsAdapter(mProductList,this));

        //Search bar visibilty handling
        findViewById(R.id.viewSearchLayout).setVisibility(mSearchVisible ? View.VISIBLE : View.GONE);
        findViewById(R.id.productList_btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO if (!mSearchVisible) => adapter filter = none
                mSearchVisible = !mSearchVisible;
                findViewById(R.id.viewSearchLayout).setVisibility(mSearchVisible ? View.VISIBLE : View.GONE);
            }
        });

        EditText txtSearch = findViewById(R.id.txtSearch);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterList(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        findViewById(R.id.productList_btnSort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortDialog();
            }
        });
    }

    private void showSortDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(R.string.sort_message)
                .setPositiveButton(R.string.sort_nameField, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mProductList = getSortedListFromFirebase(eSort.NAME);
                        mListView.setAdapter(new ProductsAdapter(mProductList,getBaseContext()));
                    }
                })
                .setNegativeButton(R.string.sort_priceField, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mProductList = getSortedListFromFirebase(eSort.PRICE);
                mListView.setAdapter(new ProductsAdapter(mProductList,getBaseContext()));
            }
        });
        dialogBuilder.create().show();
    }

    private List<Product> getSortedListFromFirebase(eSort orderBy) {
        //TODO
        //parameter orderBy (eSort.NAME     OR    eSort.PRICE)
        return mProductList;
    }

    private void filterList(final CharSequence searchString) {
        List<Product> filteredList = new ArrayList<>();
        for (Product prod : mProductList)
            if (prod.getName().contains(searchString) || prod.getCategory().contains(searchString)
                    || prod.getPrice().contains(searchString))
                filteredList.add(prod);
        mListView.setAdapter(new ProductsAdapter(filteredList,this));
    }

    private List<Product> getProductList() {
        //TODO here should be the code that fetch the data from the firebase storeage

        //temp implementation       DELETE WHEN IMPLEMENT
        List<Product> resList = new ArrayList<>();
        resList.add(new Product("p1","bags","blue","yes","150x50 cm","Meshi"
                ,null,"15$"));

        resList.add(new Product("p2","bags","red","yes","150x50 cm","Meshi"
                ,null,"35$"));

        resList.add(new Product("a3","Shoes","blue","yes","150x50 cm","Meshi"
                ,null,"135$"));
        return  resList;
    }
}
