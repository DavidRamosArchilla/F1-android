package com.example.f1;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

@SuppressWarnings("Unused")
public interface IF1ApiService {

    @GET("/api/f1/current/last/results.json")
    Call<JsonObject> getLastRace(); //(@Path("id") long id)
}
