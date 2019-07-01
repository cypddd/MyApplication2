package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.safframework.log.L;

import java.lang.reflect.Method;

public class BaseActivity extends AppCompatActivity {
    private long mExitTime=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);                                      //添加左上角返回//
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setIconVisible(menu);
        menu.add(Menu.NONE, Menu.FIRST + 1, 5, "设置");
        return true;
    }
    //
    private void setIconVisible(Menu menu) {
        try {
            Class clazz = Class
                    .forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible",
                    boolean.class);
            m.setAccessible(true);
            m.invoke(menu, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                L.d("返回");
                break;

            case Menu.FIRST+1:
                Intent i=new Intent(this,SettingActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再点击返回一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
        return true;
    }
}
