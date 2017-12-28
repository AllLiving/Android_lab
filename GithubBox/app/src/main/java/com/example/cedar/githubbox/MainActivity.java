package com.example.cedar.githubbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cedar.githubbox.adapter.CardViewAdapter;
import com.example.cedar.githubbox.adapter.CommonAdapter;
import com.example.cedar.githubbox.factory.ServiceFactory;
import com.example.cedar.githubbox.model.Github;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public EditText username = null;
    private Button clear = null;
    private Button fetch = null;
    private ProgressBar progressBar = null;

    private String name;
    private String blog;
    private int id;

    private ServiceFactory service = null;

    private RecyclerView recyclerView = null;
    private CardViewAdapter adapter = null;
    private List<Map<String, Object>> listitem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
        setClick();
        display();
    }

    private void Init(){
        username = (EditText)findViewById(R.id.username);
        username.setText("hellokenlee");
        clear = (Button)findViewById(R.id.clear);
        fetch = (Button)findViewById(R.id.fetch);
        recyclerView = (RecyclerView)findViewById(R.id.idlist);
        listitem = new ArrayList<>();
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        createAdapter();
    }

    private void createAdapter(){
        adapter = new CardViewAdapter<Map<String, Object>>(
                this, listitem, R.layout.cardview_layout
        ){
            @Override
            public void convert(ItemCardViewHolder holder, Map<String, Object> s) {
                TextView loginname = holder.getView(R.id.title1);
                loginname.setText(s.get("name").toString());
                TextView id = holder.getView(R.id.title2);
                id.setText("id:" + s.get("id").toString());
                TextView blog = holder.getView(R.id.title3);
                blog.setText("blog:"+s.get("blog").toString());
            }
        };
    }

    private void setClick(){
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
            }
        });
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = username.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                requestData();
            }
        });
        adapter.setMonItemClickListener(new CardViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, ReposActivity.class);
                Map<String, Object> tmp = listitem.get(position);
                String username = tmp.get("name").toString();
                intent.putExtra("username", username);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {
                adapter.removeItem(position);
            }
        });
    }

    private void requestData(){
        service.getApi("https://api.github.com/")
                .getUser(name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Github>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplication(), "Lose Connection", Toast.LENGTH_SHORT)
                                .show();
                        recyclerView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        progressBar.setProgress(0);
                    }

                    @Override
                    public void onNext(Github github) {
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplication(), "Get "+github.getLogin(), Toast.LENGTH_SHORT)
                                .show();
                        name = github.getLogin();
                        blog = github.getBlog();
                        id = github.getId();
                        Map<String, Object> tmp = new LinkedHashMap<>();
                        tmp.put("name", name);
                        tmp.put("id", id);
                        tmp.put("blog", blog);
                        if(!listitem.contains(tmp)){
                            listitem.add(tmp);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void display(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
    }
}






















