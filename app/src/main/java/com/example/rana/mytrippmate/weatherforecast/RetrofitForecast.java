package com.example.rana.mytrippmate.weatherforecast;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rana on 12/21/2018.
 */

public class RetrofitForecast {

    public static final String FORECAST_BASE_URL="https://samples.openweathermap.org/data/2.5/";
    public static Retrofit retrofit;
    public static RetrofitForecast retrofitForecast;

    private RetrofitForecast()
    {
        retrofit=new Retrofit.Builder()
                .baseUrl(FORECAST_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static WeatherForecastService getWeatherForecastService()
    {
        if(retrofitForecast==null)
        {
            retrofitForecast=new RetrofitForecast();
        }
        return retrofit.create(WeatherForecastService.class);
    }

    public static String getForecastIconLinkFromName(String name)
    {
        return String.format("https://openweathermap.org/img/w/%s.png",name);
    }

}
