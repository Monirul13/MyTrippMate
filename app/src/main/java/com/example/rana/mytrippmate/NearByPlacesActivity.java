package com.example.rana.mytrippmate;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.rana.mytrippmate.markercluster.MyItems;
import com.example.rana.mytrippmate.nearbyplaces.NearByPlacesResponse;
import com.example.rana.mytrippmate.nearbyplaces.NearByPlacesService;
import com.example.rana.mytrippmate.nearbyplaces.Result;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NearByPlacesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final String PLACES_BASE_URL="https://maps.googleapis.com/maps/api/";
    private ClusterManager<MyItems> clusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        clusterManager=new ClusterManager<MyItems>(this,mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);

        // Add a marker in Sydney and move the camera 23.7509° N, 90.3936° E
        LatLng sydney = new LatLng(23.7509, 90.3936);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in BITM"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,13f));
        UiSettings uiSettings=mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        //mMap.setMyLocationEnabled(true "AIzaSyB7R0lYkPDQVrv6daIelE4IQAe4TJu0gsc","AIzaSyB7R0lYkPDQVrv6daIelE4IQAe4TJu0gsc");
        getNearByPlaces();
    }

    private void getNearByPlaces() {
        Retrofit retrofit=new Retrofit.Builder().baseUrl(PLACES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NearByPlacesService nearByPlacesService=retrofit.create(NearByPlacesService.class);

        String apiKey=getString(R.string.near_by_places_api_key);
        String endUrl=String.format("place/nearbysearch/json?location=23.7516,90.3943&radius=1500&type=restaurant&key=%s",apiKey);
        nearByPlacesService.getNearByPlaces(endUrl)
                .enqueue(new Callback<NearByPlacesResponse>() {
                    @Override
                    public void onResponse(Call<NearByPlacesResponse> call, Response<NearByPlacesResponse> response) {
                        if(response.isSuccessful())
                        {
                            NearByPlacesResponse nearByPlacesResponse=response.body();
                            List<Result> results=nearByPlacesResponse.getResults();
                            Toast.makeText(NearByPlacesActivity.this, "size: "+results.size(), Toast.LENGTH_SHORT).show();

                            for(Result result:results)
                            {
                                double lat=result.getGeometry().getLocation().getLat();
                                double lng=result.getGeometry().getLocation().getLng();
                                LatLng latLng=new LatLng(lat,lng);
//                                mMap.addMarker(new MarkerOptions().position(latLng).title(result.getName())
//                                        .snippet(result.getVicinity()));

                                MyItems myItems=new MyItems(lat,lng,result.getName(),result.getVicinity());
                                clusterManager.addItem(myItems);
                                clusterManager.cluster();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NearByPlacesResponse> call, Throwable t) {

                    }
                });

    }
}
