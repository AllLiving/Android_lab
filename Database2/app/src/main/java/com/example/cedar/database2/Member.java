package com.example.cedar.database2;

/**
 * Created by Cedar on 2017/12/14.
 */

public class Member {
    private Integer ID;
    private String name;
    private String birth;
    private String info;

    public Member(){}
    public Member(Integer id, String name, String birth, String info){
        this.ID = id;
        this.name = name;
        this.birth = birth;
        this.info = info;
    }

    public Integer getID(){
        return  ID;
    }
    public void setID(Integer id){
        ID = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getBirth(){
        return birth;
    }
    public void setBirth(String birth){
        this.birth = birth;
    }
    public String getInfo(){
        return info;
    }
    public void setInfo(String INFO){
        info = INFO;
    }

}
