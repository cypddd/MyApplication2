package com.example.myapplication.loginutil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.safframework.log.L;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name,pwd,pwd_re;
    Button reg;
    TextView hint;
    ProgressBar pb,pb_downstair;

    Retrofit retrofit;
    LogServece logServece;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init_retrofit();
        init();
    }

    private void init_retrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(new MyApplication().getBaseurl())//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
                .build();
        logServece=retrofit.create((LogServece.class));
    }

    private void init() {
        name=(EditText)findViewById(R.id.et_regname);
        pwd=(EditText)findViewById(R.id.et_regpwd);
        pwd_re=(EditText)findViewById(R.id.et_regpwd_re);
        reg=(Button)findViewById(R.id.bt_reg_in_RegActivity);
        reg.setOnClickListener(this);
        pb=(ProgressBar)findViewById(R.id.pb_reg);
        pb.setVisibility(View.INVISIBLE);
        pb_downstair=(ProgressBar)findViewById(R.id.pb_reg_downstair);
        pb_downstair.setVisibility(View.INVISIBLE);
        hint=(TextView)findViewById(R.id.tv_name_hint);
        hint.setVisibility(View.INVISIBLE);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    hint.setVisibility(View.VISIBLE);
                    hint.setText("检查用户名是否可用中..");
                    pb.setVisibility(View.VISIBLE);
                    logServece.find_name(name.getText().toString())
                            .observeOn(AndroidSchedulers.mainThread())			//指定observer的回调方法运行在主线程
                            .subscribeOn(Schedulers.io())						    //运行在io线程
                            .subscribe(new Observer<RetSuccess>(){
                                @Override
                                public void onSubscribe(Disposable d) {}

                                @Override
                                public void onNext(RetSuccess retSuccess) {
                                    if(retSuccess.getSuccess().equals("0")){//可用
                                        hint.setText("当前用户名可用");
                                    }
                                    else if(retSuccess.getSuccess().equals("1")){//可用
                                        hint.setText("当前用户名已被占用");
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    pb.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onComplete() {
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            });                                                     //name的合法性,有无重复

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(pwd.getText().toString().equals(pwd_re.getText().toString())){
            if(hint.getText().toString().equals("当前用户名可用")){
                pb_downstair.setVisibility(View.VISIBLE);
                logServece.reg(name.getText().toString(),pwd.getText().toString())
                        .observeOn(AndroidSchedulers.mainThread())			//指定observer的回调方法运行在主线程
                        .subscribeOn(Schedulers.io())						    //运行在io线程
                        .subscribe(new Observer<RetSuccess>(){
                            @Override
                            public void onSubscribe(Disposable d) {}

                            @Override
                            public void onNext(RetSuccess retSuccess) {

                            }

                            @Override
                            public void onError(Throwable e) {
                                pb_downstair.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onComplete() {
                                pb_downstair.setVisibility(View.INVISIBLE);
                                Intent i=new Intent(RegisterActivity.this, LogActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });                                                     //name的合法性,有无重复

                L.d("注册成功");

            }
            else{
                L.d("注册失败");
            }
        }
        else{
            Toast.makeText(this,"两次输入的密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
        }
    }
}
