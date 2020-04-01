package com.example.mapy;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    DatabaseReference reff;
    private GoogleMap mMap;
    LocationManager locationManager;
    Marker marker,markerDog;
    Point point=new Point();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double yourX =location.getLatitude(); //czytaj X i Y
                    double yourY =location.getLongitude();
                    // get nazwa lokacji ponizej
                    Geocoder geocoder= new Geocoder(getApplicationContext());
                    try {
                        List<Address> addresses =
                                geocoder.getFromLocation(yourX, yourY, 1);
                        //String result = addresses.get(0).getLocality()+":";
                        //result += addresses.get(0).getCountryName();   nazwa lokajci + kraj: chwilowo niepotrzebne-wyswietla sie "you"
                        LatLng coords = new LatLng(yourX, yourY);
                        if (marker != null){
                            marker.remove();
                            marker = mMap.addMarker(new MarkerOptions().position(coords).title("You"));   //uaktualnianie pozycji markera
                            mMap.setMaxZoomPreference(20);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 12.0f));
                        }
                        else{
                            marker = mMap.addMarker(new MarkerOptions().position(coords).title("You"));
                            mMap.setMaxZoomPreference(20);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 21.0f));  //pierwszy marker
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

        }
        else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //zmiana czestotliwosci odswiezania w zaleznosc od systansu i czasu ponizej
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double yourX =location.getLatitude(); //czytaj X i Y
                    double yourY =location.getLongitude();
                    // get nazwa lokacji ponizej
                    Geocoder geocoder= new Geocoder(getApplicationContext());
                    //List<Address> addresses =
                    //geocoder.getFromLocation(yourX, yourY, 1);
                    //String result = addresses.get(0).getLocality()+":";
                    //result += addresses.get(0).getCountryName();   nazwa lokajci + kraj: chwilowo niepotrzebne-wyswietla sie "you"
                    LatLng coords = new LatLng(yourX, yourY);
                    if (marker != null){
                        marker.remove();
                        marker = mMap.addMarker(new MarkerOptions().position(coords).title("You"));   //uaktualnianie pozycji markera
                        mMap.setMaxZoomPreference(20);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 12.0f));
                    }
                    else{
                        marker = mMap.addMarker(new MarkerOptions().position(coords).title("You"));
                        mMap.setMaxZoomPreference(20);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 12.0f));  //pierwszy marker
                    }


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

        }
        //update markera psa
        reff= FirebaseDatabase.getInstance().getReference().child("coordinates").child("0");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                point.x= (double)dataSnapshot.child("latidude").getValue();
                point.y=(double)dataSnapshot.child("longitude").getValue();
                //Geocoder geocoder= new Geocoder(getApplicationContext());

                LatLng coordsDog = new LatLng(point.x, point.y);
                if (markerDog != null){
                    markerDog.remove();
                    markerDog = mMap.addMarker(new MarkerOptions().position(coordsDog).title("Your Dog"));   //uaktualnianie pozycji markera
                    //mMap.setMaxZoomPreference(20);
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordsDog, 12.0f));
                }
                else{
                    markerDog = mMap.addMarker(new MarkerOptions().position(coordsDog).title("Your Dog"));
                    //mMap.setMaxZoomPreference(20);
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordsDog, 21.0f));  //zakomentowane=kamera skierowana na uzytkowniku
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng YourLocation = new LatLng(53.928124, 18.512606);
        //mMap.addMarker(new MarkerOptions().position(YourLocation).title("YOU"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(YourLocation,10.2f));
    }
}
