package com.example.cedar.githubbox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Cedar on 2017/12/14.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private View mConvertView;
    private SparseArray<View> mViews;

    public ViewHolder(Context context, View itemview, ViewGroup parent){
        super(itemview);
        mConvertView = itemview;
        mViews = new SparseArray<View>();
    }

    public static ViewHolder get(Context context, ViewGroup parent, int layoutid){
        View itemview
                = LayoutInflater.from(context).inflate(layoutid, parent, false);
        ViewHolder holder = new ViewHolder(context, itemview, parent);
        return holder;
    }

    public <T extends View> T getView(int viewid){
        View view = mViews.get(viewid);
        if (view == null){
            view = mConvertView.findViewById(viewid);
            mViews.put(viewid, view);
        }
        return (T)view;
    }


























}
