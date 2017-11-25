package com.example.lixiang.threekingdoms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;

public class DetailActivity extends AppCompatActivity {
    private ImageView character_img;
    private TextView character_name;
    private TextView character_sex;
    private TextView character_date;
    private TextView introduction_name;
    private TextView character_origin;
    private TextView character_force;
    private TextView character_moreinfo;
    private ImageView favorite;
    private ImageView back_to_main;

    private FrameLayout mFlContainer;
    private FrameLayout mFlCardBack;
    private FrameLayout mFlCardFront;

    private AnimatorSet mRightOutSet; // 右出动画
    private AnimatorSet mLeftInSet; // 左入动画

    private boolean mIsShowBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);

        mFlContainer = (FrameLayout) findViewById(R.id.character_details);
        mFlCardBack = (FrameLayout) findViewById(R.id.main_fl_card_back);
        mFlCardFront = (FrameLayout) findViewById(R.id.main_fl_card_front);

        character_img = (ImageView) findViewById(R.id.character_img);
        character_name = (TextView) findViewById(R.id.character_name);
        character_sex = (TextView) findViewById(R.id.character_sex);
        character_date = (TextView) findViewById(R.id.character_date);
        character_origin = (TextView) findViewById(R.id.character_origin);
        character_force = (TextView) findViewById(R.id.character_force);
        introduction_name = (TextView) findViewById(R.id.introduction_name);
        character_moreinfo = (TextView) findViewById(R.id.introduction_others);
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
        if(characterInfo.getMoreInfoIdId()!=-1){
            character_moreinfo.setText(characterInfo.getMoreInfoIdId());
        }
        else character_moreinfo.setText(characterInfo.getInfo());
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

        back_to_main = (ImageView) findViewById(R.id.back_to_main);
        back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setAnimators(); // 设置动画
        setCameraDistance(); // 设置镜头距离
    }

    // 设置动画
    private void setAnimators() {
        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_out);
        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_in);

        // 设置点击事件
        mRightOutSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mFlContainer.setClickable(false);
            }
        });
        mLeftInSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFlContainer.setClickable(true);
            }
        });
    }

    // 改变视角距离, 贴近屏幕
    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mFlCardFront.setCameraDistance(scale);
        mFlCardBack.setCameraDistance(scale);
    }

    // 翻转卡片
    public void flipCard(View view) {
        // 正面朝上
        if (!mIsShowBack) {
            mRightOutSet.setTarget(mFlCardFront);
            mLeftInSet.setTarget(mFlCardBack);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = true;
        } else { // 背面朝上
            mRightOutSet.setTarget(mFlCardBack);
            mLeftInSet.setTarget(mFlCardFront);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = false;
        }
    }

}
