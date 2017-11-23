package com.example.lixiang.threekingdoms;

import android.os.Parcel;
import android.os.Parcelable;

public class CharacterInfo implements Parcelable {
    private String name;
    private String sex;
    private String date;
    private String origin;
    private String force;

    private int resId;
    private int bgId;
    private String letters;
    private boolean isLike;

    public CharacterInfo(String name, String sex, String date, String origin, String allegiance_force){
        this.isLike = false;
        this.name = name;
        this.sex = sex;
        this.date = date;
        this.origin = origin;
        this.force = allegiance_force;
        switch (name){
            case "蔡琰":
                resId = R.drawable.avatar_caiyan;
                bgId = R.drawable.bg_caiyan;
                break;
            case "曹操":
                resId = R.drawable.avatar_caocao;
                bgId = R.drawable.bg_caocao;
                break;
            case "大乔":
                resId = R.drawable.avatar_daqiao;
                bgId = R.drawable.bg_daqiao;
                break;
            case "典韦":
                resId = R.drawable.avatar_dianwei;
                bgId = R.drawable.bg_dianwei;
                break;
            case "貂蝉":
                resId = R.drawable.avatar_diaochan;
                bgId = R.drawable.bg_diaochan;
                break;
            case "关羽":
                resId = R.drawable.avatar_guanyu;
                bgId = R.drawable.bg_guanyu;
                break;
            case "刘备":
                resId = R.drawable.avatar_liubei;
                bgId = R.drawable.bg_liubei;
                break;
            case "吕布":
                resId = R.drawable.avatar_lvbu;
                bgId = R.drawable.bg_lvbu;
                break;
            case "司马懿":
                resId = R.drawable.avatar_simayi;
                bgId = R.drawable.bg_simayi;
                break;
            case "孙策":
                resId = R.drawable.avatar_sunce;
                bgId = R.drawable.bg_sunce;
                break;
            case "孙权":
                resId = R.drawable.avatar_sunquan;
                bgId = R.drawable.bg_sunquan;
                break;
            case "孙尚香":
                resId = R.drawable.avatar_sunshangxiang;
                bgId = R.drawable.bg_sunshangxiang;
                break;
            case "文昭皇后":
                resId = R.drawable.avatar_wenzhaohuanghou;
                bgId = R.drawable.bg_wenzhaohuanghou;
                break;
            case "张飞":
                resId = R.drawable.avatar_zhangfei;
                bgId = R.drawable.bg_zhangfei;
                break;
            case "周瑜":
                resId = R.drawable.avatar_zhouyu;
                bgId = R.drawable.bg_zhouyu;
                break;
            case "诸葛亮":
                resId = R.drawable.avatar_zhugeliang;
                bgId = R.drawable.bg_zhugeliang;
                break;
            default:
                switch (sex) {
                    case "男":
                        resId = R.drawable.avatar_nan;
                        bgId = R.drawable.bg_nan;
                        break;
                    case "女":
                        resId = R.drawable.avatar_nv;
                        bgId = R.drawable.bg_nv;
                        break;
                }
                break;
        }
        String pinyin = PinyinUtils.getPingYin(name);
        String sortString = pinyin.substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            this.letters = sortString.toUpperCase();
        }
        else {
            this.letters = "#";
        }
    }
    public String getName(){
        return name;
    }
    public String getSex(){
        return sex;
    }
    public String getDate(){
        return date;
    }
    public String getOrigin(){
        return origin;
    }
    public String getForce(){
        return force;
    }
    public int getResId(){
        return resId;
    }
    public int getBgId(){
        return bgId;
    }
    public String getLetters() {
        return letters;
    }
    public boolean getIsLike(){
        return isLike;
    }
    public void setResId(int resId) {
        this.resId = resId;
    }
    public void setBgId(int bgId) {
        this.bgId = bgId;
    }
    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }
    public void setLetters(String letters) {
        this.letters = letters;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(sex);
        out.writeString(date);
        out.writeString(origin);
        out.writeString(force);
        out.writeByte((byte)(isLike ? 1:0));
    }

    public static final Parcelable.Creator<CharacterInfo> CREATOR = new Parcelable.Creator<CharacterInfo>() {
        @Override
        public CharacterInfo createFromParcel(Parcel source) {
            CharacterInfo character = new CharacterInfo(source.readString(), source.readString(), source.readString(), source.readString(), source.readString());
            character.setIsLike(source.readByte() != 0);
            return character;
        }

        @Override
        public CharacterInfo[] newArray(int size) {
            return new CharacterInfo[size];
        }
    };

}
