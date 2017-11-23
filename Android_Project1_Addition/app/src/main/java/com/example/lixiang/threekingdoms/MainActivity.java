package com.example.lixiang.threekingdoms;

import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<CharacterInfo> character_list;
    private List<CharacterInfo> character_favo;
    private Fragment[] fragments;
    private int selectedIndex;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchContent(0);
                    setTitle("三国主页");
                    return true;
                case R.id.navigation_search:
                    switchContent(1);
                    setTitle("搜索人物");
                    return true;
                case R.id.navigation_favorite:
                    switchContent(2);
                    setTitle("我的收藏");
                    return true;
            }
            return false;
        }

    };

    private void switchContent(int index){
        if(selectedIndex!=index) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, fragments[index])
                    .commit();
            selectedIndex = index;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("三国主页");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EventBus.getDefault().register(this);

        final String[] Name = new String[]{
                "蔡琰",
                "曹操",
                "大乔",
                "典韦",
                "貂蝉",
                "关羽",
                "刘备",
                "吕布",
                "司马懿",
                "孙策",
                "孙权",
                "孙尚香",
                "文昭皇后",
                "张飞",
                "周瑜",
                "诸葛亮"
        };
        final String[] Sex = new String[]{
                "女",
                "男",
                "女",
                "男",
                "女",
                "男",
                "男",
                "男",
                "男",
                "男",
                "男",
                "女",
                "女",
                "男",
                "男",
                "男"
        };
        final String[] Date = new String[]{
                "不详",
                "(155 - 220)",
                "不详",
                "(? - 197)",
                "虚构人物",
                "(? - 219)",
                "(161 - 223)",
                "(? - 198)",
                "(179 - 251)",
                "(175 - 200)",
                "(182 - 252)",
                "不详",
                "(183 - 221)",
                "不详",
                "(175 - 210)",
                "(181 - 234)"
        };
        final String[] Origin = new String[]{
                "兖州陈留郡圉（河南开封市杞县南二十五公里）",
                "豫州沛国谯（安徽亳州市亳县）",
                "扬州庐江郡皖（安徽安庆市潜山县）",
                "兖州陈留郡己吾（河南商丘市宁陵县西南二十五公里）",
                "虚构人物",
                "司隶河东郡解（山西运城市临猗县西南）",
                "幽州涿郡涿（河北保定市涿州）",
                "并州五原郡九原（内蒙古包头市九原区麻池镇西北古城遗址）",
                "司隶河内郡温（河南焦作市温县西）",
                "扬州吴郡富春（浙江杭州市富阳）",
                "扬州吴郡富春（浙江杭州市富阳）",
                "扬州吴郡富春（浙江杭州市富阳）",
                "冀州中山国毋极（河北石家庄市无极县西、滋水北岸）",
                "幽州涿郡（河北保定市涿州）",
                "扬州庐江郡舒（安徽合肥市庐江县西南）",
                "徐州琅邪国阳都（山东临沂市沂南县南）"
        };
        final String[] Force = new String[]{
                "魏",
                "魏",
                "吴",
                "魏",
                "东汉",
                "蜀",
                "蜀",
                "东汉",
                "魏",
                "吴",
                "吴",
                "吴",
                "魏",
                "蜀",
                "吴",
                "蜀"
        };

        character_list = new ArrayList<CharacterInfo>();
        character_favo = new ArrayList<CharacterInfo>();

        for (int i = 0; i < Name.length; i++) {
            character_list.add(new CharacterInfo(Name[i],  Sex[i], Date[i], Origin[i], Force[i]));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment homeFragment = HomeFragment.newInstance(character_list);
        Fragment searchFragment = SearchFragment.newInstance(character_list);
        Fragment favoriteFragment = FavoriteFragment.newInstance(character_favo);
        fragments = new Fragment[]{homeFragment, searchFragment, favoriteFragment};
        selectedIndex = -1;
        switchContent(0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "点击FloatingActionButton", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_avatar) {

        } else if (id == R.id.nav_bg) {

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(MessageEvent event){
        CharacterInfo Item = event.getCharacterInfo();
        int action = event.getAction();
        if (action == 1) {
            boolean flag = true;
            for (CharacterInfo character : character_favo) {
                if (character.getName().equals(Item.getName())) {
                    Toast.makeText(this, Item.getName() + "已在收藏夹中,不能重复添加!", Toast.LENGTH_SHORT).show();
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Item.setIsLike(true);
                character_favo.add(Item);
                Toast.makeText(this, Item.getName() + " 已添加到收藏夹", Toast.LENGTH_SHORT).show();
                for (CharacterInfo character : character_list) {
                    if (character.getName().equals(Item.getName())) {
                        character.setIsLike(true);
                        break;
                    }
                }
            }
        }
        else {
            for (CharacterInfo character : character_favo) {
                if (character.getName().equals(Item.getName())) {
                    for (CharacterInfo character_delete : character_list) {
                        if (character_delete.getName().equals(character.getName())) {
                            character_delete.setIsLike(false);
                            break;
                        }
                    }
                    Toast.makeText(this, "已从收藏夹移除 "+Item.getName()+"!", Toast.LENGTH_SHORT).show();
                    character_favo.remove(character);
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
