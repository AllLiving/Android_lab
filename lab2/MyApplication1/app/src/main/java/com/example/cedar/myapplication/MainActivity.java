package com.example.cedar.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RadioButton stu, tea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stu = (RadioButton) findViewById(R.id.radioButton);
        stu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, R.string.stu_msg, Snackbar.LENGTH_SHORT)
                        .setAction("I know", new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplication(), "I know you got it",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .show();
            }
        });

        tea = (RadioButton) findViewById(R.id.radioButton2);
        tea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, R.string.tea_msg, Snackbar.LENGTH_SHORT)
                        .setAction("I know", new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplication(), "I know you got it",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .show();
            }
        });
    }

    public void Login_click(View target){
        Button login = (Button) findViewById(R.id.login);
        final TextInputLayout NumberBox = (TextInputLayout) findViewById(R.id.sidbox);
        final TextInputLayout PassBox = (TextInputLayout) findViewById(R.id.codebox);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText number = NumberBox.getEditText();
                EditText passwd = PassBox.getEditText();
                if( TextUtils.isEmpty(number.getText()) ){
                    NumberBox.setErrorEnabled(true);
                    NumberBox.setError("sid cannot be null");
                }else{
                    NumberBox.setErrorEnabled(false);
                }
                if( TextUtils.isEmpty(passwd.getText()) ){
                    PassBox.setErrorEnabled(true);
                    PassBox.setError("Passwd cannot be null");
                }else{
                    PassBox.setErrorEnabled(false);
                }
                if( !TextUtils.isEmpty(number.getText()) &&
                    !TextUtils.isEmpty(passwd.getText())){
                    if(TextUtils.equals(number.getText(), "123456") &&
                        TextUtils.equals(passwd.getText(), "6666")){
                        Snackbar.make(v, "LOGIN", Snackbar.LENGTH_SHORT)
                                .show();
                    }else{
                        Snackbar.make(v, "Something wrong, not fit", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });
    }

    void SelectPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
    void TakePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    public void Register_click(View target){
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final RadioButton student = (RadioButton) findViewById(R.id.radioButton);
        final RadioButton teacher = (RadioButton) findViewById(R.id.radioButton2);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes final int checkedId) {
                Button register = (Button) findViewById(R.id.register);
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkedId == student.getId()){
                            Toast.makeText(getApplication(), "student has not readied yet",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }else if(checkedId == teacher.getId()){
                            Toast.makeText(getApplication(), "Teacher has not prepared yet",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
            }
        });
    }

    public void click_image(View target){
        ImageView image = (ImageView) findViewById(R.id.imageview);
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder Dialog = new AlertDialog.Builder(MainActivity.this);
                Dialog.setTitle("Change portrait");
                final String[] items = {"shoot now", "select from album"};
                Dialog.setItems(items , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplication(), "you decided to " + items[which], Toast.LENGTH_SHORT)
                                .show();
                        if(which == 1){
                            TakePhoto();
                        }else{
                            SelectPhoto();
                        }
                    }
                });

                Dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplication(), "you agree", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
                Dialog.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplication(), "you reject", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
                Dialog.show();
            }
        });
    }
}