package com.example.cedar.githubbox.model;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Cedar on 2017/12/21.
 */

public class Repos {
    private String name, language, description;

    public String getname(){   return name;   }
    public String getlanguage(){ return language; }
    public String getDescription(){ return description; }
}
















