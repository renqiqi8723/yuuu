package com.example.qiqi.yuuu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {




    private static String token = "";


    private static void setToken(String param_token)
    {
        token = param_token;
    }

    public static String getToken() {
        return token;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this,"123",Toast.LENGTH_SHORT).show();
    }

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Tools.init(this);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.actv_user_edit);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.ed_password_edit);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();

                    return true;

                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.bt_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              hideSoftKeyboard(LoginActivity.this);
                              attemptLogin();
                          }
         });


        mLoginFormView = findViewById(R.id.login_form);
       mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private  void start_Main_activity()
    {
        FullscreenActivity.set_login_state(true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //登录失败对话框
    private void show_dialog_login_fail()
    {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("登录");
            builder.setMessage("登录失败");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }
            );
            builder.create().show();


//            new AlertDialog.Builder(this)
//                    .setTitle("登录验证")
//                    .setMessage("登录失败！")
//                    .setPositiveButton("是", null)
//                    .show();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            // TODO: alert the user with a Snackbar/AlertDialog giving them the permission rationale
            // To use the Snackbar from the design support library, ensure that the activity extends
            // AppCompatActivity and uses the Theme.AppCompat theme.
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if(TextUtils.isEmpty(password))
        {
            mPasswordView.setError("密码不能为空");
            focusView = mPasswordView;
            cancel = true;
        }

        //密码的验证只在注册时进行，登录时之验证是否为空。
//        else if ( !isPasswordValid(password)) {
//            mPasswordView.setError("密码过于简单了");
//            focusView = mPasswordView;
//            cancel = true;
//        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
//        else if (!isUserValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isUserValid(String user) {
        return user.length() >= 8 ;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");


               // AlarmManager mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
               // mAlarmManager.setTimeZone("GMT+08:00");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mHideHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }



    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }



        private int check_login(String str_user, String str_password)  //返回0代表成功
        {
            //GsonBuildConfig.VERSION

            if(str_user.contentEquals("user") && str_password.contentEquals("pass"))
                return 0;
            else
                return -1;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.

                Thread.sleep(20);
                if(http_check_hello_login(mEmail) == 0)
                {
                    if(0 ==  http_check_login(mEmail, mPassword))
                    {
                        //登陆成功
                        //Intent intent = new Intent();
                        return true;
                    }
                    else
                    {  //登陆失败

                        return false;
                    }
                }
                else
                {

                    return false;
                }


            } catch (InterruptedException e) {
                return false;
            }



            // TODO: register the new account here.
            //return false;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }



        protected  int http_check_hello_login(String user)
        {

            int num = (int) ((Math.random() * 9 + 1) * 100000);
            String num_str = Integer.toString(num);
            System.out.println("jdk---:" + num_str);
            String jstr = "{'user':'1','pass':'2'}";


            String time = Tools.get_local_datetime();
            String date = time.substring(0, 8);
            System.out.println("jdk " + time);


           // String sign_old = Tools.getServerTime();
            String data_str =  "-" + user + "-" +  "-"  +  "-" + time + "-" + num_str
                    + "sign" + "eurhmw7yf074t0gn70t8w7m97shm0f9rehm98vh09g0s04seorigb";




            String data_sign = Tools.SHA1(data_str);


            LinkedHashMap<String, String> userinfo = new LinkedHashMap<String, String>();
            userinfo.put("reqtp", "hello");
            userinfo.put("useep", user);
            userinfo.put("tipme", time);
            userinfo.put("extaa", num_str);
            userinfo.put("signp", data_sign);


            Gson gson = new Gson();
            String jsonstr = gson.toJson(userinfo);
            System.out.println("jdk:" + jsonstr);

            Map<String, String> ssss= gson.fromJson(jsonstr, Map.class);
            String s1 = ssss.get("useep").toString();

            String str_url = "http://xl113.xinlingsafe.com/recvmsg.php";
            //String str_json = "";
            int r =  http_check_login(str_url, jsonstr, time, 0);  //返回0代表登录成功,否则失败
            // int r = 1;
            return r;
        }
        protected  int http_check_login(String user, String pass)
        {
            int num = (int) ((Math.random() * 9 + 1) * 100000);
            String num_str = Integer.toString(num);
            System.out.println("jdk---:" + num_str);
            String jstr = "{'user':'1','pass':'2'}";


            String time = Tools.get_local_datetime();
            String date = time.substring(0, 8);
            System.out.println("jdk " + time);

            //密码digest

            //Toast.makeText(LoginActivity.this, time, Toast.LENGTH_SHORT).show();

            // 为保证密码和摘要的一致性，去除时间和随机数，使得密码和摘要完全统一
            // 服务端不用做任何改变，因为服务端只保存了密码的摘要。
            String str_pass = pass + "-"  + "-"  + "-"
                    + "pass" + "775ug235432wrderti0pmhgu";            //Tools tool;
            String str_pass_signed = Tools.SHA1(str_pass);
            String sign_old = Tools.getServerTime();
            String data_str = sign_old + "-" + user + "-" + str_pass_signed + "--" + time + "-" + num_str
                    + "sign" + "eurhmw7yf074t0gn70t8w7m97shm0f9rehm98vh09g0s04seorigb";
            String data_sign = Tools.SHA1(data_str);


            LinkedHashMap<String, String> userinfo = new LinkedHashMap<String, String>();
            userinfo.put("reqtp", "log_in");
            userinfo.put("useep", user);
            userinfo.put("papde", str_pass_signed);
            userinfo.put("tipme", time);
            userinfo.put("extaa", num_str);
            userinfo.put("signp", data_sign);


            Gson gson = new Gson();
            String jsonstr = gson.toJson(userinfo);
            System.out.println("jdk:" + jsonstr);

            Map<String, String> ssss= gson.fromJson(jsonstr, Map.class);
            String s1 = ssss.get("useep").toString();

            String str_url = "http://xl113.xinlingsafe.com/recvmsg.php";
            //String str_json = "";
            int r =  http_check_login(str_url, jsonstr, time, 1);  //返回0代表登录成功,否则失败
            // int r = 1;
            return r;
        }

        protected String login_make_return_signdata(String time, String code, String msg, String token)
        {
            String signdata =  "sign_return" + time + "-"+ code +"-" + msg + "-" + token + "-"  + "fv5347656na10ldffadfc4frvts534d";
            return signdata;
        }


        protected  int http_check_login(String url, String json_str, String time, int flag)
        {
            Gson gson = new Gson();
            String json = Tools.http_post_json(url, json_str);
            if(json.isEmpty())
            {
                return 4;
            }

            Map<String, String> ssss2 = gson.fromJson(json, Map.class);
            String msg = ssss2.get("errormsg").toString();
            if(msg.equalsIgnoreCase("ok"))
            {
                String sign = ssss2.get("sign").toString();
                String code = ssss2.get("code").toString();
                String token = "";
                String server_time = "";
                if(flag == 1 )
                {
                    token = ssss2.get("token").toString();
                    String signdata = login_make_return_signdata(time, code, msg, token);
                    String signcode = Tools.SHA1(signdata);
                    if(signcode.equalsIgnoreCase(sign))
                    {

                        Tools.setServerTime(sign);
                        if(!token.isEmpty())
                        {
                            setToken(token);
                            return 0;
                        }
                        return 2;
                    }
                }
                else if (flag == 0 )  //hello
                {
                    server_time = ssss2.get("server_time").toString();
                    String signdata = login_make_return_signdata(time, code, msg, server_time);
                    String signcode = Tools.SHA1(signdata);
                    if(signcode.equalsIgnoreCase(sign))
                    {

                        if(!server_time.isEmpty())
                        {
                            Tools.setServerTime(server_time);
                            return 0;
                        }
                        return 2;
                    }
                }



                return 1;

            }
            else
            {
                return -1;
            }
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                start_Main_activity();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                show_dialog_login_fail();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

