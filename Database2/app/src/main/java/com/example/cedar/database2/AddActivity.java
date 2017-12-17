package com.example.cedar.database2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Cedar on 2017/12/14.
 */

public class AddActivity extends AppCompatActivity {

    private TextInputLayout Namebox = null, Birthbox = null, Presentbox = null;
    private Button button = null;
    public static Activity addact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Init();
        setClick();
    }

    private void Init(){
        addact = this;
        Namebox = (TextInputLayout)findViewById(R.id.name);
        Birthbox = (TextInputLayout)findViewById(R.id.birth);
        Presentbox = (TextInputLayout)findViewById(R.id.present);
        button = (Button)findViewById(R.id.add);
    }

    private void setClick(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Namebox.getEditText().getText().toString();
                String birth = Birthbox.getEditText().getText().toString();
                String present = Presentbox.getEditText().getText().toString();

                if (TextUtils.isEmpty(name)){
                    Namebox.setErrorEnabled(true);
                    Namebox.setError("Name cannot be empty");
                }else {
                    Namebox.setErrorEnabled(false);
                    if (MainActivity.membase.getByName(name) != null){
                        Toast.makeText(getApplication(), "The one exists", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Member m = new Member(MainActivity.membase.getSize()+1,
                                name, birth, present);
                        MainActivity.membase.insert(m);
                        MainActivity.mainact.finish();

                        Intent intent = new Intent(AddActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}















