package com.example.lixiang.threekingdoms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lixiang.threekingdoms.SwipeItemLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment{
    private View root;
    private List<CharacterInfo> characters;
    MyAdapter_f fAdapter;

    public static FavoriteFragment newInstance(List<CharacterInfo> characters) {
        FavoriteFragment newFragment = new FavoriteFragment();
        newFragment.characters = characters;
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root==null){
            root = inflater.inflate(R.layout.fragment_favorite,container,false);
            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_favorite);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
            fAdapter = new MyAdapter_f(characters, getContext());
            recyclerView.setAdapter(fAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

            final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_favorite);
            swipeRefreshLayout.setColorSchemeColors(Color.RED);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            fAdapter.notifyDataSetChanged();
                        }
                    },1000);
                }
            });
        }
        return root;
    }


    public class MyAdapter_f extends RecyclerView.Adapter<MyAdapter_f.Holder> {
        private List<CharacterInfo> characters;
        private Context context;

        public MyAdapter_f(List<CharacterInfo> characters, Context context) {
            this.context = context;
            this.characters = characters;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.MainImg.setImageResource(characters.get(position).getResId());
            holder.MainName.setText(characters.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return characters.size();
        }

        class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            private ImageView MainImg;
            private TextView MainName;

            Holder(View itemView) {
                super(itemView);
                MainName = (TextView) itemView.findViewById(R.id.MainName_favo);
                MainImg = (ImageView) itemView.findViewById(R.id.MainImg_favo);

                View main = itemView.findViewById(R.id.main_favo);
                main.setOnClickListener(this);
                main.setOnLongClickListener(this);

                View delete = itemView.findViewById(R.id.delete_favo);
                delete.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.main_favo:
                        Intent intent = new Intent(v.getContext(), DetailActivity.class);
                        intent.putExtra("Character", characters.get(getAdapterPosition()));
                        startActivity(intent);
                        break;
                    case R.id.delete_favo:
                        EventBus.getDefault().post(new MessageEvent(characters.get(getAdapterPosition()), 0));
                        notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public boolean onLongClick(View v) {
                switch (v.getId()) {
                    case R.id.main_favo:
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("取消收藏");
                        builder.setMessage("从收藏夹中移除 "+MainName.getText()+" ?");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventBus.getDefault().post(new MessageEvent(characters.get(getAdapterPosition()), 0));
                                notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create();
                        builder.show();
                        break;
                }
                return false;
            }
        }
    }

}
