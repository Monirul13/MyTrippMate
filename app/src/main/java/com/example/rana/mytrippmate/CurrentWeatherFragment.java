package com.example.rana.mytrippmate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rana.mytrippmate.weather.WeatherResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeatherFragment extends Fragment {

    private Context context;
    private TextView tempTV,dateTV,dayTV,cityTV,minTempTV,maxTempTV,sunRiseTV,sunSetTV,humidityTV,
                     pressureTV,descriptionTV;
    private ImageView weatherImageIcon;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    //    private double latitude;
//    private double longitude;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_current_weather, container, false);
        tempTV=view.findViewById(R.id.temp_TV_id);
        dateTV=view.findViewById(R.id.date_TV_id);
        dayTV=view.findViewById(R.id.day_TV_id);
        cityTV=view.findViewById(R.id.city_TV_id);
        minTempTV=view.findViewById(R.id.minTemp_TV_id);
        maxTempTV=view.findViewById(R.id.maxTemp_TV_id);
        sunRiseTV=view.findViewById(R.id.sunrise_TV_d);
        sunSetTV=view.findViewById(R.id.sunset_TV_id);
        humidityTV=view.findViewById(R.id.humidity_TV_id);
        pressureTV=view.findViewById(R.id.pressure_TV_id);
        descriptionTV=view.findViewById(R.id.weather_desc_TV_id);
        weatherImageIcon=view.findViewById(R.id.weatherImageview_id);

        return  view;
    }

    public void setLatLng(double latitude,double longitude)
    {
        //weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22
        String apiKey = context.getString(R.string.api_key);
        String endUrl=String.format("weather?lat=%f&lon=%f&appid=%s",latitude,longitude,apiKey);
//        this.latitude=latitude;
//        this.longitude=longitude;
        RetrofitHelper.getWeatherService().getWeatherResponse(endUrl)
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        if(response.isSuccessful())
                        {
                            WeatherResponse weatherResponse=response.body();
                            double temp=weatherResponse.getMain().getTemp();
                            double minTemp=weatherResponse.getMain().getTempMin();
                            double maxTemp=weatherResponse.getMain().getTempMax();
                            Integer humidity=weatherResponse.getMain().getHumidity();
                            double pressure=weatherResponse.getMain().getPressure();

                            String city=weatherResponse.getName();

                            Integer sunrise=weatherResponse.getSys().getSunrise();
                            Integer sunset=weatherResponse.getSys().getSunset();

                            String icon=weatherResponse.getWeather().get(0).getIcon();
                            String description=weatherResponse.getWeather().get(0).getDescription();

                            tempTV.setText(String.valueOf(temp));
                            minTempTV.setText(String.valueOf(minTemp));
                            maxTempTV.setText(String.valueOf(maxTemp));
                            humidityTV.setText(String.valueOf(humidity));
                            pressureTV.setText(String.valueOf(pressure));

                            cityTV.setText(city);
                            sunRiseTV.setText(String.valueOf(sunrise));
                            sunSetTV.setText(String.valueOf(sunset));
                            descriptionTV.setText(description);

                            Picasso.get().load(RetrofitHelper.getIconLinkFromName(icon)).into(weatherImageIcon);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {

                    }
                });
    }
}
