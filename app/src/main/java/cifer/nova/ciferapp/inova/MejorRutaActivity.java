package cifer.nova.ciferapp.inova;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cifer.nova.ciferapp.inova.Modulos.DirectionFinder;
import cifer.nova.ciferapp.inova.Modulos.DirectionFinderListener;
import cifer.nova.ciferapp.inova.Modulos.Route;

import cifer.nova.ciferapp.inova.R;

import cifer.nova.ciferapp.inova.Usuario.Usuario;
import cifer.nova.ciferapp.inova.Utils.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MejorRutaActivity extends AppCompatActivity
        implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationSource.OnLocationChangedListener
        ,DirectionFinderListener
{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static boolean ACTIVITY_OPEN;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;


    public double latitud_m;
    public double longitud_m;
    public String origin;
    public LocationListener lolistener;
    private LocationManager locationListener;

    private Button buttonmapa;

    private double coleccioDeDistancias = 0.0;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private String UNIT_ID = "0";

    public Bundle dirinten;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;



    private FloatingActionButton actionButtonCaminar;
    private FloatingActionButton actionButtonCarro;
    private FloatingActionButton actionButtonBici;
    private ImageView imageViewMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mejor_ruta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_33);
        mapFragment.getMapAsync(this);

        buttonmapa = (Button) findViewById(R.id.VerRutas);

        actionButtonCaminar = (FloatingActionButton) findViewById(R.id.fabwalk);
        actionButtonCaminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MejorRutaActivity.this, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        locationListener = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationListener.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertNoGps();
        }else{

        }

        Intent intento = getIntent();
        dirinten = intento.getExtras();

        //Toast.makeText(MejorRutaActivity.this,ruta_string,Toast.LENGTH_LONG).show();

        lolistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location locationn) {
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

                //finish();
            }
        };
        //sendRequest("walking",ruta_string);

        /*actionButtonCaminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewMode.setImageResource(R.drawable.walk);
                sendRequest("walking","");
            }
        });
        actionButtonCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewMode.setImageResource(R.drawable.car);
                sendRequest("driving","");
            }
        });
        actionButtonBici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewMode.setImageResource(R.drawable.bike);
                sendRequest("bicycling","");
            }
        });*/


        /*------get user instance-------*/
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, RegistarseMainThisIsActivity.class));
        }

    }

    private void guardarMapa() {

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(firebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                DatabaseReference newRef = database.getReference("Ruta");
                newRef.child(firebaseUser.getUid()).child("lat").setValue(user.getLat());
                newRef.child(firebaseUser.getUid()).child("lng").setValue(user.getLng());
                newRef.child(firebaseUser.getUid()).child("Initlat").setValue(user.getInitlat());
                newRef.child(firebaseUser.getUid()).child("Initlng").setValue(user.getInitlng());
                if(user == null){return;}
                //Index.add(user.getNombre());
                //editTextNombre.setText(user.getNombre());
                //editTextApellido.setText(user.getApellido());
                //textViewProfile.setText(user.getNombre() +" "+" "+ user.getApellido());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "espere...", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        //myUbication();
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        //String ruta_string = dirinten.get("LOCATION_GPS_LAT")+","+dirinten.get("LOCATION_GPS_LNG");
        ProgressDialog dialog = new ProgressDialog(MejorRutaActivity.this);
        dialog.setMessage("Cargando mapa...");
        dialog.show();
        enableMyLocation();
        startListenigGPS(dialog);
        //myUbication();

    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void myUbication(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }



        locationListener = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationListener.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //actualizarUbicacion(location);
        locationListener.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1, mLocationListener);

        if (location != null){
            latitud_m = location.getLatitude();
            longitud_m = location.getLongitude();
            origin = latitud_m +","+longitud_m;
        }else {

            //Toast.makeText(MejorRutaActivity.this,location.getLatitude()+"/"+location.getLongitude()+"",Toast.LENGTH_LONG).show();
        }

    }

    private void verRutas() {

    }
    public void sendRequest(final String mode, final String destination, String orrigin){

        try {

            //Toast.makeText(MejorRutaActivity.this, " modo:"+mode+"origin: "+orrigin+" destination:" + destination, Toast.LENGTH_SHORT).show();

            new DirectionFinder(MejorRutaActivity.this,orrigin, destination, mode).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //mMap.addMarker(new MarkerOptions().position(Initt).title("Inicio").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        //
        // mMap.addMarker(new MarkerOptions().position(latLng).title("Tu ultima ubicacion").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        //if(user == null){return;}
        //Index.add(user.getNombre());
        //editTextNombre.setText(user.getNombre());
        //editTextApellido.setText(user.getApellido());
        //textViewProfile.setText(user.getNombre() +" "+" "+ user.getApellido());

    }
    public void onDirectionFinderStart() {
        progressDialog = new ProgressDialog(MejorRutaActivity.this);

        progressDialog.setTitle("Encontrando dirección!!!");
        progressDialog.setMessage("Espere un momento, esto puede tardar segun la calidad de internet...");
        progressDialog.show();

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            List<PatternItem> dashedPattern = Arrays.asList(new Dot(), new Gap(5));
            PolylineOptions polylineOptions = new PolylineOptions().pattern(dashedPattern).geodesic(true).color(Color.GREEN).width(10).zIndex(10);


            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
        locationListener.removeUpdates(lolistener);
        locationListener = null;
    }

    private void startListenigGPS(final ProgressDialog dialog) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final String ruta_string = dirinten.get("LOCATION_GPS_LAT")+","+dirinten.get("LOCATION_GPS_LNG");

        lolistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitud_m = location.getLatitude();
                longitud_m = location.getLongitude();
                origin = latitud_m +","+longitud_m;
                //saveDataofLatLong(localInt);
                if(origin != null) {
                    dialog.dismiss();
                    sendRequest("walking", ruta_string,origin);
                }else{
                    return;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                //finish();
            }
        };
        locationListener = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Location location = locationListener.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationListener.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 1, lolistener);



    }

    private void AlertNoGps() {

        AlertDialog alert = null;

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
    public void onPause() {

        super.onPause();

        if (FirebaseDatabase.getInstance() != null) {
            //FirebaseDatabase.getInstance().goOffline();
            //Toast.makeText(this, " Pause", Toast.LENGTH_SHORT).show();

        }
    }

}
