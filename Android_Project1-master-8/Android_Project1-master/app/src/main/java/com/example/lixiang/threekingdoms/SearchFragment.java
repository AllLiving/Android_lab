package com.example.lixiang.threekingdoms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment{
    private View root;
    private List<CharacterInfo> characters;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private PinyinComparator pinyinComparator;
    LinearLayoutManager manager;
    private ClearEditText mClearEditText;

    public static SearchFragment newInstance(List<CharacterInfo> characters) {
        SearchFragment newFragment = new SearchFragment();
        newFragment.characters = characters;
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root==null){
            root = inflater.inflate(R.layout.fragment_search,container,false);
            pinyinComparator = new PinyinComparator();
            sideBar = (SideBar) root.findViewById(R.id.sideBar);
            dialog = (TextView) root.findViewById(R.id.dialog);
            sideBar.setTextView(dialog);
            adapter = new SortAdapter(characters, getContext());
            manager = new LinearLayoutManager(getContext());

            //设置右侧SideBar触摸监听
            sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

                @Override
                public void onTouchingLetterChanged(String s) {
                    //该字母首次出现的位置
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        manager.scrollToPositionWithOffset(position, 0);
                    }
                }
            });
            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_search);
            Collections.sort(characters, pinyinComparator);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

            mClearEditText = (ClearEditText) root.findViewById(R.id.filter_edit);
            //根据输入框输入值的改变来过滤搜索
            mClearEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                    filterData(s.toString());
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_search);
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

    private void filterData(String filterStr) {
        List<CharacterInfo> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = characters;
        }
        else {
            filterDateList.clear();
            for (CharacterInfo cha : characters) {
                String name = cha.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        ) {
                    filterDateList.add(cha);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateList(filterDateList);
    }

    public class SortAdapter extends RecyclerView.Adapter<SortAdapter.sHolder> {
        private List<CharacterInfo> characters;
        private Context context;

        public SortAdapter(List<CharacterInfo> characters, Context context) {
            this.context = context;
            this.characters = characters;
        }

        @Override
        public sHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
            return new sHolder(root);
        }

        @Override
        public void onBindViewHolder(sHolder holder, int position) {
            int section = getSectionForPosition(position);
            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                holder.MainTag.setVisibility(View.VISIBLE);
                holder.MainTag.setText(characters.get(position).getLetters());
            }
            else {
                holder.MainTag.setVisibility(View.GONE);
            }

            holder.MainName.setText(characters.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return characters.size();
        }

        class sHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TextView MainName;
            private TextView MainTag;

            sHolder(View itemView) {
                super(itemView);
                MainName = (TextView) itemView.findViewById(R.id.search_name);
                MainTag = (TextView) itemView.findViewById(R.id.tag);
                MainName.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("Character", characters.get(getAdapterPosition()));
                startActivity(intent);
            }
        }

        public void updateList(List<CharacterInfo> list){
            this.characters = list;
            notifyDataSetChanged();
        }

        public Object getItem(int position) {
            return characters.get(position);
        }

        //根据当前位置获取分类的首字母的char ascii值
        public int getSectionForPosition(int position) {
            return characters.get(position).getLetters().charAt(0);
        }

        //根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
        public int getPositionForSection(int section) {
            for (int i = 0; i < getItemCount(); i++) {
                String sortStr = characters.get(i).getLetters();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
            return -1;
        }

    }

}
