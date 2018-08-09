package com.example.qiqi.yuuu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import zxing.activity.CaptureActivity;

/**
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 * <p>
 * Created by qiqi on 2018/7/9.
 */
public class ShipinActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipin);

        Button bt_button2 = (Button)findViewById(R.id.button2);
        bt_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShipinActivity.this, "dfsygnc", Toast.LENGTH_SHORT).show();
                startq();

            }
        });

    }

    public void  startq()
    {
        Intent intent = new Intent(ShipinActivity.this, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("abc", "aaaaaaaaaaa");
        intent.putExtra("bundle", bundle);


        startActivityForResult(intent, RESULT_OK);

    }

    public void onTabActivityResult(int requestCode, int resultCode, Intent data)
    {
        onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "iiiikkk", Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
