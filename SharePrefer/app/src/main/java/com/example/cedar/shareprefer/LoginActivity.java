package com.example.cedar.shareprefer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Cedar on 2017/12/10.
 */

public class LoginActivity extends AppCompatActivity{
    public static int MODE = MODE_PRIVATE;
    public static TextInputLayout PSD = null;
    public Button login = null;
    private Button clear = null;
    private SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle loginactivity){
        super.onCreate(loginactivity);
        setContentView(R.layout.login_main);
        MainActivity.main.finish();

        Init();
        setClick();
    }

    private void Init(){
        PSD = (TextInputLayout)findViewById(R.id.login_password);
        login = (Button)findViewById(R.id.login);
        clear = (Button)findViewById(R.id.clear);
        sharedPreferences = getSharedPreferences("PwdSave", MODE);
    }

    private void setClick(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText psd = PSD.getEditText();
                if (TextUtils.isEmpty(psd.getText())){
                    Toast.makeText(getApplication(), "Password cannot be empty!", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    String curpsd = psd.getText().toString();
                    String setpsd = sharedPreferences.getString("password", "Nokey");
                    if (setpsd.equals("NoKey")){
                        Toast.makeText(getApplication(), "Serious Error!", Toast.LENGTH_LONG)
                                .show();
                        finish();
                    }else if (curpsd.equals(setpsd)){
                        Toast.makeText(getApplication(), "Login~", Toast.LENGTH_SHORT)
                                .show();
                        Intent intent = new Intent(LoginActivity.this, EditActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplication(), "Wrong password", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText psd = PSD.getEditText();
                psd.setText("");
            }
        });
        clear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("ResetPassword", false);
                startActivity(intent);
                return false;
            }
        });
    }
}
















