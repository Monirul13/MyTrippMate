package com.example.rana.mytrippmate.markercluster;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Rana on 12/16/2018.
 */

public class MyItems implements ClusterItem {

    private LatLng position;
    private String title;
    private String snippet;

    public MyItems(double lat,double lng, String title, String snippet) {
        position = new LatLng(lat,lng);
        this.title = title;
        this.snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
     return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
