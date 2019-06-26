package com.example.myapplication;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LogServece {
    @GET("login.php")
    Observable<RetSuccess> log(@Query("name") String name, @Query("pwd") String pwd);

    @GET("register.php")
    Observable<RetSuccess>reg(@Query("name") String name,@Query("pwd") String pwd);

    @GET("find_name.php")
    Observable<RetSuccess>find_name(@Query("name") String name);
}
