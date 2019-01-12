package com.example.rana.mytrippmate.nearbyplaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Rana on 12/16/2018.
 */

public interface NearByPlacesService {
    @GET
    Call<NearByPlacesResponse> getNearByPlaces(@Url String endUrl);
}
