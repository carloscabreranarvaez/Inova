package cifer.nova.ciferapp.inova.Interfaces;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DatabaseReference;


public interface I_ShowDialogForAddMarker {

    void showInterface(final Context context,final DatabaseReference database,final DatabaseReference database_2);
    void doAddMarkers(final GoogleMap googleMap,double lat,double log);


}
