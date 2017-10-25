package com.example.cedar.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Cedar on 2017/10/21.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder>{

    private Context mContext;
    private int mLayoutid;
    private List<T> mDatas;
    private OnItemClickListener monItemClickListener=null;

    public CommonAdapter(Context context, List<T> datas, int layoutid){
        this.mContext = context;
        this.mLayoutid = layoutid;
        this.mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mLayoutid);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        convert(holder, mDatas.get(position));
        if(monItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    monItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    monItemClickListener.onLongClick(holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount(){
        return mDatas.size();
    }

    public abstract void convert(ViewHolder holder, T position);

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        monItemClickListener = onItemClickListener;
    }

    public void removeItem(int position){
        if(position >= 0){
            mDatas.remove(position);
            notifyItemRemoved(position);
        }
    }

}