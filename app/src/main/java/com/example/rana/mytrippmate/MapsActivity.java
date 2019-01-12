package com.example.rana.mytrippmate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_CODE =1;
    private GoogleMap mMap;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Geocoder geocoder;

    private double latitude;
    private double longitude;
    private LatLng latLng;

    private List<Address> addressList=new ArrayList<>();
    private String city;
    private String addressLine;
    private String countryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder=new Geocoder(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private boolean checkLocationPermission(){
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_LOCATION_CODE)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getDeviceLastLocation();
            }
        }
    }

    private void getDeviceLastLocation() {
        if(checkLocationPermission())
        {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location==null)
                            {
                                return;
                            }
                            latitude=location.getLatitude();
                            longitude=location.getLongitude();
                            Toast.makeText(getApplicationContext(),"Latitude: "+latitude+"\n"+"Longitude : "+longitude,Toast.LENGTH_LONG).show();
                            Log.e("GLDL","Latitude: "+latitude+"\n"+"Longitude : "+longitude);

                            try {
                                addressList=geocoder.getFromLocation(latitude,longitude,1);
                                addressLine=addressList.get(0).getAddressLine(0);
                                city=addressList.get(0).getLocality();
                                countryName=addressList.get(0).getCountryName();
                                Toast.makeText(getApplicationContext(),"Address: "+addressLine+"\n"+city+"\n"+countryName,Toast.LENGTH_LONG).show();
                                Log.e("GLDL","Address: "+addressLine+"\n"+city+"\n"+countryName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }
    }

    protected void onResume() {
        super.onResume();
        if(checkLocationPermission()){
            getDeviceLastLocation();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        if(checkLocationPermission())
        {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location==null)
                            {
                                return;
                            }
                            latitude=location.getLatitude();
                            longitude=location.getLongitude();
                            latLng = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in "+city).snippet(addressLine+","+countryName));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f));
                            Toast.makeText(getApplicationContext(),"Latitude: "+latitude+"\n"+"Longitude : "+longitude,Toast.LENGTH_LONG).show();
                            Log.e("GLDL","Latitude: "+latitude+"\n"+"Longitude : "+longitude);

                            try {
                                addressList=geocoder.getFromLocation(latitude,longitude,1);
                                addressLine=addressList.get(0).getAddressLine(0);
                                city=addressList.get(0).getLocality();
                                countryName=addressList.get(0).getCountryName();
                                Toast.makeText(getApplicationContext(),"Address: "+addressLine+"\n"+city+"\n"+countryName,Toast.LENGTH_LONG).show();
                                Log.e("GLDL","Address: "+addressLine+"\n"+city+"\n"+countryName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }

        Toast.makeText(getApplicationContext(),"Latitude in OnMapReady: "+latitude+"\n"+"Longitude in onMapReady: "+longitude,Toast.LENGTH_LONG).show();
        Log.e("onReadyMap()","Latitude: "+latitude+"\n"+"Longitude : "+longitude);

        //mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in "+city).snippet(addressLine+","+countryName));
        Log.e("onMapReady()","Address: "+addressLine+"\n"+city+"\n"+countryName);

        UiSettings uiSettings=mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

    }
}
