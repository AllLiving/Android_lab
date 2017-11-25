package com.example.lixiang.threekingdoms;

import android.os.Parcel;
import android.os.Parcelable;

public class CharacterInfo implements Parcelable {
    public static final int LIXIANG=0;
    public static final int B501=-1;

    private String name;
    private String sex;
    private String date;
    private String origin;
    private String force;
    private String info;

    private int resId;
    private int bgId;
    private int moreInfoId;
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
                moreInfoId = R.string.info_caiyan;
                break;
            case "曹操":
                resId = R.drawable.avatar_caocao;
                bgId = R.drawable.bg_caocao;
                moreInfoId = R.string.info_caocao;
                break;
            case "大乔":
                resId = R.drawable.avatar_daqiao;
                bgId = R.drawable.bg_daqiao;
                moreInfoId = R.string.info_daqiao;
                break;
            case "典韦":
                resId = R.drawable.avatar_dianwei;
                bgId = R.drawable.bg_dianwei;
                moreInfoId = R.string.info_dianwei;
                break;
            case "貂蝉":
                resId = R.drawable.avatar_diaochan;
                bgId = R.drawable.bg_diaochan;
                moreInfoId = R.string.info_diaochan;
                break;
            case "关羽":
                resId = R.drawable.avatar_guanyu;
                bgId = R.drawable.bg_guanyu;
                moreInfoId = R.string.info_guanyu;
                break;
            case "刘备":
                resId = R.drawable.avatar_liubei;
                bgId = R.drawable.bg_liubei;
                moreInfoId = R.string.info_liubei;
                break;
            case "吕布":
                resId = R.drawable.avatar_lvbu;
                bgId = R.drawable.bg_lvbu;
                moreInfoId = R.string.info_lvbu;
                break;
            case "司马懿":
                resId = R.drawable.avatar_simayi;
                bgId = R.drawable.bg_simayi;
                moreInfoId = R.string.info_simayi;
                break;
            case "孙策":
                resId = R.drawable.avatar_sunce;
                bgId = R.drawable.bg_sunce;
                moreInfoId = R.string.info_sunce;
                break;
            case "孙权":
                resId = R.drawable.avatar_sunquan;
                bgId = R.drawable.bg_sunquan;
                moreInfoId = R.string.info_sunquan;
                break;
            case "孙尚香":
                resId = R.drawable.avatar_sunshangxiang;
                bgId = R.drawable.bg_sunshangxiang;
                moreInfoId = R.string.info_sunshangxiang;
                break;
            case "文昭皇后":
                resId = R.drawable.avatar_wenzhaohuanghou;
                bgId = R.drawable.bg_wenzhaohuanghou;
                moreInfoId = R.string.info_wenzhaohuanghou;
                break;
            case "张飞":
                resId = R.drawable.avatar_zhangfei;
                bgId = R.drawable.bg_zhangfei;
                moreInfoId = R.string.info_zhangfei;
                break;
            case "周瑜":
                resId = R.drawable.avatar_zhouyu;
                bgId = R.drawable.bg_zhouyu;
                moreInfoId = R.string.info_zhouyu;
                break;
            case "诸葛亮":
                resId = R.drawable.avatar_zhugeliang;
                bgId = R.drawable.bg_zhugeliang;
                moreInfoId = R.string.info_zhugeliang;
                break;
            default:
                switch (sex) {
                    case "男":
                        resId = R.drawable.avatar_nan;
                        bgId = R.drawable.bg_nan;
                        moreInfoId = R.string.info_moren;
                        break;
                    case "女":
                        resId = R.drawable.avatar_nv;
                        bgId = R.drawable.bg_nv;
                        moreInfoId = R.string.info_moren;
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
    //add by aroya
    //editCharacter修改人物信息初始化方法
    public CharacterInfo(int Img, int Icon,
                         String Name,
                         String Sex,
                         String Date,
                         String Origin,
                         String Force,
                         String Info){
        isLike=false;
        resId=Icon;
        bgId=Img;
        name=Name;
        sex=Sex;
        date=Date;
        origin=Origin;
        force=Force;
        //需要解决
        //输入的info为String，不为id，需要处理如何使用String的问题
        moreInfoId = -1;
        info=Info;
        //然后处理下得到相应的letters
        String pinyin = PinyinUtils.getPingYin(Name);
        String sortString = pinyin.substring(0, 1).toUpperCase();
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
    public String getInfo() {return  info; }
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
    public int getMoreInfoIdId(){
        return moreInfoId;
    }
    public String getLetters() {
        return letters;
    }
    public boolean getIsLike(){
        return isLike;
    }
    public void setInfo(String string){info=string;}
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
    public void setMoreInfoId(int MoreInfoId){moreInfoId=MoreInfoId;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        if(moreInfoId==B501) {
            out.writeInt(B501);
            out.writeInt(bgId);
            out.writeInt(resId);
        }
        else out.writeInt(LIXIANG);

        out.writeString(name);
        out.writeString(sex);
        out.writeString(date);
        out.writeString(origin);
        out.writeString(force);
        if(moreInfoId==B501)out.writeString(info);
        out.writeByte((byte)(isLike ? 1:0));
    }

    public static final Parcelable.Creator<CharacterInfo> CREATOR = new Parcelable.Creator<CharacterInfo>() {
        @Override
        public CharacterInfo createFromParcel(Parcel source) {
            CharacterInfo character;
            if(source.readInt()==LIXIANG)
                character = new CharacterInfo(source.readString(), source.readString(), source.readString(), source.readString(), source.readString());
            else character=new CharacterInfo(source.readInt(),source.readInt(),source.readString(), source.readString(), source.readString(), source.readString(), source.readString(),source.readString());
            character.setIsLike(source.readByte() != 0);
            return character;
        }

        @Override
        public CharacterInfo[] newArray(int size) {
            return new CharacterInfo[size];
        }
    };

}
