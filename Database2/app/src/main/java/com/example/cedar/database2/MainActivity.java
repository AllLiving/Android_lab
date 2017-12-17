package com.example.cedar.database2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.Layout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static Button additem = null;
    private static RecyclerView recyclerview = null;
    private static Member mem = null;
    public static Membase membase = null;
    public static Activity mainact = null;
    private CommonAdapter adapter = null;
    private List<Map<String, Object>> listitem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
        setClick();
        display();
        if (AddActivity.addact != null){
            AddActivity.addact.finish();
        }
    }

    private void Init(){
        mainact = this;
        membase = new Membase(this);
        listitem = new ArrayList<>();
        additem = (Button)findViewById(R.id.add);
        recyclerview = (RecyclerView)findViewById(R.id.idlist);
//        String aname = "aname";
//        String abirth = "abirth";
//        String apresent = "apresent";
//        mem = new Member(0, aname, abirth, apresent);
//        membase.insert(mem);
        ReadMembase();
        WriteAdpt();
    }

    private void ReadMembase(){
        listitem.clear();
        Cursor cursor = membase.getCursor();
        while (cursor.moveToNext()){
            Map<String, Object> tmp = new LinkedHashMap<>();
            tmp.put("name", cursor.getString(1));
            tmp.put("birth", cursor.getString(2));
            tmp.put("present", cursor.getString(3));
            listitem.add(tmp);
        }
    }

    private void WriteAdpt(){
        adapter = new CommonAdapter<Map<String, Object>>(
                this, listitem, R.layout.idlist_item
        ) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> s) {
                TextView name = holder.getView(R.id.name);
                name.setText(s.get("name").toString());
                TextView birth = holder.getView(R.id.birth);
                birth.setText(s.get("birth").toString());
                TextView present = holder.getView(R.id.present);
                present.setText(s.get("present").toString());
            }
            @Override
            public int getItemCount() {
                int cnt = membase.getSize();
                return cnt;
            }
        };
    }

    private void setClick(){
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int position) {
                // Prepare datas for Dialog
                final Map<String, Object> tmp = listitem.get(position);
                final String namedata = tmp.get("name").toString();
                final String birthdata = tmp.get("birth").toString();
                final String presentdata = tmp.get("present").toString();

                LayoutInflater factor = LayoutInflater.from(MainActivity.this);
                View view_in = factor.inflate(R.layout.dialog_update, null);

                final TextInputLayout Namebox = (TextInputLayout)view_in.findViewById(R.id.namelog);
                final TextInputLayout Birthbox = (TextInputLayout)view_in.findViewById(R.id.birthlog);
                final TextInputLayout Presentbox = (TextInputLayout)view_in.findViewById(R.id.presentlog);
                final TextInputLayout Phonebox = (TextInputLayout)view_in.findViewById(R.id.phonelog);
                Namebox.getEditText().setText(namedata);
                Namebox.getEditText().setInputType(InputType.TYPE_NULL);
                Namebox.getEditText().setTextColor(Color.GRAY);
                Birthbox.getEditText().setText(birthdata);
                Presentbox.getEditText().setText(presentdata);

                String phonenum = getPhone(namedata);
                if (phonenum.equals("")){
                    phonenum = "NULL";
                }
                Phonebox.getEditText().setText(phonenum);
                Phonebox.getEditText().setInputType(InputType.TYPE_NULL);
                Phonebox.getEditText().setTextColor(Color.GRAY);

                // Begin set Dialog
                final AlertDialog.Builder updatelog = new AlertDialog.Builder(MainActivity.this);
                updatelog.setTitle("Update details")
                        .setView(view_in)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = Namebox.getEditText().getText().toString();
                                String birth = Birthbox.getEditText().getText().toString();
                                String present = Presentbox.getEditText().getText().toString();
                                Member m = new Member(position, name, birth, present);
                                membase.update(m);
                                ReadMembase();
                                adapter.notifyItemChanged(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplication(), "Why click", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }).show();
            }

            @Override
            public void onLongClick(final int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Delete it?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete(position);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplication(), "Why click long",
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }).show();
            }
        });
    }

    public void display(){
//        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(adapter);
        recyclerview.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
    }

    private void delete(int position){
        Map<String, Object> tmp = listitem.get(position);
        String name = tmp.get("name").toString();
        membase.deleteByName(name);
        adapter.removeItem(position);
        listitem.remove(position);
        Toast.makeText(getApplication(), "Delete done", Toast.LENGTH_SHORT)
                .show();
    }

    private String getPhone(String Name){
        String Number = "";
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        while (cursor.moveToNext()){
            Number = "";
            String contactid = cursor.getString(cursor.getColumnIndex("_id"));
            String display_name = cursor.getString(cursor.getColumnIndex("display_name"));
            if (display_name.equals(Name)){
                int isHas
                        = Integer.parseInt (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts. HAS_PHONE_NUMBER )));
                if (isHas > 0){
                    Cursor phone
                            = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactid,
                            null, null);
                    while(phone.moveToNext()) {
                        Number +=
                                phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + " ";
                    }
                    phone.close();
                    break;
                }
            }
        }
        return Number;
    }
}























