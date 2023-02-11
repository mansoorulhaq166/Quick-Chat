package com.example.quickchat.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.quickchat.R;
import com.example.quickchat.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    //final static String url = "http://192.168.43.231/ChatappApis/images/";
    // String imagePath = url + sendImage;
    BitmapDescriptor image = null;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        image = BitmapDescriptorFactory.fromResource(R.drawable.profile_image);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng curntlatLng = new LatLng(33.7808, 72.2883);
        // MarkerOptions markerOptions = new MarkerOptions().position(curntlatLng).title("My Location");
        // mMap.addMarker(markerOptions);
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(curntlatLng));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curntlatLng, 12f));

        // Circle
        mMap.addCircle(new CircleOptions().center(curntlatLng)
                .radius(1000)
                .strokeColor(Color.DKGRAY));

        // polygon
        /*mMap.addPolygon(new PolygonOptions().add(new LatLng(33.7808, 72.2883),
                        new LatLng(33.7808, 73.2883),
                        new LatLng(34.7808, 73.2883),
                        new LatLng(34.7808, 72.2883),
                        new LatLng(33.7808, 72.2883))
                .fillColor(Color.YELLOW).strokeColor(Color.BLUE)
        );*/

        // overlay
        mMap.addGroundOverlay(new GroundOverlayOptions()
                .position(curntlatLng, 500f, 500f)
                .image(image)
                .clickable(true)
        );

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Your Location");
                mMap.addMarker(markerOptions);
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                try {
                    ArrayList<Address> addressArrayList = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    Log.d("Address", addressArrayList.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MapsActivity.this, HomeActivity.class));
        super.onBackPressed();
    }
}