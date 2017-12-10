package com.example.cedar.player;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Cedar on 2017/12/1.
 */

public class PlayerService extends Service{
//    public static MediaPlayer mediaPlayer = null;
    private static final String tag = "PlayerService";
    private String cases = "stop";
    private mybinder binder = new mybinder();
    public static MediaPlayer mp = new MediaPlayer();

    public PlayerService(){
        Log.i(tag, "PlayerService...");
        try {
//            mp.setDataSource(Environment.getExternalStorageDirectory()+"/netease/cloudmusic/Music/Audio Machine - Danuvius.mp3");
            mp.setDataSource(Environment.getExternalStorageDirectory()+"/data/melt.mp3");
            mp.prepare();
            mp.setLooping(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(tag, "OnBinder...");
//      TODO Return the communication channel to the service.
        return binder;
    }



    public class mybinder extends Binder {
//        TODO dealing with code
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code)
            {
                case 101:
                    play();
                    reply.writeString(cases);
                    break;
                case 102:
                    stop();
                    reply.writeString(cases);
                    break;
                case 103:
                    mp.release();
                    break;
                case 104:
                    mp.seekTo(data.readInt());
                    Log.i(tag, "seek succeed");
                    break;
                case 105:
                    reply.writeInt(currentProgress());
                    reply.writeInt(total());
                    reply.writeString(cases);
                    break;
                case 106:
                    if (mp.isPlaying()) reply.writeString("play");
                    else reply.writeString("rest");
                    break;
                default:
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

    public void play(){
        Log.i(tag, "player...");
        if (mp.isPlaying()){
            Log.i(tag, "playing to pause.");
            mp.pause();
            cases = "pause";
        }else {
            mp.start();
            cases = "play";
        }
    }

    public void stop(){
        Log.i(tag, "stop...");
        if (mp != null){
            mp.stop();
            cases = "end";
            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    //返回音乐总长度
    public int total(){
        return mp.getDuration();
    }
    //返回当前进度
    public int currentProgress(){
        return mp.getCurrentPosition();
    }
}
