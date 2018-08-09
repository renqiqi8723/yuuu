package com.example.qiqi.yuuu;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends Activity {

    private TabHost mTb_host;
    //选项id
    private String[] tags = {"A_tag", "B_tag", "C_tag", "D_tag" };
    // 选项卡文本信息
    private String[] titiles = {"主页", "列表", "视频", "我的"};
    //选项卡图标
    private int[] images = {R.mipmap.main_h, R.mipmap.list_h,
    R.mipmap.msg_h_5, R.mipmap.my_h};
    private int[] images_p = {R.mipmap.main_h_5p, R.mipmap.list_h_p,
            R.mipmap.msg_h_5p, R.mipmap.my_h_5p};

    private  int[] id_tabs = {R.drawable.selector_icon_main, R.drawable.selector_icon_list,
    R.drawable.selector_icon_shipin, R.drawable.selector_icon_my};
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
            //创建imagView加载图片
            LayoutInflater inflater = this.getLayoutInflater();
            View view = inflater.inflate(R.layout.tab, null);

            TextView textView = (TextView) view.findViewById(R.id.tv_item);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);


            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)imageView.getLayoutParams();
            layoutParams.leftMargin = 80;
            layoutParams.gravity = Gravity.CENTER;

            imageView.setLayoutParams(layoutParams);
            //textView.setText(titiles[i]);
            //imageView.setImageResource(images[i]);

            imageView.setImageResource(id_tabs[i]);

            //imageView.setGravity(Gravity.CENTER);
            //imageView.setImageResource();
            //创建选项卡
            TabHost.TabSpec spec = mTb_host.newTabSpec(tags[i]);
            spec.setIndicator(view);
            //设置每个页面的内容
            spec.setContent(Intents[i]);



            //将创建的选项卡添加至tabHost上
            mTb_host.addTab(spec);

           /// mTb_host.
        }
        mTb_host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {

            }
        });

//        mTb_host.addTab(mTb_host.newTabSpec("table1").setIndicator("bianqian1", getResources().getDrawable(android.R.drawable.ic_lock_idle_alarm)).setContent(R.id.tab1));
//        mTb_host.addTab(mTb_host.newTabSpec("tab2").setIndicator("标签2",null).setContent(R.id.tab2));
//        Intent intents = new Intent(this, zhuyeActicity.class);
//        mTb_host.addTab(mTb_host.newTabSpec("tab3").setIndicator("标签3",null).setContent(intents));
        mTb_host.setCurrentTab(0);




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public interface OnTabActivityResultListener{
        public void onTabActivityResult(int requestCode, int resultCode, Intent data);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this,"123",Toast.LENGTH_SHORT).show();

        if(requestCode == CodeRequestResult.REQUEST_SCAN_BIND)
        {
            switch (resultCode)
            {
                case (CodeRequestResult.RETURN_ZHUYE_CODE):
                    //zhuyeActicity.onTabActivityResult( requestCode,  resultCode,  data);
                    //OnTabActivityResultListener listener = (OnTabActivityResultListener) subActivity;
                    //mTb_host.getCurrentView()
                    //zhuyeActicity.onTabActivityResult( requestCode,  resultCode,  data);
                    int index =  mTb_host.getCurrentTab();
                    if(0 == index)
                    {
                        try {
                            //mTb_host.getCurrentActivity();
                            View v = mTb_host.getCurrentView();
                            if(v.isEnabled())
                            {
                                //Context ctx = v.getContext();
                                Activity aaaa = getActivity(v);

                                //Activity ma = (Activity)(v.getContext());
                                //Activity hasd = mTb_host.getCurrentView().getContext();
                                //Toast.makeText(ma, "dsfds", Toast.LENGTH_SHORT).show();
                                //((zhuyeActicity)ma).onTabActivityResult(requestCode,  resultCode,  data);
                                ((zhuyeActicity)aaaa).onTabActivityResult( requestCode,  resultCode,  data);
                            }
                            else
                            {

                            }

                        } catch (Exception e) {
                            System.out.println("hjuhjhjhjhjhj1");
                            e.printStackTrace();

                            System.out.println("hjuhjhjhjhjhj2");

                        }


                    }
                    break;
                case CodeRequestResult.RETURN_LIST_CODE:break;
                case CodeRequestResult.RETURN_SHIPIN_CODE:break;
                case CodeRequestResult.RETURN_MY_CODE:break;
                default:
            }
        }
    }

    public static Activity getActivity(View view) {
        Activity activity = null;
        if (view.getContext().getClass().getName().contains("com.android.internal.policy.DecorContext")) {
            try {
                Field field = view.getContext().getClass().getDeclaredField("mPhoneWindow");
                field.setAccessible(true);
                Object obj = field.get(view.getContext());
                java.lang.reflect.Method m1 = obj.getClass().getMethod("getContext");
                activity = (Activity) (m1.invoke(obj));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            activity = (Activity) view.getContext();
        }
        return activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void init_intent() {
        Intents[0] = new Intent(this, zhuyeActicity.class);
        Intents[1] = new Intent(this, ListAcicvity.class);
        Intents[2] = new Intent(this, ShipinActivity.class);
        Intents[3] = new Intent(this, MyActivity.class);
    }
}
