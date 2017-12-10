package com.example.cedar.shareprefer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Cedar on 2017/12/10.
 */

public class EditActivity extends AppCompatActivity{
    private TextInputLayout title = null;
    private Button save = null;
    private Button load = null;
    private Button clear = null;
    private Button delete = null;
    private TextInputLayout editmain = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_main);
        MainActivity.main.finish();

        Init();
        setClick();
    }

    private void Init(){
        title = (TextInputLayout) findViewById(R.id.title);
        save = (Button)findViewById(R.id.save);
        load = (Button)findViewById(R.id.load);
        clear = (Button)findViewById(R.id.clear);
        delete = (Button)findViewById(R.id.delete);
        editmain = (TextInputLayout)findViewById(R.id.editmain);
    }

    private void setClick(){
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = editmain.getEditText();
                editText.setText("");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = title.getEditText().getText().toString()+".txt";
                try{
                    Save(fname);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = title.getEditText().getText().toString()+".txt";
                try{
                    Load(fname);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = title.getEditText().getText().toString() + ".txt";
                try{
                    Delete(fname);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void Save(String fname) throws IOException{
        String ftxt = editmain.getEditText().getText().toString();
        FileOutputStream fos =
                openFileOutput(fname, Context.MODE_PRIVATE);
        fos.write(ftxt.getBytes());
        fos.flush();
        fos.close();
        Toast.makeText(getApplication(), "Save "+fname+" done.", Toast.LENGTH_SHORT)
                .show();
    }

    private void Load(String fname) throws IOException{
        StringBuilder ftxt = new StringBuilder("");
        int len = 0;
        try{
            FileInputStream fis = openFileInput(fname);
            byte[] tmp = new byte[fis.available()];
            while ((len = fis.read(tmp)) > 0){
                ftxt.append(new String(tmp, 0, len));
            }
            fis.close();
            editmain.getEditText().setText(ftxt);
            Toast.makeText(getApplication(), "Get it!", Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e){
            Toast.makeText(getApplication(), "Fail find file "+fname, Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    private void Delete(String fname) throws IOException{
        deleteFile(fname);
        Toast.makeText(getApplication(), "Delete "+fname+" done.", Toast.LENGTH_SHORT)
                .show();
        Load(fname);
    }
}

















