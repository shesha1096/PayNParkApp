package com.shesha.projects.paynparkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.shesha.projects.paynparkapp.adapters.ParkingSpotAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Button parkingSpotButton;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private ArrayList<ParkingSpot> parkingSpots;
    private RecyclerView recyclerView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        parkingSpotButton = findViewById(R.id.findParkingSpotBtn);
        recyclerView = findViewById(R.id.parkinLocationsRecyclerView);
        textView = findViewById(R.id.parkingHistoryTextView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ParkingHistoryActivity.class);
                startActivity(intent);
            }
        });
        //To get the current location of the user
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);
        parkingSpots = new ArrayList<>();
        parkingSpotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) // Check if permission is granted for accessing device location
                {
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null)
                            {
                                Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    /*
                                    Get latitude and longitude of user location
                                     */
                                    String latitude = String.valueOf(addresses.get(0).getLatitude());
                                    String longitude = String.valueOf(addresses.get(0).getLongitude());
                                    /*
                                    Pass the latitude and longitude value to the Google Places API for finding parking spots near the location
                                     */
                                    String request = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=5000&types=parking&sensor=false&key=AIzaSyAld219SNYba7wiZkU5y-OnPHXTv_JP0FM";
                                    //RequestQueue initialized
                                    requestQueue = Volley.newRequestQueue(HomeActivity.this);

                                    //String Request initialized
                                    stringRequest = new StringRequest(Request.Method.GET, request, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                Geocoder localGeocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                                                JSONObject jsonObject = new JSONObject(response);
                                                JSONArray jsonArray = jsonObject.getJSONArray("results");
                                                /*
                                                Parsing the JSON Array and extracting required information
                                                 */
                                                for (int i = 0; i < 5; i++)
                                                {
                                                    JSONObject resultObject = (JSONObject) jsonArray.get(i);
                                                    JSONObject geometryObject = resultObject.getJSONObject("geometry");
                                                    JSONObject locationObject = geometryObject.getJSONObject("location");
                                                    List<Address> addresses = localGeocoder.getFromLocation((double)locationObject.get("lat"),(double)locationObject.get("lng"),1);
                                                    Address address = addresses.get(0);
                                                    ParkingSpot parkingSpot = new ParkingSpot();
                                                    parkingSpot.setAddressLine(address.getAddressLine(0));
                                                    parkingSpot.setAddressLocality(address.getLocality());
                                                    parkingSpot.setLocationName(resultObject.getString("name"));
                                                    parkingSpot.setVicinitySpot(resultObject.getString("vicinity"));
                                                    parkingSpots.add(parkingSpot);
                                                }
                                                /*
                                                Initializing recycler view
                                                 */
                                                ParkingSpotAdapter parkingSpotAdapter = new ParkingSpotAdapter(parkingSpots,getApplicationContext(),HomeActivity.this.getSupportFragmentManager());
                                                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                                                recyclerView.setAdapter(parkingSpotAdapter);
                                            } catch (JSONException | IOException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            Log.i("ERROR","Error :" + error.toString());
                                        }
                                    });

                                    requestQueue.add(stringRequest);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });

                }
                else
                {
                    //Request permission to access User's location.
                    ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });
    }
}