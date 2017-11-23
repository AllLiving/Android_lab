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

public class HomeFragment extends Fragment{
    private View root;
    private List<CharacterInfo> characters;

    public static HomeFragment newInstance(List<CharacterInfo> characters) {
        HomeFragment newFragment = new HomeFragment();
        newFragment.characters = characters;
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root==null){
            root = inflater.inflate(R.layout.fragment_home,container,false);
            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_home);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
            recyclerView.setAdapter(new MyAdapter(characters, getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

            final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh);
            swipeRefreshLayout.setColorSchemeColors(Color.RED);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    },1000);
                }
            });
        }
        return root;
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {
        private List<CharacterInfo> characters;
        private Context context;

        public MyAdapter(List<CharacterInfo> characters, Context context) {
            this.context = context;
            this.characters = characters;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.MainImg.setImageResource(characters.get(position).getResId());
            holder.MainName.setText(characters.get(position).getName());
            holder.MainInfo.setText(characters.get(position).getSex()+ "     生卒: "+characters.get(position).getDate()+"     主效势力: "+characters.get(position).getForce());
        }

        @Override
        public int getItemCount() {
            return characters.size();
        }

        class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            private ImageView MainImg;
            private TextView MainName;
            private TextView MainInfo;

            Holder(View itemView) {
                super(itemView);
                MainName = (TextView) itemView.findViewById(R.id.MainName);
                MainInfo = (TextView) itemView.findViewById(R.id.MainInfo);
                MainImg = (ImageView) itemView.findViewById(R.id.MainImg);

                View main = itemView.findViewById(R.id.main);
                main.setOnClickListener(this);
                main.setOnLongClickListener(this);

                View mark = itemView.findViewById(R.id.mark);
                mark.setOnClickListener(this);

                View delete = itemView.findViewById(R.id.delete);
                delete.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.main:
                        Intent intent = new Intent(v.getContext(), DetailActivity.class);
                        intent.putExtra("Character", characters.get(getAdapterPosition()));
                        startActivity(intent);
                        break;
                    case R.id.mark:
                        EventBus.getDefault().post(new MessageEvent(characters.get(getAdapterPosition()), 1));
                        notifyDataSetChanged();
                        break;
                    case R.id.delete:
                        int pos = getAdapterPosition();
                        characters.remove(pos);
                        notifyItemRemoved(pos);
                        break;
                }
            }

            @Override
            public boolean onLongClick(View v) {
                switch (v.getId()) {
                    case R.id.main:
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle(MainName.getText());
                        final String[] method = {"加入收藏", "删除人物"};
                        builder.setItems(method, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (method[which].equals("加入收藏")) {
                                    EventBus.getDefault().post(new MessageEvent(characters.get(getAdapterPosition()), 1));
                                    notifyDataSetChanged();
                                }
                                else {
                                    int pos = getAdapterPosition();
                                    characters.remove(pos);
                                    notifyItemRemoved(pos);
                                }
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
