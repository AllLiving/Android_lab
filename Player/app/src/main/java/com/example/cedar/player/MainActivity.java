package com.example.cedar.player;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView runtime, sumtime;
    private ImageView previous, pause, stop, next, plate;
    private boolean isplaying;
    private static String[] PERMISSIONS_STORAGE ={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static boolean hasPermission = false;
    private SeekBar seekBar;
    private Toolbar toolbar;
    private ServiceConnection serviceConnection;
    private IBinder binder;
    private ObjectAnimator animator;
    private String states = "stop";
    private String tag = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (PlayerService.mp.isPlaying())   states = "play";
        if (states.equals("stop"))  setContentView(R.layout.activity_main);
        Log.i("PlayerService", "MusicActivityonCreate...");
        verifyStoragePermissions(MainActivity.this);

        init();
        checkClick();
        checkChange();
        updateUI();
        String tmp = states;
    }

    public void init(){
        runtime = (TextView) findViewById(R.id.process);
        sumtime = (TextView) findViewById(R.id.sumtime);
        previous = (ImageView) findViewById(R.id.previous);
        pause = (ImageView) findViewById(R.id.play_pause);
        stop = (ImageView) findViewById(R.id.stop);
        next = (ImageView) findViewById(R.id.next);
        plate = (ImageView) findViewById(R.id.plate_img);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        isplaying = false;

        setplate();
        pause.setBackgroundResource(R.drawable.play);
        if (PlayerService.mp.isPlaying()){
            Toast.makeText(getApplication(), "Welcome back", Toast.LENGTH_SHORT)
                    .show();
            states = "play";// hard resources
            isplaying = true;
        }

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(tag, "onServiceConnected");
                binder = service;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceConnection = null;
            }
        };

        Intent intent = new Intent(this, PlayerService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void checkClick(){
        pause.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try{
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    binder.transact(101, data, reply, 0);
                    if (isplaying){
                        pause.setBackgroundResource(R.drawable.play);
                        if (animator.isRunning()){
                            animator.pause();//
                        }
                        states = "pause";
                        isplaying = false;
                        seekBar.setVisibility(View.VISIBLE);
                    }else{
                        pause.setBackgroundResource(R.drawable.pause);
                        if (states.equals("pause")){
                            if(animator.isPaused()){
                                animator.resume();
                            }else {
                                animator.start();
                            }
                        }else if(states.equals("stop")) {
                            animator.start();
                        }
                        isplaying = true;
                        states = "play";
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    if (!states.equals("stop")){
                        binder.transact(102, Parcel.obtain(), Parcel.obtain(), 0);
                        animator.end();
                        pause.setBackgroundResource(R.drawable.play);
                        isplaying = false;
                        states = "stop";
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    quitservice();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkChange(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int duration=0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    duration=(seekBar.getMax() * 4 * progress)/1000;
                    runtime.setText(duration/60 +":"+ duration%60);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                duration = seekBar.getProgress();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Parcel data = Parcel.obtain();
                try {
                    data.writeInt(1000*duration);
                    runtime.setText(duration/60 +":"+ duration%60);
                    binder.transact(104, data, Parcel.obtain(), 0);
                    updateUI();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setplate(){
        ImageView imageView = (ImageView) findViewById(R.id.plate_img);
        animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());//设置动画匀速运动method
        animator.setRepeatCount(ValueAnimator.INFINITE);
    }

    public void quitservice(){
        unbindService(serviceConnection);
        serviceConnection = null;
        try{
            binder.transact(103, Parcel.obtain(), Parcel.obtain(), 0);
            MainActivity.this.finish();
            System.exit(0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateUI(){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 111:
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        try {
                            binder.transact(105, data, reply, 0);
                            int currentP = reply.readInt()/1000;
                            int total_time = reply.readInt()/1000;
                            String cases = reply.readString();
                            runtime.setText(currentP/60 +":"+ currentP%60);
                            sumtime.setText(total_time/60 +":"+ total_time%60);
                            seekBar.setProgress(currentP);
                            seekBar.setMax(total_time);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    try {
                        Thread.sleep(100);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    if (serviceConnection!= null&&hasPermission){
                        handler.obtainMessage(111).sendToTarget();
                    }
                }
            }
        };
        thread.start();
    }
    //权限确认函数
    public static void verifyStoragePermissions(Activity activity){
        try{
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE");
            if(permission != PackageManager.PERMISSION_GRANTED){
                //没有读取的权限，去申请读取的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
            }
            else{
                hasPermission = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if(grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //权限已被用户同意，可以做你要做的事情了
        }
        else{
            //权限被用户拒绝，可以提示用户，关闭界面等等
            System.exit(0);
        }
    }
    @Override
    public void onBackPressed(){
        moveTaskToBack(false);
    }
}











