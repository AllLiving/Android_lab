package com.burytomorrow.mymusicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by going on 2017/11/13.
 */

public class MusicService extends Service {
    public static MediaPlayer mPlayer = new MediaPlayer();
    public final IBinder binder = new MyBinder();
    private String state;
    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException{
            switch (code)
            {
                case 101:
                    //播放按钮，服务处理函数
                    play();
                    reply.writeString(state);//返回播放器的状态
                    break;
                case 102:
                    //停止按钮，服务处理函数
                    stop();
                    reply.writeString(state);
                    break;
                case 103:
                    //退出按钮，服务处理函数
                    exit();
                    break;
                case 104:
                    //界面刷新，服务返回数据函数
                    reply.writeInt(currentProgress());
                    reply.writeInt(total());
                    break;
                case 105:
                    //拖动进度条，服务处理函数
                    int p=data.readInt();
                    seekTo(p);
                    break;
                default:
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }

        MusicService getService(){
            return MusicService.this;
        }
    }

    public MusicService(){
        try{
            mPlayer.setDataSource(Environment.getExternalStorageDirectory()+"/netease/cloudmusic/Music/You Are Free - Lifeline.mp3");
            mPlayer.prepare();
            mPlayer.setLooping(true);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public void exit(){
        mPlayer.release();
    }
    //音乐播放或暂停
    public void play(){
        if(mPlayer.isPlaying()){//如果正在播放则暂停
            mPlayer.pause();
            state = "Pause";
            System.out.println("这里能播放");
        }else{//否则就开始播放
            mPlayer.start();
            state = "Playing";
        }
    }
    //音乐停止
    public void stop(){
        if(mPlayer!=null){
            mPlayer.stop();
            state = "Stopped";
            try {
                mPlayer.prepare();
                mPlayer.seekTo(0);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //设置音乐的播放位置
    public void seekTo(int progress){
        mPlayer.seekTo(progress);
    }
    //返回音乐总长度
    public int total(){
        return mPlayer.getDuration();
    }
    //返回当前进度
    public int currentProgress(){
        return mPlayer.getCurrentPosition();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

