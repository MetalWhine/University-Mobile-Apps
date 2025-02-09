package com.example.geolocator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_FINE = 100;

    private  TravelManager manager;
    private String destinationAddress = "";
    private Geocoder coder;
    private boolean goodGeoCoding = false;
    private boolean permissionGranted = false;
    private FusedLocationProviderClient flc;

    TextView destinationCoord, currentCoord, distanceTV, timeLeftTV;
    EditText addressET;
    Button updateButton;

    String DestinationCoordinates,CurrentCoordinates,Distance,Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = new TravelManager();
        flc = LocationServices.getFusedLocationProviderClient(this);
        coder = new Geocoder(this);

        updateButton = findViewById(R.id.GetParemeters);
        addressET = findViewById(R.id.DestinationPlainText);
        destinationCoord = findViewById(R.id.CoordinatesText);
        currentCoord = findViewById(R.id.YourCoordinatesText);
        distanceTV = findViewById(R.id.DistanceText);
        timeLeftTV = findViewById(R.id.TimeTakenText);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                destinationCoord.setText(DestinationCoordinates);
                currentCoord.setText(CurrentCoordinates);
                distanceTV.setText(Distance);
                timeLeftTV.setText(Time);
            }
        });
    }

    public void setupAddresses() {
        goodGeoCoding = true;
        destinationAddress = addressET.getText().toString();
        try {
            // geocode destination
            List<Address> addresses
                    = coder.getFromLocationName(destinationAddress, 5);
            if (addresses != null) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                Location destinationLocation = new Location("destination");
                destinationLocation.setLatitude(latitude);
                destinationLocation.setLongitude(longitude);
                manager.setDestination(destinationLocation);
                destinationCoord.setText(latitude + ", " + longitude);
            }
        } catch (IOException ioe) {
            goodGeoCoding = false;
            Toast toast = Toast.makeText(getApplicationContext(), "problems withGeoCoding!-" + ioe.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }


    public void checkLocationPermission() {
        //we are just checking the permission for FINE Location, however in some cases COARSE Location maybe ok - so that can be checked as well
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionGranted = false;
            // Asking if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                //additional explanation for the user
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("The location access is needed to perform the calculations");
                builder.setTitle("Permision Request");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        makeRequest();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // No explanation needed, we can request the permission.
                makeRequest();
            }
        } else {
            permissionGranted = true;
        }
    }

    public void makeRequest() {
        //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_COARSE);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FINE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_FINE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    permissionGranted = true;
                    setupAddresses();
                    getUserLocation();

                } else {
                    // permission denied! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }
            /* in case we have request for COARSE location
            case REQUEST_CODE_COARSE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    permissionGranted = true;

                } else {
                    // permission denied! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }
             */
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void getUserLocation() {
        //We are getting the last known location on the device (which might not be the current one).
        //We are not requesting location updates.

        checkLocationPermission(); //first to check whether we have permission for fine location
        flc.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null && goodGeoCoding) {
                            currentCoord.setText(location.getLatitude() + ", " + location.getLongitude());
                            distanceTV.setText(manager.kmToDestination(location));
                            timeLeftTV.setText(manager.timeToDestination(location));
                        }
                    }
                });
    }

    //  ****if we want to request location updates****
    public void updateLocationRequest() {
        //create location request
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setMinUpdateIntervalMillis(10000)
                .setMaxUpdateDelayMillis(20000)
                .build();

        //add the location settings
        LocationSettingsRequest.Builder lsrBuilder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(lsrBuilder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                checkLocationPermission(); //first to check whether we have permission for fine location
                flc.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            }
        });
        //can be added OnFailure:Listener
    }
    public final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            getUserLocation();
            flc.removeLocationUpdates(locationCallback); //remove location updates

            //for (Location location : locationResult.getLocations()){
            //    Toast toast = Toast.makeText(getApplicationContext(), "location update", Toast.LENGTH_LONG);
            //    toast.show();
            //}
        }
    };

    @Override
    protected void onPause(){
        super.onPause();
        flc.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateLocationRequest();
    }
}