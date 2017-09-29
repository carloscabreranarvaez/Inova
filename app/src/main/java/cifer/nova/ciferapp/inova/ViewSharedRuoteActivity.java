package cifer.nova.ciferapp.inova;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cifer.nova.ciferapp.inova.Clases.AdapterForFireR;
import cifer.nova.ciferapp.inova.Clases.CreateAdapterForFireR;
import cifer.nova.ciferapp.inova.Clases.SharedClass;

public class ViewSharedRuoteActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView ;
    private LinearLayoutManager mLayoutManager;
    private static DatabaseReference mDatabaseReference;
    private FirebaseUser firebaseAuth;

    private FirebaseRecyclerAdapter<SharedClass, AdapterForFireR.SharedViewHolder> recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shared_ruote);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("all_rutas");
        initScreen();

    }

    private void initScreen() {
            mLayoutManager = new LinearLayoutManager(ViewSharedRuoteActivity.this);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);

            mRecyclerView =(RecyclerView) findViewById(R.id.shared_rv);
            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setLayoutManager(mLayoutManager);

        AdapterForFireR adapterForFireR = new AdapterForFireR();
        CreateAdapterForFireR fireR = new CreateAdapterForFireR(adapterForFireR);
        fireR.forFireR.SetUpAdapter(recyclerAdapter,mDatabaseReference,mRecyclerView);
    }

}
