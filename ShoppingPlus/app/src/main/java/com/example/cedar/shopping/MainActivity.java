package com.example.cedar.shopping;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

import static com.example.cedar.shopping.R.id.first_letter;

public class MainActivity extends AppCompatActivity {

    public static List<Map<String, Object>> cart_goods = new ArrayList<>();
    public static boolean[] Tag = new boolean[100];
    public static final String[] Name = new String[] {"Arla Milk", "Borggreve", "Devondale Milk", "Enchated Forest",
            "Ferrero Rocher", "Kindle Oasis", "Lindt", "Maltesers", "Mcvitie's 饼干", "waitrose 早餐麦片"};
    public static final String[] Letter = new String[] {"A", "B", "D","E", "F", "K", "L", "M", "M", "W"};
    public static final String[] Price = new String[]{"¥ 59.00","¥ 28.90","¥ 79.00","¥ 5.00","¥ 132.59",
            "¥ 2399.00","¥ 139.43","¥ 141.43","¥ 14.90","¥ 179.00"};
    public static final String[] information
            = new String[]{"产地  德国","重量  640g","产地  澳大利亚", "作者  Johanna Basford",
            "重量  300g","版本  8GB","重量  249g", "重量  118g","产地  英国","重量  2Kg"};
    int pstforcart = 0;
    int[] index_list = new int[100];
    private DynamicBroadcastReceiver dynamicBroadcastReceiver = null;
    public static SimpleAdapter simpleAdapter=null;
    public static RecyclerView recyclerView=null;
    public static ListView cart_list=null;
    public static int crt_cartindex = 0;
    private boolean cart_attitude=false;
    private FloatingActionButton cart=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        index_list = GoodsDetail.cart_index;
        for (int i=0; i<100; i++){
            Tag[i] = false;
        }

        final List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i=0; i<10; i++){
            Map<String, Object> tmp = new LinkedHashMap<>();
            tmp.put("name", Name[i]);
            tmp.put("first_letter", Letter[i]);
            listItems.add(tmp);
        }
        final CommonAdapter adapter = new CommonAdapter<Map<String, Object>>
                (this, listItems, R.layout.goods_list_item) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> s) {
                TextView name = holder.getView(R.id.name);
                name.setText(s.get("name").toString());
                TextView first = holder.getView(first_letter);
                first.setText(s.get("first_letter").toString());
            }
        };

        TypedArray imagevec = getResources().obtainTypedArray(R.array.image_vector);
        final int[] ImageId = new int[imagevec.length()];
        for (int i=0; i<imagevec.length(); i++){
            ImageId[i] = imagevec.getResourceId(i, 0);
        }
        imagevec.recycle();


        final int[] goodid = new int[100];
        for(int i=0; i<100; i++){
            goodid[i] = i;
        }
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener(){
            @Override
            public void onLongClick(int position){
                adapter.removeItem(position);
                for(int i=position; i<9; i++){
                    Tag[i] = Tag[i+1];
                    Name[i] = Name[i+1];
                    Price[i] = Price[i+1];
                    Letter[i] = Letter[i+1];
                    ImageId[i] = ImageId[i+1];
                    information[i] = information[i+1];
                }
                Toast.makeText(getApplication(), "Removing the "+position+"st item", Toast.LENGTH_SHORT)
                     .show();
            }
            @Override
            public void onClick(int position){
                Bundle bundle = new Bundle();
                bundle.putInt("good_id", goodid[position]);
                bundle.putString("good_name", Name[position]);
                bundle.putInt("good_image", ImageId[position]);
                bundle.putString("good_price", Price[position]);
                bundle.putString("good_letter", Letter[position]);
                bundle.putString("Information", information[position]);
                Intent intent = new Intent(MainActivity.this, GoodsDetail.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        } );

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());


        cart = (FloatingActionButton) findViewById(R.id.cart);
        Map<String, Object> cart_goods_tmp = new LinkedHashMap<>();
        cart_goods_tmp.put("cart_firstletter", "*");
        cart_goods_tmp.put("cart_name", "cart");
        cart_goods_tmp.put("cart_price", "PRICE");
        cart_goods.add(cart_goods_tmp);
        simpleAdapter = new SimpleAdapter(this, cart_goods, R.layout.shoping_cart,
                    new String[]{"cart_firstletter", "cart_name", "cart_price"},
                    new int[] {R.id.cart_firstletter, R.id.cart_name, R.id.cart_price});

        cart_list = (ListView)findViewById(R.id.cart_list);
        cart_list.setAdapter(simpleAdapter);
        cart_list.setVisibility(View.INVISIBLE);

        Intent from_dynamic = getIntent();
        cart_attitude = from_dynamic.getBooleanExtra("cart_attitude", true);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cart_attitude == false){
                    recyclerView.setVisibility(View.INVISIBLE);
                    cart_list.setVisibility(View.VISIBLE);
                    cart.setImageDrawable(getResources().getDrawable(R.mipmap.mainpage));
                    cart_attitude = true;
                }else if(cart_attitude == true){
                    recyclerView.setVisibility(View.VISIBLE);
                    cart_list.setVisibility(View.INVISIBLE);
                    cart.setImageDrawable(getResources().getDrawable(R.mipmap.shoplist));
                    cart_attitude = false;
                }
            }
        });

        cart_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                crt_cartindex = position;
                pstforcart = index_list[position];
                AlertDialog.Builder deletelog = new AlertDialog.Builder(MainActivity.this);
                if(position > 0){
                    deletelog.setTitle("Delete Items.");
                    deletelog.setMessage("Delete "+ pstforcart + Name[pstforcart] + " from the cart?");
                    deletelog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cart_goods.remove(position);
                            for (int i=position; i<9; i++){
                                index_list[i] = index_list[i+1];
                            }
                            simpleAdapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    }).show();
                }
                return true;
            }
        });

        cart_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pstforcart = index_list[position];
                Toast.makeText(getApplication(), "Go from cart"+pstforcart+"...", Toast.LENGTH_SHORT)
                        .show();
                if(position > 0){
                    if(MainActivity.Tag[pstforcart] == false){
                        GoodsDetail.star.setImageDrawable(getResources().getDrawable(R.mipmap.empty_star));
                    }else{
                        GoodsDetail.star.setImageDrawable(getResources().getDrawable(R.mipmap.full_star));
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("good_id", goodid[pstforcart]);
                    bundle.putString("good_name", Name[pstforcart]);
                    bundle.putInt("good_image", ImageId[pstforcart]);
                    bundle.putString("good_price", Price[pstforcart]);
                    bundle.putString("good_letter", Letter[pstforcart]);
                    bundle.putString("Information", information[pstforcart]);
                    Intent intent = new Intent(MainActivity.this, GoodsDetail.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        String STATICATION = "staticbroadcast";
        Intent staticintent = new Intent(STATICATION);
        Bundle bundle = new Bundle();
        Random random = new Random();
        int note_id = random.nextInt(10);
        bundle.putInt("note_id", goodid[note_id]);
        bundle.putString("note_name", Name[note_id]);
        bundle.putInt("note_image", ImageId[note_id]);
        bundle.putString("note_price", Price[note_id]);
        bundle.putString("note_letter", Letter[note_id]);
        bundle.putString("note_info", information[note_id]);
        staticintent.putExtras(bundle);
        sendBroadcast(staticintent);

        dynamicBroadcastReceiver
                = new DynamicBroadcastReceiver();

        IntentFilter dynamic_filter = new IntentFilter("dynamicbroadcast");
        registerReceiver(dynamicBroadcastReceiver, dynamic_filter);



    }
    @Override
    protected void onNewIntent(Intent intent){
        recyclerView.setVisibility(View.INVISIBLE);
        cart_list.setVisibility(View.VISIBLE);
        cart.setImageDrawable(getResources().getDrawable(R.mipmap.mainpage));
        cart_attitude = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dynamicBroadcastReceiver);
    }
}