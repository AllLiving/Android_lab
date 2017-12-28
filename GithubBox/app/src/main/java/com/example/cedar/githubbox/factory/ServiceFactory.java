package com.example.cedar.githubbox.factory;

import com.example.cedar.githubbox.service.GithubService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Cedar on 2017/12/21.
 */

public class ServiceFactory {

    public final static int CONNECT_TIMEOUT = 10;
    public final static int READ_TIMEOUT = 30;
    public final static int WRITE_TIMEOUT = 10;
    private static OkHttpClient okHttpClient = null;
    private static Retrofit retrofit = null;
    private static GithubService github = null;

    private static OkHttpClient createOkHttp(){
        OkHttpClient okHttpClient
                = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    public static GithubService getApi(String baseUrl){
        github = getRetrofit(baseUrl).create(GithubService.class);
        return github;
    }

    private static Retrofit getRetrofit(String baseUrl){
        if(okHttpClient == null)
            okHttpClient = createOkHttp();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }
}
