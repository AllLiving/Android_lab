package com.burytomorrow.mymusicplayer;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class MyMusicPlayer extends AppCompatActivity {
    Intent intent;
    private IBinder mIBinder;//进程间通信Parcel对象的传递主要靠IBinder接口；
    private ServiceConnection sc;
    private MusicService musicService;
    private static String[] PERMISSIONS_STORAGE ={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ObjectAnimator animator;
    //设置音乐播放进度
    private static SeekBar seekbar;
    private static TextView progress;//当前进度
    private static TextView total;//总进度
    //按钮定义
    private Button playOrPause;//播放暂停按钮
    private Button stop;//停止按钮
    private Button quit;//退出按钮
    private TextView state;//播放状态
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    //权限申请
    private static boolean hasPermission = false;
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_music_player);

        verifyStoragePermissions(MyMusicPlayer.this);
        //musicService = new MusicService();
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                //通过IBinder获得Service对象，实现Activity和Service的绑定
                Log.d("service","connected");
                mIBinder = iBinder;
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                sc = null;
            }
        };
        //bindServiceConnection();//Activity启动后绑定Service
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);
        init();//初始化
        //图片旋转
        ImageView image = (ImageView)findViewById(R.id.picture);
        animator = ObjectAnimator.ofFloat(image, "rotation", 0f, 360.0f);
        animator.setDuration(10000);//设置动画时间
        animator.setInterpolator(new LinearInterpolator());//设置动画插值
        animator.setRepeatCount(ValueAnimator.INFINITE);//设置动画重复次数

        listener();//三个按钮的触发事件
        runHandler();//运行另一个线程
    }

    public void init(){
        //进度条定义
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        progress = (TextView) findViewById(R.id.progress);
        total = (TextView) findViewById(R.id.total_time);

        //按钮定义
        playOrPause = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        quit = (Button) findViewById(R.id.quit);
        state = (TextView) findViewById(R.id.state);
    }

    public void listener(){
        //播放暂停按钮的触发事件
        playOrPause.setOnClickListener(new Button.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v){
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try{
                    mIBinder.transact(101,data,reply,0);//code==101为播放按钮
                    String s=reply.readString();
                    state.setText(s);
                    if(s.equals("Playing")){//如果是播放状态
                        if(flag == false){
                            flag = true;
                            animator.start();
                        }else{
                            animator.resume();
                        }
                        playOrPause.setText("PAUSE");
                    }else{//如果是暂停状态
                        animator.pause();
                        playOrPause.setText("PLAY");
                    }
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });

        //停止按钮的触发事件
        stop.setOnClickListener(new Button.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v){
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try{
                    flag = false;
                    animator.start();
                    animator.pause();
                    mIBinder.transact(102,data,reply,0);//code==102为停止
                    state.setText(reply.readString());
                    playOrPause.setText("PLAY");
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });

        //退出按钮的触发事件
        quit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try{
                    //解除绑定
                    unbindService(sc);
                    sc = null;
                    mIBinder.transact(103,data,reply,0);//code==103为退出
                    MyMusicPlayer.this.finish();
                    System.exit(0);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){
                if(fromUser){
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    try{
                        data.writeInt(progress);
                        mIBinder.transact(105,data,reply,0);//code==105为改变歌曲播放进度
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){}
        });
    }

    public void runHandler(){
        final Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch (msg.what){
                    case 123:
                        //UI更新
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        try{
                            mIBinder.transact(104,data,reply,0);//code==104为更新UI
                            int currentP = reply.readInt();
                            int total_time = reply.readInt();
                            progress.setText(time.format(currentP));
                            total.setText(time.format(total_time));//设置时间
                            seekbar.setProgress(currentP);
                            seekbar.setMax(total_time);//更新进度条的位置
                        }catch (RemoteException e){
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };

        Thread mThread = new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        Thread.sleep(100);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    if(sc!=null&&hasPermission == true){
                        mHandler.obtainMessage(123).sendToTarget();
                    }
                }
            }
        };
        mThread.start();
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
}
