package com.example.lixiang.threekingdoms;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import android.widget.Button;
import android.widget.TextView;
import android.widget.RadioGroup;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.lixiang.threekingdoms.R.id.character_img;
import static com.example.lixiang.threekingdoms.R.id.file;

public class editCharacter extends AppCompatActivity{

    ImageView characterIcon;
    TextView characterName,characterOrigin,characterDate,characterInfo,characterForce;
    RadioGroup characterSex;
    Button confirm,cancel;
    String nameData,originData,infoData,forceData,sexData,dateData;
    DrawerLayout drawerLayout;
    int iconID,imgID;
    int ACTION_TYPE,listPosition;
    int []recycler_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_character);

        findView();
        getAction();
        setListener();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.activity_layout);
        ActionBarDrawerToggle toggle
                = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.navigation_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        initRecyclerRes();
        recyclerView.setAdapter(new recyclerViewAdapter());

//        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
//        navigationView.setNavigationItemSelectedListener(editCharacter.this);
//        navigationView.setNavigationItemSelectedListener(this);
//        Resources resources = (Resources) getBaseContext().getResources();

        click_image(characterIcon);
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if(id == R.id.favorite){
//            Toast.makeText(getApplication(), "favorite", Toast.LENGTH_SHORT)
//                    .show();
//        }else if(id == R.id.wallet){
//            Toast.makeText(getApplication(), "wallet", Toast.LENGTH_SHORT)
//                    .show();
//        }else if(id == R.id.photo){
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_nv);
//            iconID = R.drawable.avatar_nv;
//            ImageView view = (ImageView) findViewById(R.id.edit_icon);
//            view.setImageBitmap(bitmap);
//        }else if (id == file){
//
//        }
//
//        return true;
//    }

    void SelectPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    void TakePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    public void click_image(View img){
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(editCharacter.this);
                dialog.setTitle("Change Portrait");
                final String[] items = {"select from album", "shoot now"};
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplication(), "You decide to "
                            + items[which], Toast.LENGTH_SHORT)
                                .show();
                        if(which == 1){
                            TakePhoto();
                        }else{
                            SelectPhoto();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void findView(){
        characterIcon=(ImageView)findViewById(R.id.edit_icon);
        characterName=(TextView)findViewById(R.id.edit_name);
        characterOrigin=(TextView)findViewById(R.id.edit_origin);
        characterForce=(TextView)findViewById(R.id.edit_force);
        characterDate=(TextView)findViewById(R.id.edit_date);
        characterInfo=(TextView)findViewById(R.id.edit_moreinfo);
        characterSex=(RadioGroup)findViewById(R.id.edit_sex);
        confirm=(Button)findViewById(R.id.edit_confirm);
        cancel=(Button)findViewById(R.id.edit_cancel);
    }
    private void setListener(){
        //characterIcon.setOnClickListener(new iconOnClickListener());
        confirm.setOnClickListener(new confirmOnClickListener());
        cancel.setOnClickListener(new cancelOnClickListener());
    }

    private class confirmOnClickListener implements View.OnClickListener{
        public void onClick(View view){
            //点击确认按钮事件
            Toast.makeText(getApplicationContext(),"点击了确认按钮",Toast.LENGTH_SHORT).show();
            readData();
            switch (ACTION_TYPE){
                case EditEvent.EDIT_ACTION:
                    EventBus.getDefault().post(new EditEvent(imgID,iconID,nameData,sexData,dateData,originData,forceData,infoData,listPosition));
                    break;
                case EditEvent.NEW_ACTION:
                    EventBus.getDefault().post(new EditEvent(imgID,iconID,nameData,sexData,dateData,originData,forceData,infoData));
                    break;
            }
            finish();
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
        nameData=characterName.getText().toString();
        originData=characterOrigin.getText().toString();
        infoData=characterInfo.getText().toString();
        sexData=characterSex.getCheckedRadioButtonId()==R.id.edit_sex_male?"男":"女";
        forceData=characterForce.getText().toString();
        dateData=characterDate.getText().toString();
        //获得bg图片
        getImgByIcon();
    }

    //获取当前操作类型
    private void getAction(){
        ACTION_TYPE=getIntent().getIntExtra("ACTION",EditEvent.NEW_ACTION);//默认为新建
        switch (ACTION_TYPE){
            case EditEvent.EDIT_ACTION:
                //获取到位置信息
                listPosition=getIntent().getIntExtra("listPosition",-1);
                CharacterInfo characterInfo_=getIntent().getParcelableExtra("charactersInfo");

//                characterName.setText(characterInfos.getName());
                //其他信息的设置 图片、性别、人物生平等等
                characterIcon.setImageResource(characterInfo_.getResId());
                iconID=characterInfo_.getResId();
                imgID=characterInfo_.getBgId();
                if(characterInfo_.getSex().equals("男")){
                    characterSex.check(R.id.edit_sex_male);
                }
                else{
                    characterSex.check(R.id.edit_sex_female);
                }
                characterName.setText(characterInfo_.getName());
                characterOrigin.setText(characterInfo_.getOrigin());
                characterDate.setText(characterInfo_.getDate());
                characterInfo.setText(characterInfo_.getInfo());
                characterForce.setText(characterInfo_.getForce());
                break;
        }
    }

    private void getImgByIcon(){
        switch (iconID){
            case R.drawable.avatar_caiyan:
                imgID = R.drawable.bg_caiyan;
                break;
            case R.drawable.avatar_caocao:
                imgID = R.drawable.bg_caocao;
                break;
            case R.drawable.avatar_daqiao:
                imgID = R.drawable.bg_daqiao;
                break;
            case R.drawable.avatar_dianwei:
                imgID = R.drawable.bg_dianwei;
                break;
            case R.drawable.avatar_diaochan:
                imgID = R.drawable.bg_diaochan;
                break;
            case R.drawable.avatar_guanyu:
                imgID = R.drawable.bg_guanyu;
                break;
            case R.drawable.avatar_liubei:
                imgID = R.drawable.bg_liubei;
                break;
            case R.drawable.avatar_lvbu:
                imgID = R.drawable.bg_lvbu;
                break;
            case R.drawable.avatar_simayi:
                imgID = R.drawable.bg_simayi;
                break;
            case R.drawable.avatar_sunce:
                imgID = R.drawable.bg_sunce;
                break;
            case R.drawable.avatar_sunquan:
                imgID = R.drawable.bg_sunquan;
                break;
            case R.drawable.avatar_sunshangxiang:
                imgID = R.drawable.bg_sunshangxiang;
                break;
            case R.drawable.avatar_wenzhaohuanghou:
                imgID = R.drawable.bg_wenzhaohuanghou;
                break;
            case R.drawable.avatar_zhangfei:
                imgID = R.drawable.bg_zhangfei;
                break;
            case R.drawable.avatar_zhouyu:
                imgID = R.drawable.bg_zhouyu;
                break;
            case R.drawable.avatar_zhugeliang:
                imgID = R.drawable.bg_zhugeliang;
                break;
            case R.drawable.avatar_nan:
                imgID = R.drawable.bg_nan;
                break;
            case R.drawable.avatar_nv:
                imgID = R.drawable.bg_nv;
                break;
            default:
                switch (sexData) {
                    case "男":
                        iconID = R.drawable.avatar_nan;
                        imgID = R.drawable.bg_nan;
                        break;
                    case "女":
                        iconID = R.drawable.avatar_nv;
                        imgID = R.drawable.bg_nv;
                        break;
                }
                break;
        }
    }
    private void initRecyclerRes(){
        recycler_res=new int[]{
                R.drawable.avatar_caiyan,R.drawable.avatar_caocao,
                R.drawable.avatar_daqiao,R.drawable.avatar_dianwei,
                R.drawable.avatar_diaochan,R.drawable.avatar_guanyu,
                R.drawable.avatar_liubei,R.drawable.avatar_lvbu,
                R.drawable.avatar_simayi,R.drawable.avatar_sunce,
                R.drawable.avatar_sunquan,R.drawable.avatar_sunshangxiang,
                R.drawable.avatar_wenzhaohuanghou,R.drawable.avatar_zhangfei,
                R.drawable.avatar_zhouyu,R.drawable.avatar_zhugeliang,
                R.drawable.avatar_nan,R.drawable.avatar_nv
        };
    }

    //RecyclerView Adapter
    private class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_recycler_item,viewGroup,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position){
            viewHolder.imageView.setImageResource(recycler_res[position]);
        }

        @Override
        public int getItemCount(){return recycler_res.length;}


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public ImageView imageView;
            public ViewHolder(View view){
                super(view);
                imageView=(ImageView)view.findViewById(R.id.edit_recycler_icon);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view){
                int pos=getPosition();
                characterIcon.setImageResource(recycler_res[pos]);
                iconID=recycler_res[pos];
                drawerLayout.closeDrawers();
            }
        }

    }

}
