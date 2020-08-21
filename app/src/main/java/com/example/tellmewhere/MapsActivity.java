package com.example.tellmewhere;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String received_country_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //received country name from main activity
        received_country_name = getIntent().getStringExtra(CountryDataSource.COUNTRY_KEY);
        if(received_country_name==null)
        {
            received_country_name = CountryDataSource.DEFAULT_COUNTRY_NAME;
        }

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Patna Zoo and move the camera
//        LatLng patna_zoo = new LatLng(25.603954, 85.102135);
//
         //Setting and zooming the camera
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(patna_zoo,15.0f);
//        mMap.moveCamera(cameraUpdate);
//
        //Setting the marker
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(patna_zoo);
//        markerOptions.title("Patna Zoo");
//        markerOptions.snippet("One of the biggest Zoo in India");
//
//        mMap.addMarker(markerOptions);
//
            //Drawing a circle in the map
//        CircleOptions circleOptions = new CircleOptions();
//        circleOptions.center(patna_zoo);
//        circleOptions.radius(300.00);
//        circleOptions.strokeWidth(10.0f);
//        circleOptions.strokeColor(Color.YELLOW);
//        //circleOptions.fillColor(Color.RED);
//
//
//        mMap.addCircle(circleOptions);

        double country_latitude = CountryDataSource.DEFAULT_COUNTRY_LATITUDE;
        double country_longitude = CountryDataSource.DEFAULT_COUNTRY_LONGITUDE;

        CountryDataSource countryDataSource = MainActivity.sCountryDataSource;
        String country_and_message = countryDataSource.getCountryInfo(received_country_name);

        //geo-coding process, i.e. getting the long and lat using the country name
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            String country_address = received_country_name;
            List<Address> country_addresses_list = geocoder.getFromLocationName(country_address, 10);

            if (country_address != null)
            {
                country_latitude = country_addresses_list.get(0).getLatitude();
                country_longitude = country_addresses_list.get(0).getLongitude();
            }
            else
            {
                received_country_name = CountryDataSource.DEFAULT_COUNTRY_NAME;
            }
        }
        catch (Exception e){
            received_country_name=CountryDataSource.DEFAULT_COUNTRY_NAME;
        }

        LatLng latLng = new LatLng(country_latitude,country_longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,5.0f);
        mMap.moveCamera(cameraUpdate);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(country_and_message);
        markerOptions.snippet("Successfully found");

        mMap.addMarker(markerOptions);

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(400.00);
        circleOptions.strokeWidth(10.0f);
        circleOptions.strokeColor(Color.MAGENTA);

        mMap.addCircle(circleOptions);

    }
}