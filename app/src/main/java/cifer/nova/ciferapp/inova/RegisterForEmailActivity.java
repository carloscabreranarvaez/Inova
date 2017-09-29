package cifer.nova.ciferapp.inova;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import cifer.nova.ciferapp.inova.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterForEmailActivity extends AppCompatActivity {

    LinearLayout linea1,linea2;
    Button entrar,regis,nwregis;
    EditText username,passworm,name,lastname,newusername,newpassworm;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase mFirebaseDatabase;

    DatabaseReference mReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_email);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getInstans
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        if(mAuth.getCurrentUser() != null){
            Intent i = new Intent(RegisterForEmailActivity.this,ProfileActivity.class);
            startActivity(i);
            finish();        }
        //palleteall
        setViews();
        //clicks
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginWhitEmail();
            }
        });
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linea1.setVisibility(View.GONE);
                linea2.setVisibility(View.VISIBLE);
                //NewUserWhitEmail();
            }
        });
        nwregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewUserWhitEmail();
            }
        });

    }

    private void NewUserWhitEmail() {

        int redColorValue = Color.RED;
        int greenColorValue = Color.parseColor("#00ff00");

        if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(lastname.getText().toString())) {
            Toast.makeText(RegisterForEmailActivity.this, "Los nombres estan vacios", Toast.LENGTH_SHORT).show();
            name.setHintTextColor(redColorValue);
        } else {
            if (TextUtils.isEmpty(newusername.getText().toString())) {
                Toast.makeText(RegisterForEmailActivity.this, "Ingrese su Email", Toast.LENGTH_SHORT).show();
                newusername.setHintTextColor(redColorValue);
            } else {
                if (TextUtils.isEmpty(newpassworm.getText().toString())) {
                    Toast.makeText(RegisterForEmailActivity.this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
                    newpassworm.setHintTextColor(redColorValue);
                }else {
                    ValidateNewUser();
                }
            }
        }

    }

    private void ValidateNewUser() {
        final ProgressDialog dialog = new ProgressDialog(RegisterForEmailActivity.this);
        dialog.setMessage("Cargando...");
        dialog.show();
        String email = newusername.getText().toString().trim();
        final String Passworm = newpassworm.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email,Passworm).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    //https://www.youtube.com/watch?v=jmOEeJ4CFH4&t=20s
                    DatabaseReference base = mReference.child(mAuth.getCurrentUser().getUid().toString());
                    base.child("foto").setValue("https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/profile_fotos%2Fimage_2.png?alt=media&token=0d609366-d5b4-458d-9000-4bda8232cc5f");
                    base.child("nombre").setValue(name.getText().toString()+" "+lastname.getText().toString());
                    base.child("contraseña").setValue(Passworm);
                    dialog.dismiss();
                    changeViews();
                }
            }
        });
    }

    private void changeViews() {
        linea1.setVisibility(View.VISIBLE);
        linea2.setVisibility(View.GONE);
    }

    private void LoginWhitEmail() {
        final int redColorValue = Color.RED;
        String e = username.getText().toString().trim();
        String p = passworm.getText().toString().trim();
        if(TextUtils.isEmpty(username.getText().toString())){
            Toast.makeText(RegisterForEmailActivity.this, "Ingrese Correo", Toast.LENGTH_SHORT).show();
            username.setHintTextColor(redColorValue);
        }else{
            if(TextUtils.isEmpty(passworm.getText().toString())){
                Toast.makeText(RegisterForEmailActivity.this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
                passworm.setHintTextColor(redColorValue);
            }else {
                Toast.makeText(RegisterForEmailActivity.this, e+"__"+ p, Toast.LENGTH_SHORT).show();
                mAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(RegisterForEmailActivity.this,ProfileActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(RegisterForEmailActivity.this, "Datos invalidos", Toast.LENGTH_SHORT).show();
                            name.setHintTextColor(redColorValue);
                        }
                    }
                });
            }
        }
    }
    private void setViews() {
        linea1 = (LinearLayout) findViewById(R.id.linearregistool);
        linea2 = (LinearLayout) findViewById(R.id.linearlogin);
        entrar = (Button) findViewById(R.id.RegisEmailentrar);
        regis = (Button) findViewById(R.id.RegisEmailNewUser);
        nwregis = (Button) findViewById(R.id.RegisEmailNewRegis);
        username = (EditText) findViewById(R.id.RegisEmailUsername);
        passworm = (EditText) findViewById(R.id.RegisEmailPassworm);
        name = (EditText) findViewById(R.id.RegisEmailNewName);
        lastname = (EditText) findViewById(R.id.RegisEmailNewLastname);
        newusername = (EditText) findViewById(R.id.RegisEmailNewUsername);
        newpassworm = (EditText) findViewById(R.id.RegisEmailNewPassworm);
    }



}
