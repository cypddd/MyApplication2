package com.example.myapplication.versionutil;

import com.example.myapplication.loginutil.RetSuccess;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface VersionService {
    @GET("update/checkforupdate.php")
    Observable<VersionRet> checkforupdate();
}
