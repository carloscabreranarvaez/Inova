package cifer.nova.ciferapp.inova.Clases;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cifer.nova.ciferapp.inova.Interfaces.I_SaveSharedRoute;
import cifer.nova.ciferapp.inova.VerMisRutasODMActivitye;

/**
 * Created by Cifer on 01/07/2017.
 */

public class SaveSharedRoute implements I_SaveSharedRoute {
    private DatabaseReference get_Database;
    private DatabaseReference shared_base;
    private String URL;
    private DatabaseReference myUser;
    private String UserName;
    private FirebaseUser firebaseAuth;
    private Context cc;
    @Override
    public void IsaveShaerRoute(String url,Context c) {

        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        getUserName(myUser,firebaseAuth);

        cc = c;

        get_Database = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
        shared_base = FirebaseDatabase.getInstance().getReference().child("all_rutas").push();

        //Toast.makeText(c, url, Toast.LENGTH_SHORT).show();

        URL = shared_base+"";
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl(URL);
        get_Database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int a = 0;
                int count = 1;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    a++;
                }
                for (int i = 1; i <= (a - 5); i++) {

                    reference.child(count+"").child("Longitud:").setValue(dataSnapshot.child(count+"").child("Longitud:").getValue());
                    reference.child(count+"").child("latitud:").setValue(dataSnapshot.child(count+"").child("latitud:").getValue());
                    count++;
                }
                reference.child("usuario").setValue(UserName);
                reference.child("fecha").setValue(dataSnapshot.child("fecha").getValue());
                reference.child("titulo").setValue(dataSnapshot.child("titulo").getValue());
                reference.child("our_url").setValue(URL);
                get_Database.child("general_url").setValue(URL);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        addMakers(get_Database,reference.child("markers"));

    }

    private void addMakers(DatabaseReference get_Database, final DatabaseReference reference) {
        final int[] a = {0};
        get_Database.child("markers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DatabaseReference r = reference.push();
                    r.child("color").setValue(postSnapshot.child("color").getValue());
                    r.child("lat").setValue(postSnapshot.child("lat").getValue());
                    r.child("lng").setValue(postSnapshot.child("lng").getValue());
                    r.child("nombre").setValue(postSnapshot.child("nombre").getValue());
                    //Toast.makeText(cc, a[0] +"", Toast.LENGTH_SHORT).show();
                    a[0]++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void getUserName(DatabaseReference myUser, FirebaseUser firebaseAuth) {

        myUser = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(firebaseAuth.getUid().toString()).child("nombre");
        myUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
