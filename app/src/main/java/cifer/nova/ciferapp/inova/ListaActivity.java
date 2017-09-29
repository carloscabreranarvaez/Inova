package cifer.nova.ciferapp.inova;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cifer.nova.ciferapp.inova.R;

import cifer.nova.ciferapp.inova.Usuario.Usuario;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    //private FirebaseDatabase database;
    private ListView listView;
    private TextView textView;
    private ArrayList<String> Index;
    private FloatingActionButton floatingActionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionButton=(FloatingActionButton) findViewById(R.id.fabLista);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(ListaActivity.this, LoginActivity.class);
                //i.putExtra("id",0);
                startActivity(i);
            }
        });

        Index= new ArrayList<>();

        listView = (ListView) findViewById(R.id.ListViewLista);

        textView = (TextView) findViewById(R.id.TextoLista);



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Usuario/0");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               /* String mensaje = dataSnapshot.getValue(String.class);
                Log.v("Name: ",mensaje);*/
                Usuario user = dataSnapshot.getValue(Usuario.class);
                Index.add(user.getNombre());
                textView.setText(user.getNombre());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                Index.add(user.getNombre());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.content_lista, Index);
        listView.setAdapter(arrayAdapter);


        /*listView = (ListView) findViewById(R.id.ListViewLista);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.content_lista, Index);
        listView.setAdapter(arrayAdapter);*/
        //DatabaseReference ref= database.getReference();

        /*ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mensaje = dataSnapshot.getValue(String.class);
                Log.v("Name: ",mensaje);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

}
   /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
fab.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();
        }
        });*/