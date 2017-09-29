package cifer.nova.ciferapp.inova;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cifer.nova.ciferapp.inova.R;

import cifer.nova.ciferapp.inova.Utils.PermissionUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VerMisRutasohYeahActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationSource.OnLocationChangedListener{

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private LocationManager mLocationManager;
    private Location mLocation;
    private LocationListener mLocationListener;
    private boolean mPermissionDenied = false;

    private DatabaseReference databaseReference;

    private FirebaseAuth  firebaseAuth;

    private List<LatLng> latLngs;
    private LatLng myLatlong;


    private Button acepButon;


    private LocationManager locationManager;

    private Location location;
    private LocationListener lolistener;
    private ProgressDialog progressDialog;

    private AlertDialog alert = null;

    private Bundle dirIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_mis_rutas_oh_yeah);

        Intent intento = getIntent();
        dirIntent = intento.getExtras();



        firebaseAuth = FirebaseAuth.getInstance();

        /*actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        acepButon = (Button) findViewById(R.id.aceptbutton);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertNoGps();
        } else {
            if (dirIntent.getInt("GET_RUTA") == 1) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Espere por favor...");
                progressDialog.show();
                startListenigGPS();
                acepButon.setVisibility(View.VISIBLE);
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_my);
        mapFragment.getMapAsync(this);
    }
    private void AlertNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();

                    }
                });
        alert = builder.create();
        alert.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
        acepButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myLatlong != null)
                {
                    Intent intent = new Intent(VerMisRutasohYeahActivity.this,ListaTuristicosActivity.class);
                    intent.putExtra("LOCATION_L",myLatlong.latitude);
                    intent.putExtra("LOCATION_O",myLatlong.longitude);
                    intent.putExtra("idd",dirIntent.getInt("THI_LOC"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(VerMisRutasohYeahActivity.this, dirIntent.getInt("THI_LOC")+"",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_for_my, menu);
        return true;
    }



    private void startListenigGPS() {

        lolistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location locationn) {
                double l = locationn.getLatitude();
                double o = locationn.getLongitude();
                myLatlong = new LatLng(l,o);
                progressDialog.dismiss();
                mMap.addMarker(new MarkerOptions().position(myLatlong).title("Pocision marcada").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatlong, 16));
                exitListener();
                //saveDataofLatLong(localInt);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                locationManager.removeUpdates(lolistener);
                locationManager = null;
                //finish();
            }
        };
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, lolistener);

    }

    private void exitListener() {
        locationManager.removeUpdates(lolistener);
        locationManager = null;
       /*final ProgressDialog progresDialog = new ProgressDialog(VerMisRutasohYeahActivity.this);
        progresDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progresDialog.setMessage("Arreglando Ubicación…...");
        progresDialog.setMax(100);
        progresDialog.setProgress(0);
        Thread thread = null;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (progress<= 100 ){
                    try {
                        progresDialog.setProgress(progress);
                        progress++;
                        Thread.sleep(200);

                    }catch (Exception ex){
                    }
                }
                progresDialog.dismiss();*/
                /*CODE HERE

            }
        });
        thread.start();
        progresDialog.show();*/
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
