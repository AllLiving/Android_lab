package com.example.cedar.githubbox.service;

import com.example.cedar.githubbox.model.Github;
import com.example.cedar.githubbox.model.Repos;

import java.util.List;
import rx.Observable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Cedar on 2017/12/21.
 */

public interface GithubService {
    @GET("/users/{user}")
    Observable<Github> getUser(@Path("user") String user);
    @GET("/users/{user}/repos")
    Observable<List<Repos>> getRepos(@Path("user") String user);
}


























