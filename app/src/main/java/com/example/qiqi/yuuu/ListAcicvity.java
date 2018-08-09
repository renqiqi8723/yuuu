package com.example.qiqi.yuuu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 * <p>
 * Created by qiqi on 2018/7/9.
 */
public class ListAcicvity  extends Activity {

    //private   String token =

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get_Dev_List( );
        setContentView(R.layout.list);
    }

    public void onTabActivityResult(int requestCode, int resultCode, Intent data)
    {

        onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this,"123",Toast.LENGTH_SHORT).show();
    }



    private void get_Dev_List( )
    {
        int ret = http_getdev_List( );
        if(ret == 0)
        {
            System.out.println("get_dev_list ok ...");
        }
        else
        {
            System.out.println("get_dev_list fail ...");
        }

    }


    private int  http_getdev_List( )
    {
        String token = LoginActivity.getToken();
        String sign_old = Tools.getServerTime();
        String request = "get_dev_list";
        String time = Tools.get_local_datetime();
        String sign_str = Tools.SHA1(sign_old + token + request + time + "yhuyq3wefrtyuiasdfghjkl1234567hxc");
        LinkedHashMap<String, String> userinfo = new LinkedHashMap<String, String>();
        userinfo.put("reqtp", request);
        userinfo.put("tipme", time);
        userinfo.put("signp", sign_str);


        Gson gson = new Gson();

        String jsonstr = gson.toJson(userinfo);
        System.out.println("jdk:" + jsonstr);

        String str_url = "http://xl113.xinlingsafe.com/recvmsg.php";
        String json =  Tools.http_post_json(str_url, jsonstr);  //返回0代表登录成功,否则失败

        if(json.isEmpty())
        {
            return 4;
        }

        java.lang.reflect.Type type = new TypeToken<JsonBean>() {}.getType();
        JsonBean jsonBean = gson.fromJson(json, type);



//        Map<String, String> ssss2 = gson.fromJson(json, Map.class);
//        String re_msg = ssss2.get("errormsg").toString();

        String re_msg = jsonBean.errormsg;
        if(re_msg.equalsIgnoreCase("ok"))
        {
//            String re_sign = ssss2.get("sign").toString();
//            String re_code = ssss2.get("code").toString();
            String re_sign = jsonBean.sign;
            String re_code = jsonBean.code;
            List<DEV_MSG> msg = jsonBean.msg;
            //DEV_MSG msg = jsonBean.msg;
            int length = msg.size();
            for(int i=0; i<length; i++)
            {
                String name = msg.get(i).dev_name;
                String stat = msg.get(i).dev_stat;
                System.out.println(name + ":" +  stat);
            }


            String sign_data = Tools.tool_make_return_sign_data(time, re_code, re_msg, "");
            String sign_code = Tools.SHA1(sign_data);
                if(sign_code.equalsIgnoreCase(re_sign))
                {

                    Tools.setServerTime(re_sign);
                    return 0;
                }


            return 1;

        }
        else
        {
            return -1;
        }

       // return -1;
    }


    public class JsonBean
    {
        public String errormsg;
        public String code;
        public String sign;
        public List<DEV_MSG> msg;

    }
    public static class DEV_MSG{
        String dev_name;
        String dev_stat;

    }

}


