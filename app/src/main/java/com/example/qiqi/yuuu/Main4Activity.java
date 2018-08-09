package com.example.qiqi.yuuu;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

public class Main4Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Intent intent = new Intent();
        intent.putExtra("haha", "fsdffsd");
        setResult(1, intent);
        finish();
    }

}
