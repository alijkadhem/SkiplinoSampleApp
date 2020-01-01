package com.example.skiplinoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

// Adapter class for the RecyclerView which will take in data and display it on to the stc cards
public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private ArrayList<stc> stcArrayList;
    private GoogleMap mMap ;

    // Recycler view constructor takes in an Arraylist of stc objects and the google map
    public RecyclerViewAdapter(ArrayList<stc> stcArrayList, GoogleMap mMap) {
        this.stcArrayList = stcArrayList;
        this.mMap = mMap;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView location;

        public MyViewHolder(View view) {
            super(view);
            location = (TextView) view.findViewById(R.id.tv_stc_location);
        }
    }

    // inflates the stc cards from the specified layout file
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stc_cards, parent, false);
        return new MyViewHolder(itemView);
    }


    // sets up an on click for the stc cards such that when a card is clicked the map moves to show the marker associated with that card
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final stc stcObject = stcArrayList.get(position);
        holder.location.setText(stcObject.location);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zooms in on the specified marker associated with the card that was clicked
                LatLng location = new LatLng(stcObject.getLat(),stcObject.getLongitude());
                float zoomLevel = 16.0f;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,zoomLevel));
            }
        });
    }

    @Override
    public int getItemCount() {
        return stcArrayList.size();
    }

}
