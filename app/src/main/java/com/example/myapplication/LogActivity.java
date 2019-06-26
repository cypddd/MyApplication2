package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.loginutil.RegisterActivity;
import com.example.myapplication.loginutil.ResetPwd;
import com.safframework.log.L;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class LogActivity extends AppCompatActivity implements View.OnClickListener {                 //   有空做个微信登陆//


    String TAG="LogActivity";
    Button bt_login,bt_reg;
    TextView tv_forget;
    EditText et_name,et_pwd;
    CheckBox cb_rember;
    boolean rember=false;
    Intent i;

    Retrofit retrofit;
    LogServece logServece;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        initView();
        if((Boolean) getSharedPreferences("log_info", Context.MODE_PRIVATE).getBoolean("rember",false)){        //自动登陆
            cb_rember.setChecked(true);
            checkforlog();
        }
    }

    private void initView() {
        setTitle("登陆");

        cb_rember=(CheckBox)findViewById(R.id.chkbox_autolog);
        cb_rember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                rember=b;
            }
        });
        bt_login=(Button)findViewById(R.id.but_log);
        bt_login.setOnClickListener(this);
        bt_reg=(Button)findViewById(R.id.but_reg);
        bt_reg.setOnClickListener(this);
        tv_forget=(TextView)findViewById(R.id.tv_forget_pwd);
        tv_forget.setOnClickListener(this);

        et_name=(EditText)findViewById(R.id.et_name);
        et_name.setText(getSavedInfo("id"));
        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    L.d(String.valueOf(b));                                                          //name的合法性
                }
            }
        });
        et_pwd=(EditText)findViewById(R.id.et_pwd);
        et_pwd.setText(getSavedInfo("pwd"));
        et_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    L.d(String.valueOf(b));                                                         //pwd的合法性
                }
            }
        });

        MyApplication myApplication=(MyApplication)getApplication();

        retrofit = new Retrofit.Builder()
                .baseUrl(myApplication.getBaseurl())//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
                .build();
        logServece=retrofit.create((LogServece.class));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_log:
                saveIdAndPwd(et_name.getText().toString(),et_pwd.getText().toString());
                checkforlog();
                break;
            case R.id.but_reg:
                i=new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.tv_forget_pwd:
                i=new Intent(this, ResetPwd.class);
                startActivity(i);
                break;

        }
    }

    private void checkforlog() {                                                                    //远程核验身份，通过则跳转MainActivity          //z做个进度圈

        logServece.log(et_name.getText().toString(),et_pwd.getText().toString()).observeOn(AndroidSchedulers.mainThread())			//指定observer的回调方法运行在主线程
                .subscribeOn(Schedulers.io())						    //运行在io线程
                .subscribe(new Observer<RetSuccess>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RetSuccess retSuccess) {
                        if(retSuccess.success.equals("1")){
                            i=new Intent(LogActivity.this, ResetPwd.class);
                            startActivity(i);
                            finish();
                        }
                        else Toast.makeText(LogActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }

    public void saveIdAndPwd(String id,String pwd){
        SharedPreferences.Editor editor=getSharedPreferences("log_info", Context.MODE_PRIVATE).edit();       //保存登陆信息
        editor.putString("id", id);
        editor.putString("pwd", pwd);
        editor.putBoolean("rember",rember);
        editor.commit();//将数据持久化到存储介质中去
    }
    public String getSavedInfo(String what){
        SharedPreferences preferences= getSharedPreferences("log_info", Context.MODE_PRIVATE);
        if(what.equals("id")){
            return preferences.getString("id", "");
        }
        else if (what.equals("pwd")){
            return preferences.getString("pwd", "");
        }
        return "???";
    }


    long firstTime=0;                                                                               //按两次退出
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
