package com.example.lixiang.threekingdoms;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Aroya on 11/21/2017.
 */

public class CreateCharacter extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    ImageView characterIcon;
    TextView characterName,characterOrigin,characterInfo;
    RadioGroup characterSex;
    Button confirm,cancel;
    String nameData,originData,infoData,sexData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit);
        findView();
        setListener();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.activity_edit_layout);
        ActionBarDrawerToggle toggle
                = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(CreateCharacter.this);
    }

    private void findView(){
        characterIcon=(ImageView)findViewById(R.id.edit_icon);
        characterName=(TextView)findViewById(R.id.edit_name);
        characterOrigin=(TextView)findViewById(R.id.edit_origin);
        characterInfo=(TextView)findViewById(R.id.edit_moreinfo);
        characterSex=(RadioGroup)findViewById(R.id.edit_sex);
        confirm=(Button)findViewById(R.id.edit_confirm);
        cancel=(Button)findViewById(R.id.edit_cancel);
    }
    private void setListener(){
        characterIcon.setOnClickListener(new iconOnClickListener());
        confirm.setOnClickListener(new confirmOnClickListener());
        cancel.setOnClickListener(new cancelOnClickListener());
    }
    private class iconOnClickListener implements View.OnClickListener{
        public void onClick(View view){
            //点击图片事件
            Toast.makeText(getApplicationContext(),"点击了人物图标",Toast.LENGTH_SHORT).show();
            //to do
        }
    }
    private class confirmOnClickListener implements View.OnClickListener{
        public void onClick(View view){
            //点击确认按钮事件
            Toast.makeText(getApplicationContext(),"点击了确认按钮",Toast.LENGTH_SHORT).show();
            readData();
            //to do
        }
    }
    private class cancelOnClickListener implements View.OnClickListener{
        public void onClick(View view){
            //点击确认按钮事件
            Toast.makeText(getApplicationContext(),"点击了取消按钮",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void readData(){
        nameData=characterName.toString();
        originData=characterOrigin.toString();
        infoData=characterInfo.toString();
        sexData=characterSex.toString();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.favorite){
            Toast.makeText(getApplication(), "favorite", Toast.LENGTH_SHORT)
                    .show();
        }else if(id == R.id.wallet){
            Toast.makeText(getApplication(), "wallet", Toast.LENGTH_SHORT)
                    .show();
        }else if(id == R.id.photo){

        }else if (id == R.id.file){

        }

        return true;
    }
}
