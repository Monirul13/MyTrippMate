package com.example.rana.mytrippmate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rana.mytrippmate.weatherforecast.RetrofitForecast;
import com.example.rana.mytrippmate.weatherforecast.WeatherForecastResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SevenDaysForecastFragment extends Fragment {

    private Context context;

    public SevenDaysForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_seven_days_forecast, container, false);
        return  view;
    }

    public void setLatLng(double lat,double lng)
    {
        String apiKey=context.getString(R.string.api_key);
        String endUrl=String.format("forecast/daily?lat=%f&lon=%f&cnt=10&appid=%s",lat,lng,apiKey);

        RetrofitForecast.getWeatherForecastService().getSevenDaysWeatherForecast(endUrl)
                .enqueue(new Callback<WeatherForecastResponse>() {
                    @Override
                    public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                        if(response.isSuccessful())
                        {
                           WeatherForecastResponse weatherForecastResponse=response.body();

                           //weatherForecastResponse.
                        }
                        //Toast.makeText(context,"Number of forecast"+response.)
                    }

                    @Override
                    public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {

                    }
                });


    }

}
