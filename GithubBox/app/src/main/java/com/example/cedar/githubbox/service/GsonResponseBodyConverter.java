package com.example.cedar.githubbox.service;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Cedar on 2017/12/24.
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Gson gson = null;
    private Type type = null;

    public GsonResponseBodyConverter(Gson gson, Type type){
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getString("status").equals("true")){
                String data = jsonObject.getString("data");
                return gson.fromJson(data, type);
            }else{
                String err = "convert Wrong.";
                return gson.fromJson(err, type);
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}