package cifer.nova.ciferapp.inova;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cifer.nova.ciferapp.inova.Clases.Buid_dialog_all;
import cifer.nova.ciferapp.inova.Clases.Create_Dialog_help;
import cifer.nova.ciferapp.inova.Clases.MisRutasClass;

import cifer.nova.ciferapp.inova.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MisRutasUsuarioActivity extends AppCompatActivity {

    public View v;
    private FirebaseUser firebaseAuth;
    private DatabaseReference databaseReference;
    private static DatabaseReference mDatabaseReference;
    private RecyclerView mRecyclerView ;
    private LinearLayoutManager mLayoutManager;
    private static FirebaseRecyclerAdapter<MisRutasClass,LatLogViewHolder> firebaseRecyclerAdapter;
    private AlertDialog alert = null;
    //dialog
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_rutas_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_8);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Mis_rutas").child(firebaseAuth.getUid());
        progressDialog = new ProgressDialog(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        InitScreen();
    }

    private void InitScreen()
    {
        mLayoutManager = new LinearLayoutManager(MisRutasUsuarioActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView =(RecyclerView) findViewById(R.id.my_ruta_rv);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        setupAdapter();
    }

    private void setupAdapter() {

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MisRutasClass, LatLogViewHolder>(
                MisRutasClass.class,
                R.layout.my_ruta_rv,
                LatLogViewHolder.class,
                mDatabaseReference
        ){
            @Override
            protected void populateViewHolder(LatLogViewHolder viewHolder, MisRutasClass model, final int position) {
                progressDialog.setMessage("Cargando...");
                progressDialog.show();
                viewHolder.getFecha(model.getFecha());
                viewHolder.SetTitulo(getApplicationContext(),model.getTitulo());
                viewHolder.setRutaMyurl(model.getOur_url());
                viewHolder.setRutaGoblalUrl(model.getGeneral_url());
                viewHolder.getOneRute(getApplicationContext(),"",position);
                viewHolder.delete(MisRutasUsuarioActivity.this,position);
                progressDialog.dismiss();
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class LatLogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public LatLogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
            public void SetTitulo(final Context ctx,String title)
            {
                TextView post_titulo = (TextView) mView.findViewById(R.id.tituloformyruta);
                post_titulo.setText(title);

            }
            public void getFecha(String s)
            {
                TextView post_titulo = (TextView) mView.findViewById(R.id.textmyruta);
                post_titulo.setText(s);

            }
        public void setRutaMyurl (String urls)
        {
            TextView urlTextView = (TextView) mView.findViewById(R.id.Urlrmyruta);
            urlTextView.setText(urls);
        }
        public void setRutaGoblalUrl (String url){
            TextView mTextView = (TextView) mView.findViewById(R.id.UrlGlogrmyruta);
            mTextView.setText(url);
        }

        public void getOneRute(final Context ctx, String imagen, final int pocition) {
            ImageView cono = (ImageView) mView.findViewById(R.id.myRutaButton);
            cono.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, VerMisRutasODMActivitye.class);
                    intent.putExtra("URL", firebaseRecyclerAdapter.getItem(pocition).getOur_url());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                }
            });
        }

        public void delete(final AppCompatActivity ctx,final int post) {
            ImageView im = (ImageView) mView.findViewById(R.id.DropmyRutaButton);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //AlertContinueGrabarRuta(post);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(mView.getContext());

                    builder.setTitle("Borrar Ruta");
                    builder.setMessage("Esta ruta se borrara permanentemente, desea continuar?");
                    builder.setPositiveButton("Continuar.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String value = firebaseRecyclerAdapter.getItem(post).getOur_url();
                            String value_gen = firebaseRecyclerAdapter.getItem(post).getGeneral_url();
                            FirebaseDatabase.getInstance().getReferenceFromUrl(value).removeValue();
                            FirebaseDatabase.getInstance().getReferenceFromUrl(value_gen).removeValue();

                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Create_Dialog_help help = new Create_Dialog_help();
            Buid_dialog_all buidDialogAll = new Buid_dialog_all(help);
            buidDialogAll.i_create_dialog_help.dialog(MisRutasUsuarioActivity.this,getString(R.string.mis_rutas));
            return true;
        } else
            if(item.getItemId() == R.id.action_salir) {
            finish();
            return true;
        }
            return super.onOptionsItemSelected(item);
    }

}
