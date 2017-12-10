package com.example.cedar.shareprefer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    public static int MODE = MODE_PRIVATE;
    public static final String PREFRENCE_NAME = "PwdSave";
    public static final String LOGIN_PREFE = "Login";
    public static String Hint = "";
    SharedPreferences sharedPreferences = null;
    SharedPreferences loginPrefe = null;
    SharedPreferences.Editor editor = null;
    private static TextInputLayout NameBox = null;
    private static TextInputLayout PswdBox = null;
    private static Button done = null;
    private static Button reset = null;
    public static Activity main=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Hint = "Password set done!";
        main = this;

        sharedPreferences = getSharedPreferences(PREFRENCE_NAME, MODE);
        loginPrefe = getSharedPreferences(PREFRENCE_NAME, MODE);
//        JumpAct();

        Init();
        editor = sharedPreferences.edit();
        setlistener();
        editor.apply();
    }

    private void Init(){
        done = (Button)findViewById(R.id.register);
        NameBox = (TextInputLayout) findViewById(R.id.name);
        PswdBox = (TextInputLayout)findViewById(R.id.password);
        reset = (Button)findViewById(R.id.reset);

        String key = sharedPreferences.getString("password", "NoKey");
        Intent jump = getIntent();
        boolean j = jump.getBooleanExtra("ResetPassword", true);
        if (!key.equals("NoKey") && j){
            NameBox.setVisibility(View.INVISIBLE);
            Hint = "Login~";
            NameBox.getEditText().setText(
                        sharedPreferences.getString("password", "NoKey")
            );
        }
    }

    private void setlistener(){
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pass = NameBox.getEditText();
                EditText cnfm = PswdBox.getEditText();
                if (TextUtils.isEmpty(pass.getText())){
                    NameBox.setErrorEnabled(true);
                    NameBox.setError("Password cannot be empty!");
                    Toast.makeText(getApplication(), "Password cannot be empty!", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    NameBox.setErrorEnabled(false);
                }
                if (TextUtils.isEmpty(cnfm.getText())){
                    PswdBox.setErrorEnabled(true);
                    PswdBox.setError("Password cannot be empty!");
                    Toast.makeText(getApplication(), "Password cannot be empty!", Toast.LENGTH_SHORT)
                            .show();
                }else {
                    PswdBox.setErrorEnabled(false);
                }
                if ( !TextUtils.isEmpty(pass.getText()) &&
                        !TextUtils.isEmpty(cnfm.getText())){
                    if (TextUtils.equals(pass.getText(), cnfm.getText())){
                        Toast.makeText(getApplication(), Hint, Toast.LENGTH_SHORT)
                                .show();
                        editor.putString("password", pass.getText().toString());
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplication(), "Mismatch password", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pass = NameBox.getEditText();
                EditText cnfm = PswdBox.getEditText();
                pass.setText("");
                cnfm.setText("");
            }
        });

        reset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NameBox.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    private void JumpAct(){
        String key = sharedPreferences.getString("password", "NoKey");
        Intent jump = getIntent();
        boolean j = jump.getBooleanExtra("ResetPassword", true);
        if (!key.equals("NoKey") && j){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}









