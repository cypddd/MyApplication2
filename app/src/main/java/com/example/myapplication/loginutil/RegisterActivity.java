package com.example.myapplication.loginutil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.safframework.log.L;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name,pwd,pwd_re;
    Button reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        name=(EditText)findViewById(R.id.et_regname);
        pwd=(EditText)findViewById(R.id.et_regpwd);
        pwd_re=(EditText)findViewById(R.id.et_regpwd_re);
        reg=(Button)findViewById(R.id.bt_reg_in_RegActivity);
        reg.setOnClickListener(this);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    L.d(String.valueOf(b));                                                          //name的合法性,有无重复
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
