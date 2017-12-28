package com.example.cedar.githubbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cedar.githubbox.adapter.CardViewAdapter;
import com.example.cedar.githubbox.factory.ServiceFactory;
import com.example.cedar.githubbox.model.Repos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Cedar on 2017/12/25.
 */

public class ReposActivity extends AppCompatActivity{
    private RecyclerView recyclerView = null;
    private CardViewAdapter adapter = null;
    private List<Map<String, Object>> listitem = null;
    private ServiceFactory service = null;
    private ProgressBar progressBar = null;

    private String Url = "https://api.github.com/users/hellokenlee/repos";
    private String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repos_layout);

        Init();
        display();
        requestData();
    }

    private void Init(){
        recyclerView = (RecyclerView)findViewById(R.id.reposlist);
        recyclerView.setVisibility(View.INVISIBLE);
        listitem = new ArrayList<>();
        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        createAdapter();
    }
    private void createAdapter(){
        adapter = new CardViewAdapter<Map<String, Object>>(
                this, listitem, R.layout.cardview_layout
        ) {
            @Override
            public void convert(ItemCardViewHolder holder, Map<String, Object> s) {
                TextView reposname = holder.getView(R.id.title1);
                reposname.setText(s.get("name").toString());
                TextView reposlanguage = holder.getView(R.id.title2);
                reposlanguage.setText(s.get("language").toString());
                TextView reposdrscribe = holder.getView(R.id.title3);
                reposdrscribe.setText(s.get("description").toString());
            }
        };
    }

    private void display(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
    }

    private void requestData(){
        service.getApi("https://api.github.com/")
                .getRepos(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repos>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(List<Repos> reposes) {
                        listitem.clear();
                        String name="";
                        String language = "";
                        String description = "";
                        for(int i=0; i<reposes.size(); i++){
                            if (reposes.get(i).getname() != null){
                                name = reposes.get(i).getname();
                            }
                            if(reposes.get(i).getlanguage() != null){
                                language = reposes.get(i).getlanguage();
                            }
                            if (reposes.get(i).getDescription() != null){
                                description = reposes.get(i).getDescription();
                            }
                            Map<String, Object> tmp = new LinkedHashMap<>();
                            tmp.put("name", name);
                            tmp.put("language", language);
                            tmp.put("description", description);
                            listitem.add(tmp);
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}





















