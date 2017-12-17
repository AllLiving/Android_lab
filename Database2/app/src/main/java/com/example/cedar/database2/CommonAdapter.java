package com.example.cedar.database2;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by Cedar on 2017/12/14.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext = null;
    private int mLayoutid;
    private List<T> mDatas = null;
    private OnItemClickListener monItemClickListener = null;

    public CommonAdapter(Context context, List<T> datas, int layoutid){
        this.mContext = context;
        this.mLayoutid = layoutid;
        this.mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mLayoutid);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
//        Set item height;
//        ViewHolder item = holder;
//        ViewGroup.LayoutParams layoutParams = item.itemView.getLayoutParams();
//        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
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

    public abstract void convert(ViewHolder holder, T position);

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        monItemClickListener = onItemClickListener;
    }

    public void removeItem(int position){
        if (position >= 0){
            mDatas.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateItem(int position){
        if (position >= 0){
            notifyItemInserted(position);
            notifyItemRemoved(position);
            notifyItemChanged(position);
        }
    }
}





























