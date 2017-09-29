package cifer.nova.ciferapp.inova;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import cifer.nova.ciferapp.inova.Clases.Create_marker_opp;
import cifer.nova.ciferapp.inova.Clases.ShowDialogForAddMarker;
import cifer.nova.ciferapp.inova.Interfaces.I_ShowDialogForAddMarker;
import cifer.nova.ciferapp.inova.R;

import cifer.nova.ciferapp.inova.Usuario.Usuario;
import cifer.nova.ciferapp.inova.Utils.PermissionUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements
        OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationSource.OnLocationChangedListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean START_LISTENIG = true;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    // creamos en location y el lisener para poder detenerlos en el metodo onPause
    private LocationManager locationManager;

    private Location location;
    private LocationListener lolistener;


    public double latitud;
    public double longitud;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference dataref;
    private DatabaseReference dataref_2;
    private DatabaseReference datarefForRuta;
    private DatabaseReference datarefForAll;

    private String DATABASE_URL;
    private String DATABASE_ALL_URL;

    private int localInt = -1;
    private List<LatLng> latLngs;

    private FloatingActionButton floatingActionButton;
    private FloatingActionButton floatingBackButton;
    private Button buttonRuta;
    private ImageView add_new_marker;

    private Polyline polyline;

    private Marker markerNew;

    private boolean faro = false;

    private AlertDialog alert = null;

    //private LongPressLocationSource mLocationSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_location);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        //databaseReference.goOnline();
        latLngs = new ArrayList<LatLng>();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_go);
        mapFragment.getMapAsync(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            //startActivity(new Intent(this, MapsActivity.class));
        }

       /* floatingActionButton = (FloatingActionButton) findViewById(R.id.fabRutero);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        datarefForRuta = FirebaseDatabase.getInstance().getReference().child("Mis_rutas").child(firebaseAuth.getCurrentUser().getUid().toString());
        datarefForAll =FirebaseDatabase.getInstance().getReference().child("all_rutas");
        DATABASE_URL = datarefForRuta.push()+"";
        DATABASE_ALL_URL = datarefForAll.push()+"";
        dataref = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL);
        dataref_2 = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_ALL_URL);
        //bton makrer_______________________________________________
        add_new_marker = (ImageView) findViewById(R.id.markar_id);
        //__________________________________________________________

        floatingBackButton = (FloatingActionButton) findViewById(R.id.fabBack);
        floatingBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //localInt = -1;
                //databaseReference.goOffline();
                Intent intent = new Intent(LocationActivity.this, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

        final int whiteValueColor = Color.parseColor("#FFFFFF");
        final int accentColor = Color.parseColor("#DEDEE6");

        buttonRuta = (Button) findViewById(R.id.Buttonruta);
        buttonRuta.setTextColor(whiteValueColor);
        buttonRuta.setTextColor(whiteValueColor);
        final int reValueColor = Color.parseColor("#F44336");


        buttonRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (START_LISTENIG) {
                    buttonRuta.setText("Parar");
                    buttonRuta.setBackgroundColor(reValueColor);
                    START_LISTENIG = false;
                    AlertContinueGrabarRuta();
                } else {
                    START_LISTENIG = true;
                    locationManager.removeUpdates(lolistener);
                    locationManager = null;
                    buttonRuta.setText("Grabar ruta");
                    buttonRuta.setBackgroundColor(accentColor);
                    GuardarTitulo();
                   /*if (lolistener != null) {
                    }else {
                        START_LISTENIG = true;
                        buttonRuta.setText("Grabar ruta");
                        buttonRuta.setBackgroundColor(accentColor);
                    }*/
                }
            }
        });
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertNoGps();
        }
    }

    private void GuardarTitulo() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LocationActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_tittle,null);
        final EditText mEditText = (EditText) mView.findViewById(R.id.titulo_edit_text);
        Button mButton = (Button) mView.findViewById(R.id.save_title);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        //titulo
        /*dataref.child("our_url").setValue(DATABASE_URL);
        dataref.child("general_url").setValue(DATABASE_ALL_URL);*/
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEditText.getText().toString().isEmpty()) {
                    dataref.child("titulo").setValue(mEditText.getText().toString().trim());
                    //dataref_2.child("titulo").setValue(mEditText.getText().toString().trim());
                    dialog.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(),"Coloque un título. ",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String getSystemOfFecha() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("E/d/MMMM/yyyy");
        String str = date.format(cal.getTime());
        return str+"";

    }


    private void AlertContinueGrabarRuta() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(LocationActivity.this);

        builder.setTitle("Grabar ruta");
        builder.setMessage("Esta apunto de grabar tu ruta, mantenga el teléfono encendido.");
        builder.setPositiveButton("OK.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataref.child("our_url").setValue(DATABASE_URL);
                dataref.child("general_url").setValue(DATABASE_ALL_URL);
                //dataref_2.child("our_url").setValue(DATABASE_ALL_URL);
                String date = getSystemOfFecha();
                dataref.child("fecha").setValue(date);
                //dataref_2.child("fecha").setValue(date);
                //dataref_2.child("id_user").setValue(firebaseAuth.getCurrentUser().getEmail().toString());
                //dataref_2 .child("shared").setValue(0);
                startListenigGPS();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            final int accentColor = Color.parseColor("#DEDEE6");
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    START_LISTENIG = true;
                    buttonRuta.setText("Grabar ruta");
                    buttonRuta.setBackgroundColor(accentColor);
            }
        });
        builder.show();


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
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertNoGps();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        myUbication();
    }


    private void verRutas() {

        polyline = mMap.addPolyline(new PolylineOptions().addAll(latLngs)
                .width(10).color(Color.BLACK));
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

    @Override
    public void onLocationChanged(Location location) {
        latitud = location.getLatitude();
        longitud = location.getLongitude();
        String msg = "Latitud: " + latitud
                + "Longitud: " + longitud;

        Toast.makeText(LocationActivity.this, msg, Toast.LENGTH_LONG).show();
        //getUserName(location);
    }

    private void getUserName(final Location location) {
        latitud = location.getLatitude();
        longitud = location.getLongitude();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.goOnline();
        final DatabaseReference ref = database.getReference(firebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Usuario user = dataSnapshot.getValue(Usuario.class);
                //Index.add(user.getNombre());
                //textViewMapa.setText(user.getNombre() + " " + user.getApellido());
                if (ref != null && localInt == 0) {
                    //Usuario usuario = new Usuario(user.getApellido(), user.getNombre(), 0.0, 0.0, longitud, latitud);
                    FirebaseUser userid = firebaseAuth.getCurrentUser();
                    //databaseReference.child(userid.getUid()).setValue(usuario);
                    //String msg = "Latitud: " + latitud + "Longitud: " + longitud;
                    LatLng lng = new LatLng(latitud, longitud);
                    latLngs.add(localInt, lng);
                    //GUARDAR LISTA DE LATLONGS

                    //CompararRuta(user.getLat(),user.getLng());
                } else {
                    if (ref != null) {
                        //Usuario usuario = new Usuario(user.getApellido(),user.getNombre(),latitud,longitud,longitud,latitud);
                        FirebaseUser userid = firebaseAuth.getCurrentUser();
                        //databaseReference.child(userid.getUid()).child("initlat").setValue(latitud);
                        //databaseReference.child(userid.getUid()).child("initlng").setValue(longitud);
                        //AQUI SE GUARDA LA RUTA DEL USUARIO
                        LatLng lng = new LatLng(latitud, longitud);
                        dataref.child(localInt + "").child("latitud:").setValue(latitud);
                        dataref.child(localInt + "").child("Longitud:").setValue(longitud);
                        //AQUI SE GUARDA LA RUTA DEL USUARIO para todos
                        //dataref_2.child(localInt + "").child("latitud:").setValue(latitud);
                        //dataref_2.child(localInt + "").child("Longitud:").setValue(longitud);
                        //__________________________________________________________
                        latLngs.add(localInt, lng);
                        //saveDataofLatLong(localInt);
                        //CompararRuta(user.getLat(),user.getLng());
                    } else {
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void saveDataofLatLong(final int localInt) {
        dataref.child(localInt + "").setValue(latLngs.get(localInt));
    }

    private void myUbication() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //actualizarUbicacion(location);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //startListenigGPS();
        //Toast.makeText(this, " on start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alert != null) {
            alert.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //locationManager.removeUpdates(lolistener);
        if (FirebaseDatabase.getInstance() != null) {
            //FirebaseDatabase.getInstance().goOffline();
            //Toast.makeText(this, " Pause", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        //Toast.makeText(this, " On Resume", Toast.LENGTH_SHORT).show();
        //finish();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationManager == null) {
            //locationManager.removeUpdates(lolistener);
            locationManager = null;
        } else {
            //do noting
        }
        //databaseReference.goOffline();
        //Toast.makeText(this, " On STOP", Toast.LENGTH_SHORT).show();

    }

    private void startListenigGPS() {
        Toast.makeText(LocationActivity.this, " ESCUCHANDO AL GPS " + localInt, Toast.LENGTH_SHORT).show();
        HabilitarOpciones();
        lolistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location locationn) {
                //PARA GUARDAR LAS COORDENADAS DES CAMINO
                getUserName(locationn);
                //PARA VER LAS LINEAS EN LA PANTALLA
                verRutas();
                //saveDataofLatLong(localInt);
                localInt++;
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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, lolistener);

    }

    private void HabilitarOpciones() {
        ShowDialogForAddMarker addMarker = new ShowDialogForAddMarker();
        final Create_marker_opp opp = new Create_marker_opp(addMarker);
        add_new_marker.setVisibility(View.VISIBLE);
        add_new_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LocationActivity.this,"nuevo marcador",Toast.LENGTH_LONG).show();
                opp.dialogForAddMarker.showInterface(LocationActivity.this,dataref,dataref_2);
                opp.dialogForAddMarker.doAddMarkers(mMap,latitud,longitud);
            }
        });
    }
}
