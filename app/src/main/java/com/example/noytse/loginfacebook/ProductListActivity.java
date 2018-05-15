package com.example.noytse.loginfacebook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.example.noytse.loginfacebook.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private FirebaseUser mFirebaseUser;
    private User myUser;

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


        if (mFirebaseUser != null) {
            DatabaseReference myUserRef = FirebaseDatabase.getInstance().getReference("Users/" + mFirebaseUser.getUid());

            myUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    myUser = snapshot.getValue(User.class);
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

    private List<Product> getCurrentUserParchesedProductsList(FirebaseUser mFirebaseUser) {
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

    private List<Product> getFilteredListFromFirebase(filterResult filterResult) {
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
        mListView.setAdapter(new ProductsAdapter(filteredList,getApplicationContext(), myUser));
    }

    private List<Product> getProductList() {
        //TODO here should be the code that fetch the data from the firebase storeage
        ArrayList<Review> demoReviewList = new ArrayList<>();
        demoReviewList.add(new Review("Demo User Name", "Demo Review 1"));
        demoReviewList.add(new Review("Demo User Name", "Demo Review 2"));
        demoReviewList.add(new Review("Demo User Name", "Demo Review 3"));
        //temp implementation       DELETE WHEN IMPLEMENT
        List<Product> resList = new ArrayList<>();
        resList.add(new Product("p1","bags","blue","yes","150x50 cm","Meshi"
                ,null,"15$",demoReviewList));

        resList.add(new Product("p2","bags","red","yes","150x50 cm","Meshi"
                ,null,"35$",demoReviewList));

        resList.add(new Product("a3","Shoes","blue","yes","150x50 cm","Meshi"
                ,null,"135$",demoReviewList));
        return  resList;
    }
}
