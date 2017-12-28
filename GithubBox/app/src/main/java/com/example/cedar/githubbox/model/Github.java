package com.example.cedar.githubbox.model;

import retrofit2.http.GET;

/**
 * Created by Cedar on 2017/12/21.
 */

public class Github {
    private String login = null;
    private String blog = null;
    private int id;

    public String getLogin(){ return login; }
    public String getBlog(){ return blog; }
    public int getId(){ return id; }
}
