package cifer.nova.ciferapp.inova;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cifer.nova.ciferapp.inova.Clases.Blogclass;

import cifer.nova.ciferapp.inova.Clases.Buid_dialog_all;
import cifer.nova.ciferapp.inova.Clases.Create_Dialog_help;
import cifer.nova.ciferapp.inova.R;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class BlogMainActivity extends  AppCompatActivity {

    private RecyclerView mRecyclerView ;
    private LinearLayoutManager mLayoutManager;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;

    private static FirebaseRecyclerAdapter<Blogclass,BlogViewHolder> firebaseRecyclerAdapter;
    private static DatabaseReference mDatabaseReference;
    private DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;
    private Intent intento;

    private ProgressDialog progressDialog;

   private Button nButtonActualizar;

    ImageView post_img;
    private Bundle dirIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.keyboard_backspace);
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout_2);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_2);
        /*if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);

            View avHeaderView = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);
            final TextView tvHeaderName = (TextView) avHeaderView.findViewById(R.id.header_name);
            final ImageView foto = (ImageView) avHeaderView.findViewById(R.id.header_foto);

            TextView iduser = (TextView) avHeaderView.findViewById(R.id.header_id);
            iduser.setText(firebaseAuth.getCurrentUser().getEmail() + "");
            final DatabaseReference refname = databaseReference.child("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("nombre");
            refname.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (refname == null){
                        tvHeaderName.setText(firebaseAuth.getCurrentUser().getDisplayName());
                        Toast.makeText(BlogMainActivity.this, "Defaul name".toString(), Toast.LENGTH_SHORT).show();
                    }else {
                        tvHeaderName.setText(dataSnapshot.getValue() + "");
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            DatabaseReference reffoto = databaseReference.child("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("foto");
            reffoto.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (refname == null) {
                        Toast.makeText(BlogMainActivity.this, "Defaul foto".toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Picasso.with(BlogMainActivity.this).load(dataSnapshot.getValue() + "").into(foto);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }*/
        FloatButtonFuncion();


        nButtonActualizar = (Button) findViewById(R.id.buttonActualizar);

        intento = getIntent();
        dirIntent = intento.getExtras();
        switch (dirIntent.getInt("id")) {
            case 0:
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("cajamarca");
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Cargando...☺");
                progressDialog.show();
                break;
            case 1:
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("porcon");
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Cargando...◘");
                progressDialog.show();
                break;
            case 2:
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("Otusco");
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Cargando...☻");
                progressDialog.show();
                break;
            case 3:
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("Cumbe");
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Cargando...♥");
                progressDialog.show();
                break;
            case 4:
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("Banios");
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Cargando...♦");
                progressDialog.show();
                break;
            case 5:
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("santa");
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Cargando...♠");
                progressDialog.show();
                break;
            case 6:
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("belen");
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Cargando...♣");
                progressDialog.show();
                break;
            case 7:
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("Celendin");
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Cargando...◙");
                progressDialog.show();
                break;
        }


        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //CastBlogInfo();


    }

   /* private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_home:
                                menuItem.setChecked(true);
                                //______________________________________________
                                finish();
                                startActivity(new Intent(BlogMainActivity.this,ProfileActivity.class));
                                //______________________________________________

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_profile:
                                menuItem.setChecked(true);

                                //______________________________________________
                                finish();
                                startActivity(new Intent(BlogMainActivity.this,EditProfileActivity.class));
                                //______________________________________________


                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_grabarruta:
                                menuItem.setChecked(true);

                                //______________________________________________________________________________
                                Intent intent = new Intent(BlogMainActivity.this, LocationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                //___________________________________________________________________________________

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_misrutas:
                                menuItem.setChecked(true);
                                //______________________________________________
                                finish();
                                startActivity(new Intent(BlogMainActivity.this,MisRutasUsuarioActivity.class));
                                //______________________________________________

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                            case R.id.item_navigation_drawer_settings:
                                menuItem.setChecked(true);

                                Toast.makeText(BlogMainActivity.this, " " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_help_and_feedback:
                                menuItem.setChecked(true);
                                Toast.makeText(BlogMainActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });
    }

    private void ActualizarPagina() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Cargando...");
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        Thread thread = null;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (progress<= 100 ){
                    try {
                        progressDialog.setProgress(progress);
                        progress++;
                        Thread.sleep(100);

                    }catch (Exception ex){
                    }
                }
                progressDialog.dismiss();
                finish();
                startActivity(intento);
            }
        });
        thread.start();
        progressDialog.show();

    }*/

    /*private void CastBlogInfo() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        FirebaseRecyclerAdapter<Blogclass,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blogclass, BlogViewHolder>(
                Blogclass.class,
                R.layout.blog_item_rv,
                BlogViewHolder.class,
                mDatabaseReference

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blogclass model, int position) {

                viewHolder.SetTitulo(model.getTitulo());
                viewHolder.SetDesc(model.getDescripcion());
                viewHolder.SetAuth(model.getUsuarioId());
                viewHolder.SetImage(getApplicationContext(),model.getImagen());

            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        InitScreen();
    }

    private void InitScreen()
    {
        mLayoutManager = new LinearLayoutManager(BlogMainActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView =(RecyclerView) findViewById(R.id.BlogIdRV);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        setupAdapter();
}

    private void setupAdapter() {

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blogclass, BlogViewHolder>(
                Blogclass.class,
                R.layout.blog_item_rv,
                BlogViewHolder.class,
                mDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blogclass model, final int position) {
                viewHolder.SetTitulo(model.getTitulo());
                viewHolder.SetDesc(model.getDescripcion());
                viewHolder.SetAuth(model.getUsuarioId());
                viewHolder.SetImage(getApplicationContext(),model.getImagen(),position);
                viewHolder.setMyurl(model.getMy_url());
                viewHolder.DropPost(getApplicationContext(),position,model.getUsuarioId());
                progressDialog.dismiss();
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void SetTitulo(String title)
        {
                TextView post_titulo = (TextView) mView.findViewById(R.id.titulo_blog_id);
                post_titulo.setText(title);
        }
        public void SetDesc(String desc)
        {
            TextView post_desc = (TextView) mView.findViewById(R.id.descripcion_blog_id);
            post_desc.setText(desc);
        }
        public void SetAuth(String desc)
        {
            //final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            TextView post_auth = (TextView) mView.findViewById(R.id.usuario_blog_id);
            post_auth.setText(desc);

        }
        public void setMyurl (String urls)
        {
            TextView urlTextView = (TextView) mView.findViewById(R.id.my_url);
            urlTextView.setText(urls);
        }
        public void SetImage(final Context ctx, String imagen, final int pocition){
            ImageView post_img = (ImageView) mView.findViewById(R.id.image_blog);
            Picasso.with(ctx).load(imagen).into(post_img);
            post_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx,SitiosDescripcionActivity.class);
                    intent.putExtra("IMAGEN",firebaseRecyclerAdapter.getItem(pocition).getImagen());
                    intent.putExtra("TITULO",firebaseRecyclerAdapter.getItem(pocition).getTitulo());
                    intent.putExtra("DESCRIPCION",firebaseRecyclerAdapter.getItem(pocition).getDescripcion());
                    intent.putExtra("MY_URL",firebaseRecyclerAdapter.getItem(pocition).getMy_url());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                }
            });

         }

        public void DropPost(Context ctx,final int post, String srt)
        {
            String reg = "subido por: "+firebaseAuth.getCurrentUser().getDisplayName();
            ImageView im = (ImageView) mView.findViewById(R.id.image_eliminar_post);
            //Toast.makeText(ctx,srt,Toast.LENGTH_LONG).show();
            if(srt.equals(reg))
            {
                im.setVisibility(View.VISIBLE);
            }
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
                            String value = firebaseRecyclerAdapter.getItem(post).getMy_url();
                            //String value_gen = firebaseRecyclerAdapter.getItem(post).getGeneral_url();
                            FirebaseDatabase.getInstance().getReferenceFromUrl(value).removeValue();
                            //FirebaseDatabase.getInstance().getReferenceFromUrl(value_gen).removeValue();

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
    void FloatButtonFuncion(){
        intento = getIntent();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BlogMainActivity.this,ListaTuristicosActivity.class);
                i.putExtra("idd",dirIntent.getInt("id"));
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                //drawerLayout.openDrawer(GravityCompat.START);
                finish();
                return true;
        }
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_salir) {
            //return true;
            //startActivity(new Intent(MainActivity.this,MejorRutaActivity.class));
            finish();
        }else {
            if (id == R.id.action_settings) {
                Create_Dialog_help help = new Create_Dialog_help();
                Buid_dialog_all buidDialogAll = new Buid_dialog_all(help);
                buidDialogAll.i_create_dialog_help.dialog(BlogMainActivity.this,getString(R.string.los_lugares));
                return true;
                //startActivity(new Intent(MainActivity.this,MejorRutaActivity.class));
                //finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
