package com.example.rana.mytrippmate.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Rana on 12/15/2018.
 */

public interface WeatherService {

    @GET
    Call<WeatherResponse> getWeatherResponse(@Url String endUrl);
}
