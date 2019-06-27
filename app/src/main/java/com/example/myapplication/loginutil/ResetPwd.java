package com.example.myapplication.loginutil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.RetSuccess;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetPwd extends AppCompatActivity implements View.OnClickListener {


    EditText name,pwd,pwd_re;
    Button reg;
    ProgressBar pb_downstair;

    Retrofit retrofit;
    LogServece logServece;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
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
        name=(EditText)findViewById(R.id.et_forname);
        pwd=(EditText)findViewById(R.id.et_forpwd);
        pwd_re=(EditText)findViewById(R.id.et_forpwd_re);
        reg=(Button)findViewById(R.id.bt_for_in_ForgetActivity);
        reg.setOnClickListener(this);

        pb_downstair=(ProgressBar)findViewById(R.id.pb_for);
        pb_downstair.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        if(pwd.getText().toString().equals(pwd_re.getText().toString())){
            pb_downstair.setVisibility(View.VISIBLE);
            logServece.change(name.getText().toString(),pwd.getText().toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<RetSuccess>(){
                        @Override
                        public void onSubscribe(Disposable d) {}
                        @Override
                        public void onNext(RetSuccess retSuccess) {
                            if(retSuccess.getSuccess().equals("1")){
                                Toast.makeText(ResetPwd.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(ResetPwd.this, LogActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                Toast.makeText(ResetPwd.this,"不存在用户名或新旧密码相同",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            pb_downstair.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onComplete() {
                            pb_downstair.setVisibility(View.INVISIBLE);

                        }
                    });

        }
        else{
            Toast.makeText(this,"两次输入的密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
        }
    }
}
