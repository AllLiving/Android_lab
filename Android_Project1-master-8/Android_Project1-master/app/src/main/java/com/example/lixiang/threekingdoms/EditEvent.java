package com.example.lixiang.threekingdoms;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Aroya on 11/25/2017.
 */

//从editCharacter向MainActivity传递文件
public class EditEvent {
    public static final int EDIT_ACTION=0;
    public static final int NEW_ACTION=1;
    private int action;//0:new 1:
    private int imgId,iconId,listPosition;
    private String name,sex,date,origin,force,info;

    //img为全身图，icon为头像

    //初始化/创建方法
    //添加人物
    public EditEvent(int ImgId,int IconId,
                          String Name,
                          String Sex,
                          String Date,
                          String Origin,
                          String Force,
                          String Info){
        action=NEW_ACTION;
        imgId=ImgId;
        iconId=IconId;
        name=Name;
        sex=Sex;
        date=Date;
        origin=Origin;
        force=Force;
        info=Info;
        listPosition=-1;
    }
    //创建人物用初始化
    public EditEvent(int ImgId,int IconId,
                          String Name,
                          String Sex,
                          String Date,
                          String Origin,
                          String Force,
                          String Info,
                          int ListPosition){
        action=EDIT_ACTION;
        imgId=ImgId;
        iconId=IconId;
        name=Name;
        sex=Sex;
        date=Date;
        origin=Origin;
        force=Force;
        info=Info;
        listPosition=ListPosition;
    }

    //get方法
    public int getAction(){return action;}
    public int getImgId(){return imgId;}
    public int getIconId(){return iconId;}
    public String getName(){return name;}
    public String getSex(){return sex;}
    public String getDate(){return date;}
    public String getOrigin(){return origin;}
    public String getForce(){return force;}
    public String getInfo(){return info;}
    public int getListPosition(){return listPosition;}
}
