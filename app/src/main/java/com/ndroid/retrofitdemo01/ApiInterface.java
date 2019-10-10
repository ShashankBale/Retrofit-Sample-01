package com.ndroid.retrofitdemo01;

import com.ndroid.retrofitdemo01.model.XyzUsers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("/users")
    Call<List<XyzUsers>> getUsers();
}
