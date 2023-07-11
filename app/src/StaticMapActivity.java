package com.dhruvi.dhruvisonani.usersidexa2.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.dhruvi.dhruvisonani.usersidexa2.R;
import com.dhruvi.dhruvisonani.usersidexa2.ShopInformationBottomActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class StaticMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_map);
        declaration();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng SSASIT = new LatLng(19.169257,73.341601);
        map.addMarker(new MarkerOptions().position(SSASIT).title("SSASIT"));
        map.moveCamera(CameraUpdateFactory.newLatLng(SSASIT));
        map.setOnMapClickListener(this);
    }
    private void declaration(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_static_map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapClick(LatLng latLng) {
        Intent i = new Intent(StaticMapActivity.this, ShopInformationBottomActivity.class);
        startActivity(i);
    }
}