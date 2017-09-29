package cifer.nova.ciferapp.inova;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import cifer.nova.ciferapp.inova.Clases.AddComent;
import cifer.nova.ciferapp.inova.Clases.Comentario;
import cifer.nova.ciferapp.inova.Clases.Create_AddComent;
import cifer.nova.ciferapp.inova.Clases.Create_viewComents;
import cifer.nova.ciferapp.inova.Clases.ViewComents;
import cifer.nova.ciferapp.inova.R;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SitiosDescripcionActivity extends AppCompatActivity {


    private ImageView imageView;
    private TextView textViewTitulo;
    private TextView textViewDescrip;
    private TextView textViewRute;
    private RatingBar mRatingBar;
    private float rate;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private RecyclerView CrecyclerView;
    private LinearLayoutManager mLayoutManager;

    private DrawerLayout drawerLayout;
    private ActionBar actionBar;

    private FloatingActionButton rutafabButton;
    FirebaseRecyclerAdapter<Comentario, ViewComents.ViewComentViewHolder> firebaseRecyclerAdapter;

    private CardView cardStars;
    private CardView cardRating;
    //number of strart
    private TextView s_one;
    private TextView s_two;
    private TextView s_tree;
    private TextView s_four;
    private TextView s_five;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios_descripcion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intento = getIntent();
        final Bundle dirIntent = intento.getExtras();

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //.---------Numero de estrellas -------------------
        s_one = (TextView) findViewById(R.id.rating_number_1);
        s_two = (TextView) findViewById(R.id.rating_number_2);
        s_tree = (TextView) findViewById(R.id.rating_number_3);
        s_four= (TextView) findViewById(R.id.rating_number_4);
        s_five = (TextView) findViewById(R.id.rating_number_5);
        //.------------------------------------------------



        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout_3);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_3);
        if (navigationView != null) {
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
                        Toast.makeText(SitiosDescripcionActivity.this, "Defaul name".toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SitiosDescripcionActivity.this, "imagen por defecto".toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Picasso.with(SitiosDescripcionActivity.this).load(dataSnapshot.getValue() + "").into(foto);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(dirIntent.getString("MY_URL"));

        imageView = (ImageView) findViewById(R.id.thumbnail);
        textViewTitulo = (TextView) findViewById(R.id.titulo);
        textViewDescrip = (TextView) findViewById(R.id.descrip);

        rutafabButton = (FloatingActionButton) findViewById(R.id.fabRutasss);
        textViewRute = (TextView) findViewById(R.id.textView5);
        mRatingBar = (RatingBar) findViewById(R.id.rating_bar);

        cardRating = (CardView) findViewById(R.id.card_rating);
        cardStars = (CardView) findViewById(R.id.card_grafic);



        textViewRute.setText("Te gusto este sitio turístico, puntúa");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabsss);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                finish();
            }
        });
        rutafabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = databaseReference.child("ubication");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Intent intent = new Intent(SitiosDescripcionActivity.this, MejorRutaActivity.class);
                        intent.putExtra("LOCATION_GPS_LAT",dataSnapshot.child("lat").getValue()+"");
                        intent.putExtra("LOCATION_GPS_LNG",dataSnapshot.child("long").getValue()+"");
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });



        textViewTitulo.setText(dirIntent.getString("TITULO"));
        textViewDescrip.setText(dirIntent.getString("DESCRIPCION"));
        Picasso.with(SitiosDescripcionActivity.this).load(dirIntent.getString("IMAGEN")).into(imageView);
       //Toast.makeText(SitiosDescripcionActivity.this,dirIntent.getString("BASE"),Toast.LENGTH_SHORT).show();
        final DatabaseReference ref = databaseReference.child("rating");
        final DatabaseReference userVote = databaseReference.child("users_vote").push();
        final DatabaseReference UserCom = databaseReference.child("users_vote");

        verOptions(UserCom,firebaseAuth);
        GetStars(UserCom,firebaseAuth);
        setRatingFuncion(userVote);

        mLayoutManager = new LinearLayoutManager(SitiosDescripcionActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        CrecyclerView = (RecyclerView) findViewById(R.id.comentarios_rv);
        CrecyclerView.setHasFixedSize(false);
        CrecyclerView.setLayoutManager(mLayoutManager);
        final DatabaseReference rift = databaseReference.child("user_comets");
        rift.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){

                    ViewComent( databaseReference.child("user_comets"),CrecyclerView,firebaseRecyclerAdapter);
                }else {
                    Toast.makeText(SitiosDescripcionActivity.this,"Sin comentarios...",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void ViewComent(DatabaseReference user_comets, RecyclerView crecyclerView, FirebaseRecyclerAdapter<Comentario, ViewComents.ViewComentViewHolder> recyclerAdapter) {
        ViewComents coments = new ViewComents();
        Create_viewComents create_viewComents = new Create_viewComents(coments);
        create_viewComents.i_viewComents.GetComendtOfDataBase(recyclerAdapter,user_comets,crecyclerView);

    }

    private void GetStars(DatabaseReference userCom, FirebaseAuth firebaseAuth) {
        final double[] star1 = {0};
        final double[] star2 = {0};
        final double[] star3 = {0};
        final double[] star4 = {0};
        final double[] star5 = {0};
        userCom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.child("voto").getValue() != null){
                        double number = Double.parseDouble(data.child("voto").getValue().toString());
                        if(number < 2){
                            star1[0]++;
                            s_one.setText(star1[0]+"");
                        }else {
                            if( number < 3 && number >=1.5){
                                star2[0]++;
                                s_two.setText(star2[0]+"");
                            }else {
                                if( number < 4 && number >=2.5){
                                    star3[0]++;
                                    s_tree.setText(star3[0]+"");
                                }else {
                                    if( number < 5 && number >=3.5)
                                    {
                                        star4[0]++;
                                        s_four.setText(star4[0]+"");
                                    }if( number < 6 && number >=4.5)
                                    {
                                        star5[0]++;
                                        s_five.setText(star5[0]+"");
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setRatingFuncion(final DatabaseReference ref) {
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ref.setValue(rating);

                //agregarUserVote(userVote,firebaseAuth,rating);
                if (fromUser) {
                    textViewRute.setText("Gracias !!!");
                    agregarUserVote(ref,firebaseAuth,rating);
                    cardRating.setVisibility(View.GONE);
                    cardStars.setVisibility(View.GONE);
                    //________________________________
                    SetComentDialog(databaseReference.child("user_comets").push(),firebaseAuth);
                }

                mRatingBar.setClickable(false);
            }
        });
    }

    private void SetComentDialog(DatabaseReference ref, FirebaseAuth firebaseAuth) {
        AddComent coment = new AddComent();
        Create_AddComent addComent = new Create_AddComent(coment);
        addComent.i_addComent.comend(SitiosDescripcionActivity.this,ref,firebaseAuth.getCurrentUser().getEmail().toString());

    }

    private void verOptions(final DatabaseReference userCom, final FirebaseAuth firebaseAuth) {
        final boolean[] paso = {false};
        userCom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot ds : dataSnapshot.getChildren()){
                   //Toast.makeText(SitiosDescripcionActivity.this,ds.child("usuario").getValue()+"",Toast.LENGTH_LONG).show();
                   /*if (ds.child("usuario").getValue().equals(firebaseAuth.getCurrentUser().getUid()+"") && ds.child("usuario")!=null){
                       cardStars.setVisibility(View.VISIBLE);
                       //Toast.makeText(SitiosDescripcionActivity.this,ds.child("usuario").getValue()+"",Toast.LENGTH_LONG).show();
                       //Toast.makeText(SitiosDescripcionActivity.this,a+"",Toast.LENGTH_LONG).show();
                       paso[0] =true;
                   }*/
                   if(ds.child("usuario").getValue() != null){
                       if (ds.child("usuario").getValue().equals(firebaseAuth.getCurrentUser().getUid()+"") && ds.child("usuario")!=null){
                           cardStars.setVisibility(View.VISIBLE);
                           //Toast.makeText(SitiosDescripcionActivity.this,ds.child("usuario").getValue()+"",Toast.LENGTH_LONG).show();
                           //Toast.makeText(SitiosDescripcionActivity.this,a+"",Toast.LENGTH_LONG).show();
                           paso[0] =true;
                       }
                   }
               }
                //Toast.makeText(SitiosDescripcionActivity.this,"VISIBILIDAD: "+cardRating.getVisibility(),Toast.LENGTH_LONG).show();
               if(!paso[0]){
                    cardRating.setVisibility(View.VISIBLE);
                }
               /*else {
                   Toast.makeText(SitiosDescripcionActivity.this,"funciona ♥",Toast.LENGTH_LONG).show();
               }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void agregarUserVote(DatabaseReference userVote, FirebaseAuth firebaseAuth, float rating) {

        userVote.child("usuario").setValue(firebaseAuth.getCurrentUser().getUid());
        userVote.child("voto").setValue(rating);
    }


    private void getDatos(Bundle dirIntent) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Lugares/"+dirIntent.getInt("id"));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textViewTitulo.setText(dataSnapshot.child("titulo").getValue()+"");
                textViewDescrip.setText(dataSnapshot.child("descripcion").getValue()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                                startActivity(new Intent(SitiosDescripcionActivity.this,ProfileActivity.class));
                                //______________________________________________

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_profile:
                                menuItem.setChecked(true);

                                //______________________________________________
                                finish();
                                startActivity(new Intent(SitiosDescripcionActivity.this,EditProfileActivity.class));
                                //______________________________________________


                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_grabarruta:
                                menuItem.setChecked(true);

                                //______________________________________________________________________________
                                Intent intent = new Intent(SitiosDescripcionActivity.this, LocationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                //___________________________________________________________________________________

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_misrutas:
                                menuItem.setChecked(true);
                                //______________________________________________
                                finish();
                                startActivity(new Intent(SitiosDescripcionActivity.this,MisRutasUsuarioActivity.class));
                                //______________________________________________

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                            case R.id.item_navigation_drawer_settings:
                                menuItem.setChecked(true);

                                Toast.makeText(SitiosDescripcionActivity.this, " " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_help_and_feedback:
                                menuItem.setChecked(true);
                                Toast.makeText(SitiosDescripcionActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
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
                drawerLayout.openDrawer(GravityCompat.START);
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
                return true;
                //startActivity(new Intent(MainActivity.this,MejorRutaActivity.class));
                //finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
