package com.example.qiqi.yuuu;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 * <p>
 * Created by qiqi on 2018/7/17.
 */
public class Tools {

    private static String serverTime = "";

    //字符串转16进制string
    public static String str2HexStr(String str)
    {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 十六进制转换字符串
     * @param String str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr)
    {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++)
        {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * bytes转换成十六进制字符串
     * @param byte[] b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b)
    {
        String stmp="";
        StringBuilder sb = new StringBuilder("");
        for (int n=0;n<b.length;n++)
        {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length()==1)? "0"+stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    /**
     * bytes字符串转换为Byte值
     * @param String src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src)
    {
        int m=0,n=0;
        int l=src.length()/2;
        System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++)
        {
            m=i*2+1;
            n=m+1;
            ret[i] = Byte.decode("0x" + src.substring(i*2, m) + src.substring(m,n));
        }
        return ret;
    }

    /**
     * String的字符串转换成unicode的String
     * @param String strText 全角字符串
     * @return String 每个unicode之间无分隔符
     * @throws Exception
     */
    public static String strToUnicode(String strText)
            throws Exception
    {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++)
        {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128)
                str.append("\\u" + strHex);
            else // 低位在前面补00
                str.append("\\u00" + strHex);
        }
        return str.toString();
    }

    /**
     * unicode的String转换成String的字符串
     * @param String hex 16进制值字符串 （一个unicode为2byte）
     * @return String 全角字符串
     */
    public static String unicodeToString(String hex)
    {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++)
        {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // 高位需要补上00再转
            String s1 = s.substring(2, 4) + "00";
            // 低位直接转
            String s2 = s.substring(4);
            // 将16进制的string转为int
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
            // 将int转换为字符
            char[] chars = Character.toChars(n);
            str.append(new String(chars));
        }
        return str.toString();
    }

    public static String http_post_json(String url, String json_str )
    {
        //打开浏览器

        OkHttpClient oKhttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();


        final Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json;charset:utf-8")
                //.post(body)
                .put(RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        json_str))// post json提交


                .build();
        //Response response = oKhttpClient.newCall(request).execute();
        //return response.body().string();

        try {
            Response response = oKhttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                //System.out.println("hhujiijjijjijisjidfji");
                System.out.println(json);
                //System.out.println("hhujiijjijjijisjidfji");
                try {
                    return json;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }


            }
            else
            {
                System.out.println("http request fail");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";

    };

    public  static   String tool_make_return_sign_data(String time, String code, String msg, String extra )
    {

        String sign_data = "sign_return"  + time + "-" + code + "-" + msg  + "-"  + extra + "-"+ "rwm74n9x9jrYc2djhLPPfidzczoxc";
        return sign_data;
    }


//    public static boolean check_return_sign(String time, String code, String msg, String token, String sign)
//    {
//        String data = "sign_return" + time + "-"+ code +"-" + msg + "-" + token + "-"  + "fv5347656na10ldffadfc4frvts534d";
//        try {
//            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
//            messageDigest.update(data.getBytes());
//            byte[] shaBytes = messageDigest.digest();
//            String str_sign = Tools.byte2HexStr(shaBytes);
//            str_sign = str_sign.replaceAll(" ", "");
//            str_sign = str_sign.toLowerCase();
//            if(sign.equalsIgnoreCase(str_sign))
//            {
//                return true;
//            }
//
//            //System.out.println("jdk SHA 1: " + Hex.encodeHexString(shaBytes));
//            System.out.println("ret jdk SHA 1: " + Tools.byte2HexStr(shaBytes));
//            // Tools
//            //return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return false;
//    }





    public static String getServerTime() {
        return serverTime;
    }

    public static void setServerTime(String serverTime) {
        Tools.serverTime = serverTime;
    }

    public static String get_local_datetime( )
    {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8"); //设置时区为东8区
        Calendar calendar=Calendar.getInstance();  //获取当前时间，作为图标的名字
        calendar.setTimeZone(timeZone);
        String year=calendar.get(Calendar.YEAR)+"";
        String month=calendar.get(Calendar.MONTH)+1+"";
        if(month.toString().length() == 1)
        {
            month = "0" + month;
        }

        String day=calendar.get(Calendar.DAY_OF_MONTH)+"";
        if(day.toString().length() == 1)
        {
            day = "0" + day;
        }

        String hour=calendar.get(Calendar.HOUR_OF_DAY)+"";
        if(hour.toString().length() == 1)
        {
            hour = "0" + hour;
        }
        String minute=calendar.get(Calendar.MINUTE)+"";
        if(minute.toString().length() == 1)
        {
            minute = "0" + minute;
        }
        String second=calendar.get(Calendar.SECOND)+"";
        if(second.toString().length() == 1)
        {
            second = "0" + second;
        }


        String time = year+month+day+hour+minute+second;
        //String date = year + month + day;
        return time;
    }

    public static String SHA1(String data)
    {
        String data_str = data;
        String data_sign = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(data_str.getBytes());
            byte[] shaBytes = messageDigest.digest();
            data_sign = Tools.byte2HexStr(shaBytes);
            data_sign = data_sign.replaceAll(" ", "");
            data_sign = data_sign.toLowerCase();

        } catch (Exception e) {
            e.printStackTrace();
            data_sign = "";
        }
        return data_sign;
    }
}
