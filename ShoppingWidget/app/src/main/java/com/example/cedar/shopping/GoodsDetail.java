package com.example.cedar.shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 * Created by Cedar on 2017/10/23.
 */

public class GoodsDetail extends AppCompatActivity {
    public static int[] cart_index = new int[100];
    public static ImageView star = null;

    @Override
    protected void onCreate(Bundle goodsdetail){
        super.onCreate(goodsdetail);
        setContentView(R.layout.goods);

        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        final int goodid = bundle.getInt("good_id");


        TextView getName = (TextView)findViewById(R.id.goodname);
        getName.setText(MainActivity.Name[goodid]);
        TextView getPrice = (TextView)findViewById(R.id.goodprice);
        getPrice.setText(MainActivity.Price[goodid]);
        TextView getInform = (TextView)findViewById(R.id.goodinform);
        getInform.setText(MainActivity.information[goodid]);

        String[] option_text = new String[]{"Take it","Share it","Unlike it","For more"};
        List<Map<String, Object>> options = new ArrayList<>();
        for(int i=0; i<4; i++){
            Map<String, Object> tmp = new LinkedHashMap<>();
            tmp.put("options", option_text[i]);
            options.add(tmp);
        }
        ListView goodorder = (ListView)findViewById(R.id.goodorder);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, options, R.layout.item,
                                        new String[]{"options"}, new int[]{R.id.options});
        goodorder.setAdapter(simpleAdapter);


        final int imageId = bundle.getInt("good_image");
        ImageView goodpic = (ImageView)findViewById(R.id.goodpic);
        goodpic.setImageResource(imageId);


        star = (ImageView)findViewById(R.id.star);
        if(MainActivity.Tag[goodid] == false){
            star.setImageDrawable(getResources().getDrawable(R.mipmap.empty_star));
        }else{
            star.setImageDrawable(getResources().getDrawable(R.mipmap.full_star));
        }
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.Tag[goodid] == true){
                    star.setImageDrawable(getResources().getDrawable(R.mipmap.empty_star));
                    MainActivity.Tag[goodid] = false;
                }else{
                    star.setImageDrawable(getResources().getDrawable(R.mipmap.full_star));
                    MainActivity.Tag[goodid] = true;
                }
            }
        });

        ImageView cart_collect = (ImageView)findViewById(R.id.cart_collect);
        cart_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Put it into cart done", Toast.LENGTH_SHORT)
                        .show();
                Map<String, Object> tmp = new LinkedHashMap<>();
                cart_index[MainActivity.crt_cartindex+1] = goodid;
                MainActivity.crt_cartindex++;
                tmp.put("cart_firstletter", MainActivity.Letter[goodid]);
                tmp.put("cart_name", MainActivity.Name[goodid]);
                tmp.put("cart_price", MainActivity.Price[goodid]);
                MainActivity.cart_goods.add(tmp);
                MainActivity.simpleAdapter.notifyDataSetChanged();

                Intent dynamic_intent = new Intent("dynamicbroadcast");
                dynamic_intent.putExtras(intent);
                sendBroadcast(dynamic_intent);

                String WIDGET_DYNAMIC = "android.appwidget.action.dynamic.APPWIDGET_UPDATE";
                Intent dynamic_widget = new Intent(WIDGET_DYNAMIC);
                dynamic_widget.putExtras(intent);
                sendBroadcast(dynamic_widget);
            }
        });

        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsDetail.this.setResult(0, intent);
                GoodsDetail.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}