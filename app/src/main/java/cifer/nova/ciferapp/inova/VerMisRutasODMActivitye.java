package cifer.nova.ciferapp.inova;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cifer.nova.ciferapp.inova.Clases.Buid_dialog_all;
import cifer.nova.ciferapp.inova.Clases.Create_Dialog_help;
import cifer.nova.ciferapp.inova.Clases.Create_SaveShared;
import cifer.nova.ciferapp.inova.Clases.SaveSharedRoute;
import cifer.nova.ciferapp.inova.Utils.PermissionUtils;

public class VerMisRutasODMActivitye extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationSource.OnLocationChangedListener {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private Bundle dirIntent_2;
    private DatabaseReference databaseReference;
    private AlertDialog alert = null;
    private boolean mPermissionDenied = false;
    private double latitud;
    private double longitud;
    private List<LatLng> latLngs;
    private LatLng myLatlong;
    private Polyline polyline;

    private Button buttonShare;

    private FirebaseAuth  firebaseAuth;
    private DatabaseReference globalDatabaseReference;

    private DrawerLayout drawerLayout;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_mao_odm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intento = getIntent();

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        globalDatabaseReference= FirebaseDatabase.getInstance().getReference();



        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            //startActivity(new Intent(this,MainActivity.class));
        } else {
            drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout_6);
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_6);
            if (navigationView != null) {
                setupNavigationDrawerContent(navigationView);
                //final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                View avHeaderView = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);
                final TextView tvHeaderName = (TextView) avHeaderView.findViewById(R.id.header_name);
                final ImageView foto = (ImageView) avHeaderView.findViewById(R.id.header_foto);
                TextView iduser = (TextView) avHeaderView.findViewById(R.id.header_id);
                DatabaseReference refname = globalDatabaseReference.child("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("nombre");
                refname.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null) {
                            tvHeaderName.setText(firebaseAuth.getCurrentUser().getDisplayName());
                        } else {
                            tvHeaderName.setText(dataSnapshot.getValue() + "");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                DatabaseReference reffoto = globalDatabaseReference.child("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("foto");
                reffoto.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null) {
                            Toast.makeText(VerMisRutasODMActivitye.this, "Defaul foto".toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Picasso.with(VerMisRutasODMActivitye.this).load(dataSnapshot.getValue() + "").into(foto);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                iduser.setText(firebaseAuth.getCurrentUser().getEmail() + "");

                //getUserName();
            }

            //getMyRutaPolygon();

            // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(last, 17));
        }

        dirIntent_2 = intento.getExtras();

        latLngs = new ArrayList<LatLng>();

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (dirIntent_2.get("URL") != null) {
            databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(dirIntent_2.get("URL") + "");
            //Toast.makeText(VerMisRutasOjosDeMedusaActivitye.this, databaseReference + "☺", Toast.LENGTH_SHORT).show();
          buttonShare = (Button) findViewById(R.id.boton_compartir);
            buttonShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetDialog();
                }
            });

            getMyRutaPolygon();
            puTheMakers();
        } else {
            Toast.makeText(VerMisRutasODMActivitye.this, "error en conexion...", Toast.LENGTH_SHORT).show();
        }

        if (dirIntent_2.getInt("SHARE_BUTT") == 1) {
            buttonShare.setVisibility(View.GONE);
        } else {
            //Toast.makeText(VerMisRutasODMActivitye.this, "error en conexion...", Toast.LENGTH_SHORT).show();
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertNoGps();
        }
        enableMyLocation();
        //


    }

    private void SetDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(VerMisRutasODMActivitye.this);

        builder.setTitle("Compartir esta ruta");
        builder.setMessage("Está apunto de compartir esta ruta, todos podrán verla ☺.");
        builder.setPositiveButton("De acuerdo.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveSharedRoute route = new SaveSharedRoute();
                Create_SaveShared shared = new Create_SaveShared(route);
                shared.sharedRoute.IsaveShaerRoute(dirIntent_2.get("URL")+"",VerMisRutasODMActivitye.this);
                finish();
            }
        });
        builder.setNegativeButton("No.", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void puTheMakers() {
        final DatabaseReference ref = databaseReference.child("markers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    double llat = Double.parseDouble(postSnapshot.child("lat").getValue().toString());
                    double llng = Double.parseDouble(postSnapshot.child("lng").getValue().toString());

                    float bit;

                    switch (postSnapshot.child("color").getValue().toString()) {
                        case "HUE_GREEN":
                            bit = BitmapDescriptorFactory.HUE_GREEN;
                            mMap.addMarker(new MarkerOptions().position(new LatLng(llat, llng)).title(postSnapshot.child("nombre").getValue().toString())
                                    .icon(BitmapDescriptorFactory.defaultMarker(bit)));
                            break;
                        case "HUE_RED":
                            bit = BitmapDescriptorFactory.HUE_RED;
                            mMap.addMarker(new MarkerOptions().position(new LatLng(llat, llng)).title(postSnapshot.child("nombre").getValue().toString())
                                    .icon(BitmapDescriptorFactory.defaultMarker(bit)));
                            break;
                        case "HUE_ORANGE":
                            bit = BitmapDescriptorFactory.HUE_ORANGE;
                            mMap.addMarker(new MarkerOptions().position(new LatLng(llat, llng)).title(postSnapshot.child("nombre").getValue().toString())
                                    .icon(BitmapDescriptorFactory.defaultMarker(bit)));
                            break;
                        case "HUE_BLUE":
                            bit = BitmapDescriptorFactory.HUE_BLUE;
                            mMap.addMarker(new MarkerOptions().position(new LatLng(llat, llng)).title(postSnapshot.child("nombre").getValue().toString())
                                    .icon(BitmapDescriptorFactory.defaultMarker(bit)));
                            break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void getMyRutaPolygon() {

        final DatabaseReference ref = databaseReference;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int a = 0;
                int count = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    a++;
                }

                for (int i = 1; i <= (a - 5); i++) {
                    String strLat = dataSnapshot.child(i + "").child("latitud:").getValue() + "";
                    String strLog = dataSnapshot.child(i + "").child("Longitud:").getValue() + "";
                    double Dlat = Double.parseDouble(strLat);
                    double Dlng = Double.parseDouble(strLog);
                    LatLng matlong = new LatLng(Dlat, Dlng);
                    latLngs.add(count, matlong);
                    count++;
                    if (i == 1) {
                        mMap.addMarker(new MarkerOptions().position(matlong).title("Inicio").icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)));
                    }
                    /*latitud = (double) dataSnapshot.child(i+"").child("latitud:").getValue();
                    longitud = (double) dataSnapshot.child(i+"").child("Longitud:").getValue();
                    //Toast.makeText(VerMisRutasohYeahActivity.this,dataSnapshot.child(i+"").child("Longitud:").getValue()+"",Toast.LENGTH_LONG).show();

                    latLngs.add(count, matlong);

                    ;*/
                    //Toast.makeText(VerMisRutasOjosDeMedusaActivitye.this,dataSnapshot.child(i+"").child("Longitud:").getValue()+"",Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(VerMisRutasOjosDeMedusaActivitye.this,latLngs+"",Toast.LENGTH_LONG).show();
                if(latLngs.size() >0) {
                    mMap.addMarker(new MarkerOptions().position(latLngs.get(a - 6)).title("Final").icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow2)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(a - 6), 17));
                    verRutas();
                }else{
                    Toast.makeText(VerMisRutasODMActivitye.this,"esta ruta esta vacia...",Toast.LENGTH_LONG).show();
                    buttonShare.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override

                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_home:
                                menuItem.setChecked(true);
                                //______________________________________________
                                finish();
                                startActivity(new Intent(VerMisRutasODMActivitye.this,ProfileActivity.class));
                                //______________________________________________

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_profile:
                                menuItem.setChecked(true);
                                //______________________________________________

                                startActivity(new Intent(VerMisRutasODMActivitye.this,EditProfileActivity.class));
                                //______________________________________________

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_grabarruta:
                                menuItem.setChecked(true);

                                //______________________________________________________________________________
                                Intent intent = new Intent(VerMisRutasODMActivitye.this, LocationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                //___________________________________________________________________________________

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_misrutas:
                                menuItem.setChecked(true);
                                //______________________________________________
                                startActivity(new Intent(VerMisRutasODMActivitye.this,MisRutasUsuarioActivity.class));
                                //______________________________________________
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                            case R.id.item_navigation_drawer_settings:
                                if (dirIntent_2.getInt("SHARE_BUTT") == 1) {
                                    Toast.makeText(VerMisRutasODMActivitye.this, "ruta de un usuario", Toast.LENGTH_SHORT).show();
                                } else {
                                    Create_Dialog_help help = new Create_Dialog_help();
                                    Buid_dialog_all buidDialogAll = new Buid_dialog_all(help);
                                    buidDialogAll.i_create_dialog_help.dialog(VerMisRutasODMActivitye.this,getString(R.string.la_ruta));
                                }
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_help_and_feedback:
                                Create_Dialog_help help = new Create_Dialog_help();
                                Buid_dialog_all buidDialogAll = new Buid_dialog_all(help);
                                buidDialogAll.i_create_dialog_help.dialog(VerMisRutasODMActivitye.this,getString(R.string.contact));
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });
    }

    private void verRutas() {

        polyline = mMap.addPolyline(new PolylineOptions().addAll(latLngs)
                .width(10).color(Color.GREEN));
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
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_for_my, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_mapa) {
            //return true;
            //startActivity(new Intent(MainActivity.this,MejorRutaActivity.class));
            //Toast.makeText(VerMisRutasODMActivitye.this,mMap.getMapType()+"",Toast.LENGTH_LONG).show();
            if (mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID)
            {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }else {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }else {
                    if(mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE){
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    }else {
                        if(mMap.getMapType() == GoogleMap.MAP_TYPE_TERRAIN){
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        }
                    }
                }
            }

        } else {
            if (id == R.id.action_help) {

                if (dirIntent_2.getInt("SHARE_BUTT") == 1) {
                    Toast.makeText(VerMisRutasODMActivitye.this, "ruta de un usuario", Toast.LENGTH_SHORT).show();
                } else {
                    Create_Dialog_help help = new Create_Dialog_help();
                    Buid_dialog_all buidDialogAll = new Buid_dialog_all(help);
                    buidDialogAll.i_create_dialog_help.dialog(VerMisRutasODMActivitye.this,getString(R.string.la_ruta));
                }
                return true;
                //startActivity(new Intent(MainActivity.this,MejorRutaActivity.class));
                //finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
