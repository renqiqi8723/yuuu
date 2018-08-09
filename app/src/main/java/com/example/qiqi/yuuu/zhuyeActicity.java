package com.example.qiqi.yuuu;

//package com.xys.libzxing.zxing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import zxing.activity.CaptureActivity;
import zxing.activity.ResultActivity;

//import com.acker.simplezxing.activity.CaptureActivity;

//import com.xys.libzxing.zxing.activity.CaptureActivity;

/**
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 * <p>
 * Created by qiqi on 2018/7/9.
 */


public class zhuyeActicity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuyemian);

        ImageButton imbt_add = (ImageButton) findViewById(R.id.imbt_add);
        imbt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(zhuyeActicity.this, "jjjj", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(zhuyeActicity.this, Main4Activity.class);
//                intent.putExtra("aaa","123");
//                startActivityForResult(intent,CodeRequestResult.REQUEST_SCAN_BIND);
                do_saomiao();
            }
        });
    }

    private int index = 0;

    public  int do_saomiao()
    {
        Intent intent = new Intent(zhuyeActicity.this, CaptureActivity.class);
        //startActivityForResult(intent, CodeRequestResult.REQUEST_SCAN_BIND);

        Bundle bundle = new Bundle();

        bundle.putString("result", "uughufh");
        intent.putExtras(bundle);

        getParent().startActivityForResult( intent, CodeRequestResult.REQUEST_SCAN_BIND);

        //startActivity(new Intent(CaptureActivity.this, ResultActivity.class).putExtras(bundle));

       // startActivityForResult( new Intent(this, ResultActivity.class).putExtras(bundle), CodeRequestResult.REQUEST_SCAN_BIND);



        return 0;
    }

    public void onTabActivityResult(int requestCode, int resultCode, Intent data)
    {
        onActivityResult(requestCode, resultCode, data);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //System.out.println("sdfsfjjjjjjjjjjjjjj");
        Toast.makeText(zhuyeActicity.this,"123fhtytyryh",Toast.LENGTH_SHORT).show();
        Bundle b = data.getExtras();

        String str = b.getString("result");
        Toast.makeText(zhuyeActicity.this, str,Toast.LENGTH_SHORT).show();
        System.out.println(str);


    }

    @Override
    protected void onResume() {
        super.onResume();
        index ++;
        TextView tv = (TextView) findViewById(R.id.tv_no);
        String str = Integer.toString(index);
        tv.setText(str);

    }

}
