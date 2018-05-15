package com.example.noytse.loginfacebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by NoyTse on 15/05/2018.
 */

public class ReviewAdapter extends BaseAdapter {
    List<Review> mReviewList;
    Context mContext;

    public ReviewAdapter(List<Review> mReviewList, Context mContext) {
        this.mReviewList = mReviewList;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return mReviewList.size();
    }

    @Override
    public Object getItem(int i) {
        return mReviewList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_listitem,viewGroup,false);

        ((TextView)view.findViewById(R.id.reviewItem_userName)).setText(mReviewList.get(i).getUserName());
        ((TextView)view.findViewById(R.id.reviewItem_content)).setText(mReviewList.get(i).getReviewTxt());
        return view;
    }
}
