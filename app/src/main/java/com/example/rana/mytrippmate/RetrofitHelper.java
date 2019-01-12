package com.example.rana.mytrippmate;

import com.example.rana.mytrippmate.weather.WeatherService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rana on 12/15/2018.
 */

public class RetrofitHelper {
    private static final String WEATHER_BASE_URL="https://samples.openweathermap.org/data/2.5/";
    private static Retrofit retrofit;
    private static RetrofitHelper retrofitHelper;

    private RetrofitHelper()
    {
        retrofit=new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static WeatherService getWeatherService()
    {
        if(retrofitHelper==null)
        {
            retrofitHelper=new RetrofitHelper();
        }
        return retrofit.create(WeatherService.class);
    }

    public static String getIconLinkFromName(String name)
    {
        return String.format("https://openweathermap.org/img/w/%s.png",name);
    }
}
