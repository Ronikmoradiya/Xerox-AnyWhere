package com.dhruvi.dhruvisonani.usersidexa2.JavaClass;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GetNearByPlacesData extends AsyncTask<Object,String,String> {

    String googlePlaceDate;
    GoogleMap mMap;
    String url;
    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownLoadURL downLoadURL = new DownLoadURL();
        try {
            googlePlaceDate = downLoadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlaceDate;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearByPlaceList = null;
        DataParser parser = new DataParser();
        nearByPlaceList = parser.parse(s);
        showNearByPlaces(nearByPlaceList);
    }

    private void showNearByPlaces(List<HashMap<String,String>> nearByPlaceList){
        for(int i=0;i<nearByPlaceList.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearByPlaceList.get(i);

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            LatLng latLng = new LatLng(lat,lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : "+ vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }


}

