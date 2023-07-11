//package com.dhruvi.dhruvisonani.usersidexa2;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.net.ConnectivityManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.FragmentActivity;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,//1
//GoogleApiClient.OnConnectionFailedListener, LocationListener{//, GoogleMap.OnMarkerClickListener
//
//
//    private GoogleApiClient client;
//    private Location lastLocation;
//    private GoogleMap mMap,CurrentMapLOcation;
//    ProgressBar pbar_map;
//    GoogleMap map; //statically
//    public static final int REQUEST_LOCATION_CODE = 99;
//
//    private Marker currentLocationMarker,elseMarker;
//    private Marker ShopMarker;
//    private LocationRequest locationRequest;
//
//    int PROXIMITY_RADIUS =10000,count=0;
//    Double latitude,longitude;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        android.net.NetworkInfo datac = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            checkLocationPermission();
//        }
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
////        declaration();
//        initialization();
//
//        }
//        else{
//            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
//            builder.setTitle("No internet Connection");
//            builder.setMessage("This app requires Internet Connection so.. \n 1. Close this app\n 2. Turn On Internet \n 3. Use efficiently");
//            builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //dialog.dismiss();
//                    finish();
//                }
//            });
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//        }
//    }
//
//    public void initialization(){
//
//    }
//    //handles permission. display popup
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case REQUEST_LOCATION_CODE:
//                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                        if(client == null){
//                            buildApiClient();
//                        }
//                        mMap.setMyLocationEnabled(true);
//
//                    }
//                }
//                else {
//                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//        }
//    }
//
//    //map is ready to use
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        //this is for finding dynamically
//        mMap = googleMap;
//        CurrentMapLOcation = googleMap;
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            buildApiClient();
//            mMap.setMyLocationEnabled(true);
//        }
//
//
//        //to show static location
////        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
////                .findFragmentById(R.id.map);
////        mapFragment.getMapAsync(this);
//
////        mMap = googleMap;
//
//        CurrentMapLOcation = googleMap;
//        LatLng SSASIT = new LatLng(19.169257,73.341601);
//        LatLng Hindustan = new LatLng(21.2140,72.8599);
//        LatLng Raghukul = new LatLng(21.2215,72.5282);
//        LatLng ambika = new LatLng(21.2026,72.8386);
////        mMap.addMarker(new MarkerOptions().position(Hindustan).title("Hindustan")).showInfoWindow();
//        elseMarker = CurrentMapLOcation.addMarker(new MarkerOptions().position(Raghukul).title("Raghukul"));//.showInfoWindow();
//        CurrentMapLOcation.addMarker(new MarkerOptions().position(ambika).title("Ambika")).showInfoWindow();
//
//        CurrentMapLOcation.addMarker(new MarkerOptions().position(Hindustan).title("Hindustan")).showInfoWindow();
//
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(Hindustan));
//        CurrentMapLOcation.moveCamera(CameraUpdateFactory.newLatLng(Hindustan));
//        CurrentMapLOcation.moveCamera(CameraUpdateFactory.newLatLng(Raghukul));
//        CurrentMapLOcation.moveCamera(CameraUpdateFactory.newLatLng(ambika));
//        CurrentMapLOcation.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                Toast.makeText(MapsActivity.this, "dvbj", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MapsActivity.this,ShopInformationBottomActivity.class));
//                return true;
//            }
//        });
//
////        currentLocationMarker.setOnMarkerClickListener(this);
//    }
//    private String getUrl(double latitide, double longitude, String nearbyPlace){
//        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//        googlePlaceUrl.append("location"+latitide+","+longitude);
//        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
//        googlePlaceUrl.append("&type="+nearbyPlace);
//        googlePlaceUrl.append("&sensor=true");
//        googlePlaceUrl.append("&key="+"AIzaSyDx07t7a5JJmP-o07QCiMs9KxRCx5j8pnU"); //here api key
//        return googlePlaceUrl.toString();
//    }
//    protected synchronized void buildApiClient(){
//        client = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//
//        client.connect();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//        lastLocation = location;
//
//        if(currentLocationMarker != null){
//            currentLocationMarker.remove();
//        }
//        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Location");//.draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ddevice));
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//
//        mMap.addMarker(markerOptions);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
//
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                if(marker == currentLocationMarker)
//                {Toast.makeText(MapsActivity.this, "dsfd", Toast.LENGTH_SHORT).show();
//                return true;}
//                else{
//                startActivity(new Intent(MapsActivity.this,ShopInformationBottomActivity.class));
//                return false;}
//            }
//
//        });
//        if(client!=null){
//            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
//        }
//
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {//when connected
//        //create location request object
//        locationRequest = new LocationRequest();
//        locationRequest.setInterval(1000);
//        locationRequest.setFastestInterval(1000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
//        }
//    }
//
//    public boolean checkLocationPermission(){
//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
//            }
//            else{
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
//            }
//
//            return false;
//        }
//        else{
//            return true;
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { //failed
//
//    }
////    @Override
////    public boolean onMarkerClick(Marker marker) {
////
////        if (marker != elseMarker) {
//////            final Timer t = new Timer();
//////            TimerTask tt = new TimerTask() {
//////                @Override
//////                public void run() {
//////                    count++;
//////
//////                    if (count == 10) {
//////                        t.cancel();
//////                    }
//////                }
//////            };
////            Intent i = new Intent(MapsActivity.this, LoginSignUpActivity.class);
////            startActivity(i);
////            return true;
////        }
////        if(marker == currentLocationMarker){
////            Toast.makeText(this, "Current", Toast.LENGTH_SHORT).show();
////            return false;
////        }
////        return false;
////    }
//}



package com.dhruvi.dhruvisonani.usersidexa2;

        import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.dhruvi.dhruvisonani.usersidexa2.Activity.LoginSignUpActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,//1
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {


    private GoogleApiClient client;
    private Location lastLocation;
    private GoogleMap mMap;//,CurrentMapLOcation;
    ProgressBar pbar_map;
    GoogleMap map; //statically
    public static final int REQUEST_LOCATION_CODE = 99;

    private Marker currentLocationMarker;
    private Marker ShopMarker;
    private LocationRequest locationRequest;

    int PROXIMITY_RADIUS =10000,count=0;
    Double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                checkLocationPermission();
            }
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            declaration();
            initialization();

        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            builder.setTitle("No internet Connection");
            builder.setMessage("This app requires Internet Connection so.. \n 1. Close this app\n 2. Turn On Internet \n 3. Use efficiently");
            builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //dialog.dismiss();
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void declaration(){
//        pbar_map = findViewById(R.id.pbar_map);
    }

    public void initialization(){

    }
    //handles permission. display popup
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(client == null){
                            buildApiClient();
                        }
                        mMap.setMyLocationEnabled(true);

                    }
                }
                else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    //map is ready to use
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //this is for finding dynamically
        mMap = googleMap;
//        CurrentMapLOcation = googleMap;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            buildApiClient();
            mMap.setMyLocationEnabled(true);
        }


        //to show static location
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        mMap = googleMap;

        LatLng SSASIT = new LatLng(19.169257,73.341601);
        LatLng Hindustan = new LatLng(21.2140,72.8599);
        LatLng Raghukul = new LatLng(21.2215,72.8782);
        LatLng ambika = new LatLng(21.2026,72.8386);
//        mMap.addMarker(new MarkerOptions().position(Hindustan).title("Hindustan")).showInfoWindow();
        mMap.addMarker(new MarkerOptions().position(Raghukul).title("Raghukul")).showInfoWindow();
        mMap.addMarker(new MarkerOptions().position(ambika).title("Ambika")).showInfoWindow();

        mMap.addMarker(new MarkerOptions().position(Hindustan).title("Hindustan")).showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(Hindustan));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Raghukul));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ambika));
        mMap.setOnMarkerClickListener(this);
//        currentLocationMarker.setOnMarkerClickListener(this);

    }

    public void onClick(View v){
        /*if(v.getId() == R.id.btn_location){
            EditText et_location = findViewById(R.id.et_location);
            String location = et_location.getText().toString();
            List<Address> addressList = null;
            MarkerOptions mo = new MarkerOptions();
            if(!location.equals("")){
                Geocoder geocoder = new Geocoder(this);
                try {
                    //show addresses
                    addressList = geocoder.getFromLocationName(location,5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for(int i = 0;i<addressList.size();i++){
                    Address myAddress = addressList.get(i);
                    LatLng latLng = new LatLng(myAddress.getLatitude(),myAddress.getLongitude());
                    mo.position(latLng);
                    mo.title("Shop num : "+(i+1));
                    mMap.addMarker(mo);
                    //camera focuses on last result
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        }*/
       /*else if(v.getId() == R.id.btn_XeroxShop){
            mMap.clear();
            String xerox = "xerox";
            String url = getUrl(latitude,longitude,xerox);
            Object dataTransfer[] = new Object[2];
            dataTransfer[0]=mMap;
            dataTransfer[1] = url;

            GetNearByPlacesData getNearByPlacesData = new GetNearByPlacesData();
            getNearByPlacesData.execute(dataTransfer);
            Toast.makeText(this, "Showing Nearby Shops ", Toast.LENGTH_LONG).show();
        }*/
    }

    private String getUrl(double latitide, double longitude, String nearbyPlace){
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location"+latitide+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyDx07t7a5JJmP-o07QCiMs9KxRCx5j8pnU"); //here api key
        return googlePlaceUrl.toString();

    }
    protected synchronized void buildApiClient(){
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        lastLocation = location;

        if(currentLocationMarker != null){
            currentLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");//.draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ddevice));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        currentLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(client!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {//when connected
        //create location request object
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }

            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { //failed

    }
    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker != currentLocationMarker) {
//
//            final Timer t = new Timer();
//            TimerTask tt = new TimerTask() {
//                @Override
//                public void run() {
//                    count++;
//
//                    if (count == 10) {
//                        t.cancel();
//                    }
//                }
//            };
//            pbar_map.setVisibility(View.GONE);

            String markerTitle = marker.getTitle();
            if(markerTitle.equals("Current Location")){
                Toast.makeText(this, "Oops! You clicked your location", Toast.LENGTH_SHORT).show();
                return true;
            }
            else {
                Intent i = new Intent(MapsActivity.this, LoginSignUpActivity.class);
                i.putExtra("markerTitle", markerTitle);
                startActivity(i);
            }
            return false;
        }
        return true;
    }
}

