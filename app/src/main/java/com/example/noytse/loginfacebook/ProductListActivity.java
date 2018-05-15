package com.example.noytse.loginfacebook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.example.noytse.loginfacebook.model.Product;
import com.example.noytse.loginfacebook.model.ProductWithKey;
import com.example.noytse.loginfacebook.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProductListActivity extends AppCompatActivity {
    private FirebaseUser mFirebaseUser;
    private User myUser;

    private enum eSort {
        NAME,
        PRICE
    };
    private Map<String,ProductWithKey> mProductList;
    private ListView mListView;
    private boolean mSearchVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        ProductsDatabase mDataBase = new ProductsDatabase(myUser,this); //fetch products from db

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser != null) {
            final DatabaseReference myUserRef = FirebaseDatabase.getInstance().getReference("Users/" + mFirebaseUser.getUid());

            myUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    myUser = snapshot.getValue(User.class);
                    if (mProductList != null) {
                        //TODO update in mProductList for each product if purchased (from myUser keys)
                        for (String id : myUser.getMyBags()){
                            mProductList.get(id).setPurchased(true);
                        }
                    }
                    List<Product> prodListForShowing = new ArrayList<>();
                    mListView.setAdapter(new ProductsAdapter(mProductList,getApplicationContext(),myUser));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }




        mProductList = getProductList();
        mListView = findViewById(R.id.listView_Products);
        mListView.setAdapter(new ProductsAdapter(mProductList,this, myUser));

        //Search bar visibilty handling
        findViewById(R.id.viewSearchLayout).setVisibility(mSearchVisible ? View.VISIBLE : View.GONE);
        findViewById(R.id.productList_btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchVisible = !mSearchVisible;
                if (!mSearchVisible)
                    mProductList = getProductList();
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

        findViewById(R.id.productList_btnFilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });

        findViewById(R.id.productList_btnMyItems).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductList = getCurrentUserParchesedProductsList(mFirebaseUser);
                mListView.setAdapter(new ProductsAdapter(mProductList,getApplicationContext(), myUser));
            }
        });
    }

    private Map<String,ProductWithKey> getCurrentUserParchesedProductsList(FirebaseUser mFirebaseUser) {
        //TODO here should come the code that fetch only the products the user bought
        return mProductList;
    }

    private void showFilterDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.filter_dialog);
        dialog.setTitle("Filter");





        dialog.findViewById(R.id.filterDialog_btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterResult filterResult = new filterResult();
                filterResult.Shoes = ((CheckBox)dialog.findViewById(R.id.checkbox_shoes)).isChecked();
                filterResult.Bags= ((CheckBox)dialog.findViewById(R.id.checkbox_bags)).isChecked();
                filterResult.Blue= ((CheckBox)dialog.findViewById(R.id.checkbox_blue)).isChecked();
                filterResult.Red= ((CheckBox)dialog.findViewById(R.id.checkbox_red)).isChecked();
                filterResult.Towels= ((CheckBox)dialog.findViewById(R.id.checkbox_towels)).isChecked();
                filterResult.White= ((CheckBox)dialog.findViewById(R.id.checkbox_white)).isChecked();

                mProductList = getFilteredListFromFirebase(filterResult);
                mListView.setAdapter(new ProductsAdapter(mProductList,view.getContext(), myUser));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private List<ProductWithKey> getFilteredListFromFirebase(final filterResult filterResult) {
        DatabaseReference productsReference = FirebaseDatabase.getInstance().getReference("products");
        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                filterProductList(snapshot, filterResult);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //TODO here should be the code that fetch filtered list from firebase
        return mProductList;
    }

    private void showSortDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(R.string.sort_message)
                .setPositiveButton(R.string.sort_nameField, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mProductList = getSortedListFromFirebase(eSort.NAME);
                        mListView.setAdapter(new ProductsAdapter(mProductList,getBaseContext(), myUser));
                    }
                })
                .setNegativeButton(R.string.sort_priceField, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mProductList = getSortedListFromFirebase(eSort.PRICE);
                mListView.setAdapter(new ProductsAdapter(mProductList,getBaseContext(), myUser));
            }
        });
        dialogBuilder.create().show();
    }

    private List<ProductWithKey> getSortedListFromFirebase(final eSort orderBy) {
        DatabaseReference productsReference = FirebaseDatabase.getInstance().getReference("products");
        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                sortProductList(snapshot, orderBy);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //parameter orderBy (eSort.NAME     OR    eSort.PRICE)
        return mProductList;
    }

    private void sortProductList(DataSnapshot snapShot, final eSort orderBy){
        Query filteredList;
        DatabaseReference productsReference = FirebaseDatabase.getInstance().getReference("products");
        if(orderBy.equals(eSort.NAME)) {
            filteredList = productsReference.orderByChild("name");
        }
        else {
            filteredList = productsReference.orderByChild("price");
        }

        filteredList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                updateProductList(snapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    private void filterList(final CharSequence searchString) {
        Map<String,ProductWithKey> filteredList = new HashMap<>();
        for (String key : mProductList.keySet()){
            ProductWithKey prod = mProductList.get(key);
            if (prod.getproduct().getName().contains(searchString) || prod.getproduct().getCategory().contains(searchString)
                    || prod.getproduct().getPrice().contains(searchString))
                filteredList.put(key,mProductList.get(key));
        }
        mListView.setAdapter(new ProductsAdapter(filteredList,getApplicationContext(), myUser));
    }

    private Map<String,ProductWithKey> getProductList() {
        return new HashMap<>();
        //TODO here should be the code that fetch the data from the firebase storeage
    }

    public void updateListView(Map<String,ProductWithKey> prodList) {
        mProductList = prodList;
        mListView.setAdapter(new ProductsAdapter(mProductList,this,myUser));
    }

    private void filterProductList(DataSnapshot snapshot, final filterResult filterResult) {
        Query filteredList;
        DatabaseReference productsReference = FirebaseDatabase.getInstance().getReference("products");
        filteredList = productsReference.orderByChild("color");
        if(filterResult.White)
            filteredList = filteredList.equalTo("white");
        if(filterResult.Red)
            filteredList = filteredList.equalTo("red");
        if(filterResult.Blue)
            filteredList = filteredList.equalTo("blue");
        filteredList = filteredList.orderByChild("category");
        if(filterResult.Bags)
            filteredList = filteredList.equalTo("bags");
        if(filterResult.Shoes)
            filteredList = filteredList.equalTo("shoes");
        if(filterResult.Towels)
            filteredList = filteredList.equalTo("towels");

        filteredList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                updateProductList(snapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    private void updateProductList(DataSnapshot snapshot) {

        mProductList.clear();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            Product product = dataSnapshot.getValue(Product.class);
            String key = dataSnapshot.getKey();
            mProductList.put(key, product);
        }
        updateListView(mProductList);
    }
}
