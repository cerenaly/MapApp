package com.example.mapapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OverpassAPI {
    @GET("api/interpreter")
    Call<OverpassResponse> getSupermarkets(
            @Query("data") String query
    );
}
