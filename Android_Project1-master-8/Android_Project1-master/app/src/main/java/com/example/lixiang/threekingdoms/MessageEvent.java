package com.example.lixiang.threekingdoms;

public class MessageEvent {
    private CharacterInfo Info;
    private int action;
    public MessageEvent(CharacterInfo Item, int action){
        this.Info = Item;
        this.action = action;
    }
    public CharacterInfo getCharacterInfo(){
        return Info;
    }
    public int getAction(){
        return action;
    }
}
