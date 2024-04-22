package com.example.mygooglemaps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, MainActivity1, GoogleMap.OnMarkerClickListener ,AddEventDialogFragment.DialogListener{
    private HashMap<Marker, String> markerTextMap = new HashMap<>();
    Marker marker1,marker2;
    private EditText searchEditText;
    private Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(MainActivity.this);

        searchEditText = findViewById(R.id.editTextText);
        searchButton = findViewById(R.id.button);

        // Set OnClickListener for the searchButton
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }

            private void performSearch() {
                // Get the search query from the EditText
                String query = searchEditText.getText().toString().toLowerCase();

                // Iterate through markerTextMap and check if the event exists
                boolean eventFound = false;
                for (HashMap.Entry<Marker, String> entry : markerTextMap.entrySet()) {
                    String event = entry.getValue();
                    if (event.toLowerCase().contains(query)) {
                        // Show toast indicating the event is in the corresponding marker
                        Marker marker = entry.getKey();
                        String markerTitle = marker.getTitle();
                        Toast.makeText(MainActivity.this, "Event '" + event + "' is in marker '" + markerTitle + "'", Toast.LENGTH_SHORT).show();
                        eventFound = true;
                        break; // Stop searching once the event is found
                    }
                }

                // If event is not found, show a toast indicating no matching event was found
                if (!eventFound) {
                    Toast.makeText(MainActivity.this, "No matching event found", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng sydney = new LatLng(-34, 151);
        LatLng india = new LatLng( 13.3,77.11);

        marker1=googleMap.addMarker(new MarkerOptions().position(sydney).title("Sydney").draggable(true));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



       Marker marker2= googleMap.addMarker(new MarkerOptions().position(india).title("India").draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(india));


 googleMap.setOnMarkerClickListener(this);
    }


    @Override
    public boolean onMarkerClick(final Marker markera) {
        // Display the dialog fragment
        // Display the dialog fragment
        marker1=markera;
        marker2=markera;
        FragmentManager fm = getSupportFragmentManager();
        AddEventDialogFragment dialogFragment = new AddEventDialogFragment();
        dialogFragment.setDialogListener(this); // Set the DialogListener
        dialogFragment.show(fm, "add_event_dialog");

        return true;
    }


    @Override
    public void onDialogOK(String event) {
        // Store the event in the HashMap
        markerTextMap.put(marker1, event);
        markerTextMap.put(marker2,event);
    }

}