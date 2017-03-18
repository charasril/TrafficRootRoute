package com.example.itm801.trafficrootroute;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.text.DecimalFormat;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private EditText mAddressEditText;
    private ImageButton mGeoCodingBtn;
    private GoogleMap mMapView;
    private TextView mLocationTextView;
    private GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 1000;
    private String currentLocationStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindWidget();
    }

    private void BindWidget() {
        mAddressEditText = (EditText) findViewById(R.id.mAddressEditText);
        mGeoCodingBtn = (ImageButton) findViewById(R.id.mGeoCodingBtn);
        mLocationTextView = (TextView) findViewById(R.id.mLocationTextView);
        SupportMapFragment mySupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mMapView);
        mySupportMapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        // mำการกำหนด Criteria ในการ Tracking
                        mLocationRequest = LocationRequest.create();
                        mLocationRequest
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        // ตั้งเวลาในการตรวจสอบคือ Refresh ทุกกี่วินาที
                        mLocationRequest.setInterval(UPDATE_INTERVAL);
                        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

                    }  //onConnected

                    @Override
                    public void onConnectionSuspended(int i) {
                        Toast.makeText(getApplicationContext(), "Connection is Suspended!", Toast.LENGTH_LONG).show();

                    }
                }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), "Connection is failed!", Toast.LENGTH_LONG).show();

                    }
                }).build();
//                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setTrafficEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.marker_info_content, null);
                TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
                if (marker.getTitle() != null && !marker.getTitle().equals("")) {
                    tvTitle.setText(marker.getTitle());
                    tvTitle.setVisibility(View.VISIBLE);
                }else{
                    tvTitle.setVisibility(View.GONE);
                }
                LatLng latLng = marker.getPosition();
                TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
                DecimalFormat formatter = new DecimalFormat("#,###.000");

                tvLat.setText("Latitude: " + formatter.format(latLng.latitude) + "°");
                tvLng.setText("Longtitude: " + formatter.format(latLng.longitude) + "°");

                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

    }  //onMapReady
} //MainActivity
