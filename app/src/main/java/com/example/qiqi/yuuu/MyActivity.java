package com.example.qiqi.yuuu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 * <p>
 * Created by qiqi on 2018/7/9.
 */
public class MyActivity extends Activity {

    private Button mBt_logout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my);

        mBt_logout = (Button) findViewById(R.id.bt_logout);
        mBt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtoLogin();
            }
        });
    }

    private  int backtoLogin(){

        FullscreenActivity.set_login_state(false);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        return 0;
    }

    public void onTabActivityResult(int requestCode, int resultCode, Intent data)
    {
        onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
