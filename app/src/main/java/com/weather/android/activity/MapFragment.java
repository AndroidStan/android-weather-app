package com.weather.android.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weather.android.R;

public class MapFragment extends Fragment implements OnMapReadyCallback{

    GoogleMap googleMap;
    MapView mapView;
    View view;

    public MapFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.view = inflater.inflate(R.layout.map_fragment, container, false);
        return this.view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        MapsInitializer.initialize(getContext());

        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        String cityName = getArguments().getString("cityName");
        Double  latitude = getArguments().getDouble("latitude"),
                longtitude = getArguments().getDouble("longtitude");

        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtitude)).title(cityName).snippet("Will go there"));

        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(latitude, longtitude)).zoom(14).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
    }

}
