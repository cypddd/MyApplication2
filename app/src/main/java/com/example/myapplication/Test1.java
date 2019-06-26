package com.example.myapplication;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.safframework.log.L;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class Test1 extends AppCompatActivity implements View.OnClickListener {

    String TAG="testtest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        doRequestByRetrofit();

    }

    private void doRequestByRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cyp.natapp1.cc/")//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
                .build();
        Emp emp=retrofit.create(Emp.class);
        emp.getinfo().observeOn(AndroidSchedulers.mainThread())			//指定observer的回调方法运行在主线程	//要导入rxandroid包
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Emplinfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        L.d("onsubscribe");
                    }

                    @Override
                    public void onNext(Emplinfo emplinfo) {
                        L.json(emplinfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.d("onerror"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        L.d("oncomplete");
                    }
                });
        L.d("11111111");
    }

    @Override
    public void onClick(View view) {
        L.json(view);
    }


    public interface Emp {
        @GET("i.php")
        Observable<Emplinfo> getinfo();
    }

    public class Employee{
        private String FirstName;
        private String LastName;

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String firstName) {
            FirstName = firstName;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String lastName) {
            LastName = lastName;
        }
    }

    public class Emplinfo{

        private List<Employee> Employees;

        public List<Employee> getEmployees() {
            return Employees;
        }

        public void setEmployees(List<Employee> employees) {
            Employees = employees;
        }
    }
}

