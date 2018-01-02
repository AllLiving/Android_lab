package com.example.xiayi.qr_project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;   //导入这个包只是为了添加打开相册的功能

public class TestGeneratectivity extends AppCompatActivity {
    private ImageView mQRCodeIv;
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 100; //请求打开相册的请求码
    private ImageView mLogoIv;
    private EditText mTextEdit;
//    private ImageView mChineseLogoIv;
//    private ImageView mEnglishLogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_generate);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        initView();
        //createQRCode();
    }

    //初始化视图
    private void initView() {
        mQRCodeIv = (ImageView) findViewById(R.id.iv_chinese);
        mLogoIv = (ImageView) findViewById(R.id.iv_logo);
        mTextEdit = (EditText) findViewById(R.id.editText);
    }

    //初始化输入框
    public void inputText(View v){
        mTextEdit.setText("");
    }

    //生成二维码
    public void encodeQR(View v) {
        mLogoIv.setDrawingCacheEnabled(false);
        mLogoIv.setDrawingCacheEnabled(true);
        Bitmap bitmap = mLogoIv.getDrawingCache();
        String inputText = mTextEdit.getText().toString();
        createQRCode(inputText, bitmap, "生成二维码失败");
    }

    //根据参数用子线程生成二维码
    private void createQRCode(final String inputText, final Bitmap logoBitmap, final String errorTip) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(inputText, BGAQRCodeUtil.dp2px(TestGeneratectivity.this, 150), Color.BLACK, Color.WHITE, logoBitmap); //
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    mQRCodeIv.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(TestGeneratectivity.this, "生成中文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    //点击解析二维码
    public void decodeQR(View v) {
        mQRCodeIv.setDrawingCacheEnabled(false);
        mQRCodeIv.setDrawingCacheEnabled(true);
        Bitmap bitmap = mQRCodeIv.getDrawingCache();
        decodeQRCode(bitmap, "解析中文二维码失败");
    }

    //用子线程去解析二维码
    private void decodeQRCode(final Bitmap bitmap, final String errorTip) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
         */
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return QRCodeDecoder.syncDecodeQRCode(bitmap);
            }

            @Override
            protected void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(TestGeneratectivity.this, errorTip, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TestGeneratectivity.this, result, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    //从相册载入logo
    public void downloadLogo(View v){
        startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
    }

    //显示logo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
            //Toast.makeText(TestGeneratectivity.this, picturePath, Toast.LENGTH_SHORT).show();
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picturePath, options);
                int sampleSize = options.outHeight / 400;
                if (sampleSize <= 0) {
                    sampleSize = 1;
                }
                options.inSampleSize = sampleSize;
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
                mLogoIv.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(TestGeneratectivity.this, "加载logo失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}