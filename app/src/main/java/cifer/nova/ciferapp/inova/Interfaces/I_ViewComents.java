package cifer.nova.ciferapp.inova.Interfaces;

import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Cifer on 05/07/2017.
 */

public interface I_ViewComents {


    void GetComendtOfDataBase(FirebaseRecyclerAdapter firebaseRecyclerAdapter,DatabaseReference reference, RecyclerView recyclerView);
}
