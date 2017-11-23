package com.example.lixiang.threekingdoms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class DetailActivity extends AppCompatActivity {
    private ImageView character_img;
    private TextView character_name;
    private TextView character_sex;
    private TextView character_date;
    private TextView introduction_name;
    private TextView character_origin;
    private TextView character_force;
    private ImageView favorite;
    private ImageView back_to_card;
    private ImageView back_to_main;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);

        character_img = (ImageView) findViewById(R.id.character_img);
        character_name = (TextView) findViewById(R.id.character_name);
        character_sex = (TextView) findViewById(R.id.character_sex);
        character_date = (TextView) findViewById(R.id.character_date);
        character_origin = (TextView) findViewById(R.id.character_origin);
        character_force = (TextView) findViewById(R.id.character_force);
        introduction_name = (TextView) findViewById(R.id.introduction_name);
        favorite = (ImageView) findViewById(R.id.favorite);

        Intent intent = getIntent();
        final CharacterInfo characterInfo = intent.getParcelableExtra("Character");

        //接收人物信息并赋值
        character_img.setImageResource(characterInfo.getBgId());
        character_name.setText(characterInfo.getName());
        introduction_name.setText(characterInfo.getName());
        character_sex.setText(characterInfo.getSex());
        character_date.setText(characterInfo.getDate());
        character_origin.setText(characterInfo.getOrigin());
        character_force.setText(characterInfo.getForce());
        boolean like_flag = characterInfo.getIsLike();

        if (like_flag){
            favorite.setTag("1");
            favorite.setImageResource(R.drawable.ic_favorite_border_white);
        }
        else {
            favorite.setTag("0");
            favorite.setImageResource(R.drawable.ic_favorite_white);
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favorite.getTag() == "0") {
                    favorite.setImageResource(R.drawable.ic_favorite_border_white);
                    favorite.setTag("1");
                    EventBus.getDefault().post(new MessageEvent(characterInfo, 1));
                }
                else {
                    favorite.setImageResource(R.drawable.ic_favorite_white);
                    favorite.setTag("0");
                    EventBus.getDefault().post(new MessageEvent(characterInfo, 0));
                }
            }
        });

        final RelativeLayout card = (RelativeLayout) findViewById(R.id.card);
        final LinearLayout introduction = (LinearLayout) findViewById(R.id.introduction);
        //设置初始状态
        card.setVisibility(View.VISIBLE);
        introduction.setVisibility(View.INVISIBLE);
        character_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card.setVisibility(View.INVISIBLE);
                introduction.setVisibility(View.VISIBLE);
            }
        });

        back_to_card = (ImageView) findViewById(R.id.back_to_card);
        back_to_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card.setVisibility(View.VISIBLE);
                introduction.setVisibility(View.INVISIBLE);
            }
        });

        back_to_main = (ImageView) findViewById(R.id.back_to_main);
        back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
