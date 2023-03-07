package com.example.f1;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@SuppressWarnings("Unused")
public interface IF1ApiService {

    @GET("/api/f1/current/last/results.json")
    Call<JsonObject> getLastRace(); //(@Path("id") long id)

    @GET("/api/f1/{year}")
    Call<JsonObject> getRacesOfYear(@Path("year") String year);

    @GET("/api/f1/{year}/{round}/results")
    Call<JsonObject> getResultOfRace(@Path("year") String year, @Path("round") String round);
}
