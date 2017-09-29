package cifer.nova.ciferapp.inova;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cifer.nova.ciferapp.inova.R;

import cifer.nova.ciferapp.inova.Usuario.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref= database.getReference();
    private Usuario usuario;
    private List<Usuario> usuarioList;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonEntrar;
    private TextView buttonRegistro;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**/

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        /*RegistrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*DatabaseReference nameUser = ref.child("user3").child("NombreUsuario");
                DatabaseReference passUser = ref.child("user3").child("Password");
                DatabaseReference emailUser = ref.child("user3").child("Email");
                nameUser.setValue(eUssername.getText().toString());
                passUser.setValue(ePassword.getText().toString());
                emailUser.setValue(eMailText.getText().toString());
                usuario = new Usuario();
                usuario.setNombre(eUssername.getText().toString());
                usuario.setPassword(ePassword.getText().toString());
                usuario.setEmail(eMailText.getText().toString());

                usuarioList = new ArrayList<Usuario>();
                usuarioList.add(usuario);

                DatabaseReference ClassUser = ref.child("Usuario");
                //usuario = new Usuario(,,eMailText.getText().toString());
                ClassUser.setValue(usuarioList);
            }
        });

        ListaBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(MainActivity.this, ListaActivity.class);
                //i.putExtra("id",0);
                startActivity(i);
            }
        });*/
        editTextEmail= (EditText) findViewById(R.id.emailEntrar);
        editTextPassword = (EditText)findViewById(R.id.passwordEntrar);

        buttonEntrar = (Button) findViewById(R.id.button2);
        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        buttonRegistro = (TextView) findViewById(R.id.RegIdButton);
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }

    private void userLogin()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Ingrese su correo electronico...",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Escriba su contraseña...",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Cargando...");
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressDialog.dismiss();
                finish();
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
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
//-------------CODIGO_CONSUTAL_HOSTINGER________________--------------------------//
/*RegistrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String NombreUsuario = eUssername.getText().toString();
                final String Email = eMailText.getText().toString();
                    final String password = ePassword.getText().toString();
                final int IdUbicacion = 111;
                final int IdLugares = 111;

                Response.Listener<String> stringListener = new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Registro Exitoso!!").create().show();
                                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                                startActivity(i);

                            }else
                            {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                                builder2.setMessage("Registro Fallido!!").setNegativeButton("Cancelar",null).create().show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

RegisterRequest request = new RegisterRequest(NombreUsuario,Email,password,IdUbicacion,IdLugares,stringListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);queue.add(request);
            }});*/
