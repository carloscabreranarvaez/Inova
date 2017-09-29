package cifer.nova.ciferapp.inova;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import cifer.nova.ciferapp.inova.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener,LocationSource.OnLocationChangedListener{

    private GoogleMap mMap;
    private FirebaseAuth firebaseAuth;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    private LocationManager locationManager1;



    private Marker marker;
    public double latitud;
    public double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast toast = Toast.makeText(MapsActivity.this, "ERROR DE ENTRADA", Toast.LENGTH_LONG);
            toast.show();
            return;

        }
        mMap.setMyLocationEnabled(true);*/

        locationManager1 = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager1.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, (LocationListener) this);

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

        // Add a marker in Sydney and move the camera

        /*LatLng sydney = new LatLng(-7.159122917264517, -78.51669140160084);
        LatLng SantaApolonia = new LatLng(-7.160594612770227, -78.51921200752258);
        LatLng PlazaDeArmas = new LatLng(-7.156989881806371, -78.51737804710865);
        LatLng CuartoRescate = new LatLng(-7.157906702386268, -78.51650565862656);
        LatLng CatedralP = new LatLng(-7.1567037908274065, -78.51806670427322);
        LatLng SanFrancisco = new LatLng(-7.157472909424235, -78.51639971137047);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Ingresar a la DDC"));
        mMap.addMarker(new MarkerOptions().position(SantaApolonia).title("Santa Apolonia"));
        mMap.addMarker(new MarkerOptions().position(PlazaDeArmas).title("Plaza De Armas"));
        mMap.addMarker(new MarkerOptions().position(CuartoRescate).title("Cuarto del Rescate"));
        mMap.addMarker(new MarkerOptions().position(CatedralP).title("Iglesia Catedral"));
        mMap.addMarker(new MarkerOptions().position(SanFrancisco).title("Iglesia San Francisco"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
        setUpMap();*/
        myUbication();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        mMap.setMyLocationEnabled(true);

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "se requiere activar GPS", Toast.LENGTH_LONG).show();
                    //finish();
                }
                break;
        }

    }

    private void agregarMarcador(double latitud,double longitud)

    {
        LatLng coordenada = new LatLng(latitud,longitud);
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLngZoom(coordenada,14);
        if(marker != null){
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(coordenada).title("godm"));
    }
    private void actualizarUbicacion(Location location){
        if(location != null){
            latitud = location.getLatitude();
            longitud = location.getLongitude();
            agregarMarcador(latitud,longitud);
        }
    }
    public LocationListener lolistener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
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
        LocationManager locationListener = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationListener.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationListener.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0, lolistener);
    }


    private void setUpMap() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitud=location.getLatitude();
        longitud=location.getLongitude();
        String msg = "Latitud: " + latitud
                + "Longitud: " + longitud;

        Toast.makeText(getBaseContext(), msg , Toast.LENGTH_LONG).show();
    }

}
