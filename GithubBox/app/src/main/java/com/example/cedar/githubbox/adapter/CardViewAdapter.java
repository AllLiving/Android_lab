package com.example.cedar.githubbox.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.cedar.githubbox.R;

import java.util.List;

/**
 * Created by Cedar on 2017/12/24.
 */

public abstract class CardViewAdapter<T> extends RecyclerView.Adapter<CardViewAdapter.ItemCardViewHolder> {

    private Context context = null;
    private int mLayoutid;
    private List<T> mDatas = null;
    private OnItemClickListener monItemClickListener = null;

    public CardViewAdapter(Context context, List<T> datas, int layout){
        this.context = context;
        this.mDatas = datas;
        this.mLayoutid = layout;
    }

    @Override
    public ItemCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCardViewHolder itemCardViewHolder
                = ItemCardViewHolder.get(context, parent, mLayoutid);
        return itemCardViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemCardViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
        if (monItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    monItemClickListener.onLongClick(holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setMonItemClickListener(OnItemClickListener onItemClickListener){
        monItemClickListener = onItemClickListener;
    }

    public abstract void convert(ItemCardViewHolder holder, T position);

    public void removeItem(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ItemCardViewHolder extends RecyclerView.ViewHolder{
        private View mConvertView;
        private SparseArray<View> mViews;
        public ItemCardViewHolder(View view){
            super(view);
            mConvertView = view;
            mViews = new SparseArray<View>();
        }
        public static ItemCardViewHolder get(Context context, ViewGroup parent, int layout){
            View view =
                    LayoutInflater.from(context).inflate(layout, parent, false);
            ItemCardViewHolder holder
                    = new ItemCardViewHolder(view);
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
}
