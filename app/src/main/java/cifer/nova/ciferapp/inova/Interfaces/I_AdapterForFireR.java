package cifer.nova.ciferapp.inova.Interfaces;

import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import cifer.nova.ciferapp.inova.Clases.MisRutasClass;
import cifer.nova.ciferapp.inova.MisRutasUsuarioActivity;

/**
 * Created by Cifer on 01/07/2017.
 */

public interface I_AdapterForFireR {

    void SetUpAdapter(FirebaseRecyclerAdapter firebaseRecyclerAdapter,DatabaseReference mDatabaseReference,RecyclerView mRecyclerView);

}
