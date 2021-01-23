package com.hfad.testvertagelab;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class MapScreenActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MapScreenActivity.class.getSimpleName();

    private Place[] places = null;
    private GoogleMap googleMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);
        setupToolbar();
        setupMap();
        getPlaces();
    }

    private void setupToolbar() {
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("email");
        Toolbar mActionBarToolbar = findViewById(R.id.toolbar_actionbar);
        mActionBarToolbar.setTitle(message);
        setSupportActionBar(mActionBarToolbar);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getPlaces() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String request = null;
                        try {
                            request = new RestClient().getRequest();
                            places = new Gson().fromJson(request, PlacesResult.class).places;
                            if (googleMap != null) {
                                updateMarkers(places, googleMap);
                            }
                            updateList(places);
                        } catch (Exception e) {
                            Log.e(TAG, "Error wile loading places", e);
                            final String requestResponse = request;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MapScreenActivity.this, "Error wile loading places: " + requestResponse, Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                }).start();
    }

    private void updateList(final Place[] places) {
        String[] placesNames = new String[places.length];
        for (int i = 0; i < places.length; i++) {
            Place place = places[i];
            placesNames[i] = i + ". " + place.getName();
        }

        final ListView listView = findViewById(R.id.list_places);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, R.id.place_name, placesNames));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (googleMap != null) {
                    final Place place = places[position];
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getLat(), place.getLng())));
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (places != null) {
            updateMarkers(places, googleMap);
        }
    }

    private static void updateMarkers(Place[] places, GoogleMap googleMap) {
        for (int i = 0; i < places.length; i++) {
            Place place = places[i];
            final LatLng latLng = new LatLng(place.getLat(), place.getLng());
            googleMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            if (i == places.length - 1) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }
}