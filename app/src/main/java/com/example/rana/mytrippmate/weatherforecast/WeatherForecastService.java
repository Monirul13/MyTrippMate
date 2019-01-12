package com.example.rana.mytrippmate.weatherforecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Rana on 12/21/2018.
 */

public interface WeatherForecastService {
    @GET
    Call<WeatherForecastResponse> getSevenDaysWeatherForecast(@Url String endUrl);
}
