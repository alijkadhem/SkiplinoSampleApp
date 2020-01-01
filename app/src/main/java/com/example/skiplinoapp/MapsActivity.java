package com.example.skiplinoapp;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReferenceSTC = database.getReference("stc");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // fetch the data from database and add the markers and cards
        fecthData();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }


    // fetches the data from the Firebase database and processes it (adds the map markers and cards)
    public void fecthData() {

        ValueEventListener postListener = new ValueEventListener() {

            // Fetches the data and listens for any changes that happen in the database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<stc> stcArray = new ArrayList<stc>();
                final ArrayList<Marker> markerArrayList = new ArrayList<>();

                // Loops through fetched data and add markers for each location
                for(DataSnapshot  postSnapshot : dataSnapshot.getChildren()){

                    stc stcObject = postSnapshot.getValue(stc.class);
                    LatLng location = new LatLng(stcObject.getLat(),stcObject.getLongitude());
                    LatLng defaultLocation = new LatLng(26.228647,50.537113);
                    float zoomLevel = 15.0f;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,zoomLevel));
                    stcArray.add(stcObject);

                    // adds the markers on the map
                    Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(stcObject.getLocation()));
                    marker.setTag(stcArray.size());
                    markerArrayList.add(marker);

                }
                // set up the recycler view adapter
                setupAdapter(stcArray);
                // set on click listener to change the colour of the marker when clicked and scroll the the appropriate card associated with that marker
                mMap.setOnMarkerClickListener(markerClickListener(markerArrayList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DATA FETCHING", "loadPost:onCancelled", databaseError.toException());

            }
        };
        databaseReferenceSTC.addValueEventListener(postListener);
    }


    // pumps the data into the adapter to display the stc cards
    public void setupAdapter(ArrayList<stc>stcArray){
        recyclerView =    findViewById(R.id.my_recycler_view);
        adapter = new RecyclerViewAdapter(stcArray,mMap);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    // on click listener that changes the colour of the clicked marker to blue and then resets to red when another marker is clicked
    // also scrolls to the corresponding card associated with that marker
    public GoogleMap.OnMarkerClickListener markerClickListener(final ArrayList<Marker>markerArrayList){

        GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int position = (int) (marker.getTag())-1;

                // scroll to the card related to the specific marker
                recyclerView.getLayoutManager().scrollToPosition(position);

                // loops through the markers on the map and sets up the colours accordingly
                for (int i=0; i<markerArrayList.size();i++) {
                    if(marker.getTag()==markerArrayList.get(i).getTag()) {
                        markerArrayList.get(i).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    }
                    else {
                        markerArrayList.get(i).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                }
                return false;
            }
        };
        return onMarkerClickListener;
    }

}


