package com.example.rana.mytrippmate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class WeatherActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private CurrentWeatherFragment currentWeatherFragment;
    private SevenDaysForecastFragment sevenDaysForecastFragment;

    private FusedLocationProviderClient providerClient;
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        providerClient = LocationServices.getFusedLocationProviderClient(this);
        currentWeatherFragment=new CurrentWeatherFragment();
        sevenDaysForecastFragment=new SevenDaysForecastFragment();
        viewPager=findViewById(R.id.viewPager_id);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        tabLayout=findViewById(R.id.tabLayout_id);
        tabLayout.addTab(tabLayout.newTab().setText("CURRENT"));
        tabLayout.addTab(tabLayout.newTab().setText("7 Days Forecast"));
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(checkLocationPermission()){
            getDeviceLastLocation();
        }
    }

    private boolean checkLocationPermission(){
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION_CODE);
            return false;
        }

        return true;
    }

    private void getDeviceLastLocation(){
        if(checkLocationPermission()){
            Task<Location> locationTask = providerClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            double latitude;
                            double longitude;
                            if (location == null) {

                                Toast.makeText(WeatherActivity.this, "Accurate location not found", Toast.LENGTH_SHORT).show();
//                                return;
                                latitude = 23.75;
                                longitude = 90.39;
                            } else {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                            currentWeatherFragment.setLatLng(latitude, longitude);
                            sevenDaysForecastFragment.setLatLng(latitude,longitude);

                        }
                    });
        }
    }




    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 0:
                    return currentWeatherFragment;
                case 1:
                    return sevenDaysForecastFragment;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}

