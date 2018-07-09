package com.example.qiqi.yuuu;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends ActivityGroup {

    private TabHost mTb_host;
    //选项id
    private String[] tags = {"A_tag", "B_tag", "C_tag", "D_tag" };
    // 选项卡文本信息
    private String[] titiles = {"主页", "列表", "视频", "我的"};
    //选项卡图标
    private int[] images = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
    R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background};
    //用于跳转的activity的Intent数组
    private Intent[] Intents = new Intent[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        mTb_host = (TabHost) findViewById(R.id.tab_host);
        mTb_host.setBackgroundColor(Color.WHITE);

        LocalActivityManager manager = new LocalActivityManager(MainActivity.this, false);
        manager.dispatchCreate(savedInstanceState);
        mTb_host.setup(manager);
        init_intent();
        for (int i = 0; i < Intents.length; i++)
        {
            LayoutInflater inflater = this.getLayoutInflater();
            View view = inflater.inflate(R.layout.tab, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_item);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            textView.setText(titiles[i]);
            imageView.setImageResource(images[i]);
            //创建选项卡
            TabHost.TabSpec spec = mTb_host.newTabSpec(tags[i]);
            spec.setIndicator(view);
            //设置每个页面的内容
            spec.setContent(Intents[i]);

            //将创建的选项卡添加至tabHost上
            mTb_host.addTab(spec);
        }

//        mTb_host.addTab(mTb_host.newTabSpec("table1").setIndicator("bianqian1", getResources().getDrawable(android.R.drawable.ic_lock_idle_alarm)).setContent(R.id.tab1));
//        mTb_host.addTab(mTb_host.newTabSpec("tab2").setIndicator("标签2",null).setContent(R.id.tab2));
//        Intent intents = new Intent(this, zhuyeActicity.class);
//        mTb_host.addTab(mTb_host.newTabSpec("tab3").setIndicator("标签3",null).setContent(intents));
        mTb_host.setCurrentTab(0);




    }

    public void init_intent() {
        Intents[0] = new Intent(this, zhuyeActicity.class);
        Intents[1] = new Intent(this, ListAcicvity.class);
        Intents[2] = new Intent(this, ShipinActivity.class);
        Intents[3] = new Intent(this, MyActivity.class);
    }
}
