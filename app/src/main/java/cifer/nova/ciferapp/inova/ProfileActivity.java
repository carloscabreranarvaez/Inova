package cifer.nova.ciferapp.inova;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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
import android.widget.Toast;

import cifer.nova.ciferapp.inova.Adaptadores.JakuAdapter;
import cifer.nova.ciferapp.inova.Adaptadores.OpcionesAdapter;
import cifer.nova.ciferapp.inova.Clases.Buid_dialog_all;
import cifer.nova.ciferapp.inova.Clases.Create_Dialog_help;
import cifer.nova.ciferapp.inova.Clases.JakuClass;
import cifer.nova.ciferapp.inova.Clases.OpcionesCardView;

import cifer.nova.ciferapp.inova.R;

import cifer.nova.ciferapp.inova.Usuario.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private TextView textViewUser;
    private FloatingActionButton floatingActionButton;

    private DatabaseReference databaseReference;

    private TextView textViewProfile;
    private double latitud =0.0;
    private double longitud = 0.0;
    private Button buttonDatos;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager IManagement;

    private ImageView imageView;

    private FloatingActionButton faboption;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,RegistarseMainThisIsActivity.class));
        }else {
            drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
            if (navigationView != null) {
                setupNavigationDrawerContent(navigationView);

                //final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                View avHeaderView = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);
                final TextView tvHeaderName = (TextView) avHeaderView.findViewById(R.id.header_name);
                final ImageView foto = (ImageView) avHeaderView.findViewById(R.id.header_foto);
                TextView iduser = (TextView) avHeaderView.findViewById(R.id.header_id);
                DatabaseReference refname = databaseReference.child("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("nombre");
                refname.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null){
                            tvHeaderName.setText(firebaseAuth.getCurrentUser().getDisplayName());
                            Toast.makeText(ProfileActivity.this, "Defaul name".toString(), Toast.LENGTH_SHORT).show();
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
                        if (dataSnapshot == null) {
                            Toast.makeText(ProfileActivity.this, "Defaul foto".toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Picasso.with(ProfileActivity.this).load(dataSnapshot.getValue() + "").into(foto);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                iduser.setText(firebaseAuth.getCurrentUser().getEmail() + "");

                //getUserName();
            }
        }




        /*editTextNombre = (EditText) findViewById(R.id.NombreTextoProfile);
        editTextApellido = (EditText) findViewById(R.id.ApellidoTextoProfile);*/
        /*imageView = (ImageView) findViewById(R.id.EditProfilePen);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
            }
        });*/
        //textViewProfile = (TextView) findViewById(R.id.NombreTextoView);

        /*floatingActionButton = (FloatingActionButton) findViewById(R.id.fabProfile);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,LocationActivity.class));
            }
        }); */

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //textViewUser = (TextView) findViewById(R.id.TextViewProfile);
        //textViewUser.setText("Hola: "+firebaseUser.getEmail());
        if (firebaseUser != null)
        {
            /*String name = firebaseUser.getDisplayName();
            String lastname = firebaseUser.getDisplayName();
            Usuario usurio = new Usuario(lastname,name,0.0,0.0,longitud,latitud);
            databaseReference.child(firebaseUser.getUid()).setValue(usurio);*/
            Toast.makeText(ProfileActivity.this,"hola ☺",Toast.LENGTH_LONG).show();
        }
        faboption = (FloatingActionButton) findViewById(R.id.FabOpciones);

        //getUserName();
        starReciclerViewOpciones();
        VerOpciones();
        GetEncuestaUser();

    }

    private void GetEncuestaUser() {
        DatabaseReference reffoto = databaseReference.child("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("encuestado");
        reffoto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(ProfileActivity.this,dataSnapshot.getValue()+".",Toast.LENGTH_LONG).show();
                if(dataSnapshot.getValue() == null){
                    CrearEncuesta();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void CrearEncuesta() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

        builder.setTitle("Encuesta!!! ");
        builder.setMessage("Ayúdanos respondiendo esta encuesta, nos ayudara a mejorar.☼");
        builder.setPositiveButton("Está bien .", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = "https://goo.gl/forms/025qF9qA7mx7WDXN2";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                DatabaseReference reffoto = databaseReference.child("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("encuestado");
                reffoto.setValue("gracias!");
            }
        });
        builder.setNegativeButton("No ahora.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.show();
    }

    private void starReciclerViewOpciones() {

        int color1 = Color.parseColor("#80DEEA");
        int color2 = Color.parseColor("#4DD0E1");
        int color3 = Color.parseColor("#26C6DA");
        int color4 = Color.parseColor("#00BCD4");
        int color5 = Color.parseColor("#00ACC1");


        final List<OpcionesCardView> opcionesCardViews = new ArrayList<>();
        opcionesCardViews.add(new OpcionesCardView(color1,faboption,R.mipmap.marker,"Grabar Ruta"));
        opcionesCardViews.add(new OpcionesCardView(color2,faboption,R.mipmap.mymaps,"Mis Rutas"));
        opcionesCardViews.add(new OpcionesCardView(color3,faboption,R.drawable.world_map,"Rutas"));
        opcionesCardViews.add(new OpcionesCardView(color4,faboption,R.drawable.edit_profile,"Editar Perfil"));
        opcionesCardViews.add(new OpcionesCardView(color4,faboption,R.mipmap.dollar,"Cambio de moneda"));
        opcionesCardViews.add(new OpcionesCardView(color5,faboption,R.mipmap.icons8_lout,"Cerrar Sesión"));


        recycler = (RecyclerView) findViewById(R.id.RwProfile);
        IManagement = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(IManagement);
        adapter = new OpcionesAdapter(this,opcionesCardViews);
        //adapter.setClickListenerA(this);
        recycler.setAdapter(adapter);
    }

    private void VerOpciones() {

        String url1 ="https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fotuzco-min.jpg?alt=media&token=3d8d8530-a44f-43c7-ab4d-001259cc6517";
        String url_2 ="https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fcumbe-min.jpg?alt=media&token=1f3fff60-e04a-4354-891e-1b5578a032c5";
        String url3 ="https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fbanios-min.jpg?alt=media&token=e3d46b11-3aa8-4cbe-9bb2-74c943175d4b";
        String url4 ="https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fsantapa.jpg?alt=media&token=d385186b-aa8c-408d-ae76-914ffb868b5d";
        String url5 ="https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fbelen-min.jpg?alt=media&token=8b6897f2-f4e5-4c18-8e2a-8dc66e5565ae";
        String url6 = "https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fcajamarca_l.jpg?alt=media&token=abd777ae-b8a6-4630-bbbb-dcd1b02c2991";
        String url7 = "https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fcelendin-min.jpg?alt=media&token=3995f500-5ce0-428e-876f-86c93bebc995";
        String url8= "https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fpo-min.jpg?alt=media&token=ed42f279-8710-45e1-8cbd-a47e8bb49b09";


        final List<JakuClass> mClasses = new ArrayList<>();
        mClasses.add(new JakuClass(R.mipmap.image_4,"Mas en cajamarca",url6));
        mClasses.add(new JakuClass(R.mipmap.image_4,"Granja Porcón",url8));
        mClasses.add(new JakuClass(R.mipmap.image_4,"Otuzco",url1));
        mClasses.add(new JakuClass(R.mipmap.image_4,"Cumbe Mayo",url_2));
        mClasses.add(new JakuClass(R.mipmap.image_4,"Baños de Inca",url3));
        mClasses.add(new JakuClass(R.mipmap.image_4,"Santa Apolonia",url4));
        mClasses.add(new JakuClass(R.mipmap.image_4,"Complejo Belén",url5));
        mClasses.add(new JakuClass(R.mipmap.image_4,"Celendin",url7));

        recycler = (RecyclerView) findViewById(R.id.JBlogRV);
        IManagement = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(IManagement);
        adapter = new JakuAdapter(this,mClasses);
        //adapter.setClickListenerA(this);
        recycler.setAdapter(adapter);
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
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_salir) {
            //return true;
            //startActivity(new Intent(MainActivity.this,MejorRutaActivity.class));
            finish();
        } else {
            if (id == R.id.action_settings) {
                Create_Dialog_help help = new Create_Dialog_help();
                Buid_dialog_all buidDialogAll = new Buid_dialog_all(help);
                buidDialogAll.i_create_dialog_help.dialog(ProfileActivity.this,getString(R.string.profile));


                //startActivity(new Intent(MainActivity.this,MejorRutaActivity.class));
                //finish();
            }
        }
        return super.onOptionsItemSelected(item);
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
                                startActivity(new Intent(ProfileActivity.this,ProfileActivity.class));
                                //______________________________________________

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_profile:
                                menuItem.setChecked(true);
                                //______________________________________________

                                startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
                                //______________________________________________

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_grabarruta:
                                menuItem.setChecked(true);

                                //______________________________________________________________________________
                                Intent intent = new Intent(ProfileActivity.this, LocationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                //___________________________________________________________________________________

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_misrutas:
                                menuItem.setChecked(true);
                                //______________________________________________
                                startActivity(new Intent(ProfileActivity.this,MisRutasUsuarioActivity.class));
                                //______________________________________________
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                            case R.id.item_navigation_drawer_settings:
                                Create_Dialog_help help = new Create_Dialog_help();
                                Buid_dialog_all buidDialogAll = new Buid_dialog_all(help);
                                buidDialogAll.i_create_dialog_help.dialog(ProfileActivity.this,getString(R.string.profile));
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_help_and_feedback:
                                Create_Dialog_help helpp = new Create_Dialog_help();
                                Buid_dialog_all buidDialogAlll = new Buid_dialog_all(helpp);
                                buidDialogAlll.i_create_dialog_help.dialog(ProfileActivity.this,getString(R.string.contact));
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                }
        );
    }
}
