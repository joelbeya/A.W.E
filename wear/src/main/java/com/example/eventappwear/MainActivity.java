package com.example.eventappwear;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainActivity extends WearableActivity
    implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnSuccessListener<Location>
{

    public static String lon = "3.8767";
    public static String lat = "43.6108";

    public static final String url = "https://hmin309-embedded-systems.herokuapp.com/message-exchange/messages/";

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.FRANCE);

    private TextView mTextView;
    private CapabilityClient capabilityClient;
    private Set<Node> nodes = new HashSet<>();
    private int counter = 0;
    private TextView mClockView;
    private LocationCallback lc;
    private TextView gpsPresence;
    private LinearLayout mContainerView;
    public FusedLocationProviderClient flpc;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContainerView = (LinearLayout) findViewById(R.id.container);
        gpsPresence = (TextView) findViewById(R.id.GPSpresence);
        mClockView = (TextView) findViewById(R.id.clock);
        mTextView = (TextView) findViewById(R.id.text);
        capabilityClient = Wearable.getCapabilityClient(this);

        Button b1 = findViewById(R.id.getlocation);

        b1.setOnClickListener(view -> {
            getlocation(view);
        });

        Button b = (Button)findViewById(R.id.button);
        // Enables Always-On

        setAmbientEnabled();

        b.setOnClickListener(view -> {
            AsyncHTTPGET taskGet = new AsyncHTTPGET(MainActivity.this);
            AsyncHTTPPost taskPost = new AsyncHTTPPost(MainActivity.this);
            taskPost.execute(url);
            taskGet.execute(url);
        });

        ImageView imgGPS = (ImageView)findViewById(R.id.gps);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            Log.d("CIO", "This watch has no GPS.");
            gpsPresence.setText("No. Using:");
            imgGPS.setMaxWidth(250);
            imgGPS.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.telgps4));
        }
        else {
            Log.d("CIO", "GPS available :)");
            gpsPresence.setText("Yes :)");
            imgGPS.setImageBitmap(BitmapFactory.decodeResource(getResources(), android.R.drawable.presence_online));
        }

        GoogleApiClient gapi = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        gapi.connect();

        updateDisplay();
    }


    public void clickSearchNodes(View view) {
        Log.i("CIO", "Searching nodes...");

        Task<Map<String, CapabilityInfo>> capabilitiesTask =
                capabilityClient.getAllCapabilities(CapabilityClient.FILTER_REACHABLE);

        capabilitiesTask.addOnSuccessListener(
                capabilityInfoMap -> {
                    nodes.clear();
                    if (capabilityInfoMap.isEmpty()) {
                        Log.i("CIO", "No capability advertised :/"); return;
                    }

                    for (String capabilityName : capabilityInfoMap.keySet()) {
                        CapabilityInfo capabilityInfo = capabilityInfoMap.get(capabilityName);
                        Log.i("CIO", "Capability found: " + capabilityInfo.getName());
                        if (capabilityInfo != null) {
                            nodes.addAll(capabilityInfo.getNodes());
                        }
                    }
                    Log.i("CIO", "Nodes found: " + nodes.toString());
                    TextView found = (TextView)findViewById(R.id.foundNodes);
                    StringBuilder printNodes = new StringBuilder();
                    for (Node node : nodes)
                        printNodes.append(node.getDisplayName()).append(" ");
                    found.setText("Nodes: " + printNodes);
                });
    }

    public void clickSendMessage(View view) {
        Log.i("CIO", "Sending a message...");
        MessageClient clientMessage = Wearable.getMessageClient(this);
        for (Node node : nodes) {
            Log.i("CIO", "  - to " + node.getId());
            String message = "Hello " + counter;
            Task<Integer> sendMessageTask =
                    clientMessage.sendMessage(node.getId(),"CIO", message.getBytes());
            counter++;

        }
    }


    // Should check if the permission is granted for this app
    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("CIO", "GAPI connected");

        // Get the location provider through the GoogleApi
        flpc = LocationServices.getFusedLocationProviderClient(this);

        // Check location availability
        @SuppressLint("MissingPermission") Task<LocationAvailability> locAvailable = flpc.getLocationAvailability();
        locAvailable.addOnSuccessListener(locationAvailability -> Log.i("CIO", "Location is available = " + locationAvailability.toString()));

        // Ask for update of the location
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000).setFastestInterval(1000);
        lc = new LocationCallback();
        flpc.requestLocationUpdates(locationRequest, lc, null);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("CIO", "GAPI supsended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("CIO", "Error with GAPI connection");
    }

    // Should check if the permission is granted for this app
    public void getlocation(View view) {
        Task<Location> loc = flpc.getLastLocation();
        loc.addOnSuccessListener(this);
    }

    // Callback for the location task
    @Override
    public void onSuccess(Location loc) {
        Log.i("CIO", "Task completed.");
        if (loc != null) {
            DecimalFormat df = new DecimalFormat("#.##");
            lon = df.format(loc.getLongitude());
            lat = df.format(loc.getLatitude());
            Log.i("CIO", "Location: " + lon  + " , " + lat );
            TextView latTV = (TextView)findViewById(R.id.lat);
            latTV.setText("latitude: " + lon);
            TextView lonTV = (TextView)findViewById(R.id.lon);
            lonTV.setText("longitude: " + lat);
        }
        else
        {
            Log.i("CIO", "No defined location ! Are you inside ? Have you authorized location on the smartwatch ?");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (lc != null)
            flpc.removeLocationUpdates(lc);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            Log.i("EventAppWear", "Go to Ambient mode !");
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setTextColor(getResources().getColor(android.R.color.white));

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
            findViewById(R.id.getlocation).setVisibility(View.GONE);
            findViewById(R.id.GPSpresence).setVisibility(View.GONE);
            findViewById(R.id.labelGPS).setVisibility(View.GONE);
            findViewById((R.id.gps)).setVisibility(View.GONE);
            findViewById(R.id.text).setVisibility(View.GONE);
            findViewById(R.id.lat).setVisibility(View.GONE);
            findViewById(R.id.lon).setVisibility(View.GONE);
            findViewById(R.id.button).setVisibility(View.GONE);
            findViewById(R.id.button2).setVisibility((View.GONE));
            findViewById(R.id.button3).setVisibility((View.GONE));
            findViewById(R.id.text2).setVisibility(View.GONE);
            findViewById(R.id.foundNodes).setVisibility(View.GONE);
            findViewById(R.id.lat).setVisibility((View.GONE));
            findViewById(R.id.lon).setVisibility(View.GONE);
            mClockView.setVisibility(View.VISIBLE);
        } else {
            Log.i("EventAppWear", "Go to Interactive mode !");
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.VISIBLE);
            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
            findViewById(R.id.getlocation).setVisibility(View.VISIBLE);
            findViewById(R.id.GPSpresence).setVisibility(View.VISIBLE);
            findViewById(R.id.labelGPS).setVisibility(View.VISIBLE);
            findViewById((R.id.gps)).setVisibility(View.VISIBLE);
            findViewById(R.id.text).setVisibility(View.VISIBLE);
            findViewById(R.id.lat).setVisibility(View.VISIBLE);
            findViewById(R.id.lon).setVisibility(View.VISIBLE);
            findViewById(R.id.button).setVisibility(View.VISIBLE);
            findViewById(R.id.button2).setVisibility((View.VISIBLE));
            findViewById(R.id.button3).setVisibility((View.VISIBLE));
            findViewById(R.id.text2).setVisibility(View.VISIBLE);
            findViewById(R.id.foundNodes).setVisibility(View.VISIBLE);
            findViewById(R.id.lat).setVisibility(View.VISIBLE);
            findViewById(R.id.lon).setVisibility(View.VISIBLE);
        }
    }
}
