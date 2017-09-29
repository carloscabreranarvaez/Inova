package cifer.nova.ciferapp.inova;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cifer.nova.ciferapp.inova.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private CardView cardView;
    private TextView button;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTexReptPassword;
    private Button buttonRegistrar;
    private SignInButton signInButton;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


    private static  final int RC_SIGN_IN = 1;
    private static final String TAG = "MAIN_ACTIVITY";

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        editTextEmail = (EditText) findViewById(R.id.LoginUsuario);
        editTextPassword = (EditText) findViewById(R.id.LoginPasword);
        editTexReptPassword = (EditText) findViewById(R.id.LoginREPPasword);
        buttonRegistrar = (Button) findViewById(R.id.LoginButton);
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        button = (TextView) findViewById(R.id.RegistrarNewButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Intent i;
               // i = new Intent(LoginActivity.this, MainActivity.class);
                //i.putExtra("id",0);
                //startActivity(i);
            }
        });
        signInButton = (SignInButton) findViewById(R.id.LoginButtonGoogle);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //google api client
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                Toast.makeText(LoginActivity.this,"Connection fail",Toast.LENGTH_LONG).show();
                            }
                        })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /*cardView = (CardView) findViewById(R.id.LoginCardView);
        YoYo.with(Techniques.DropOut)
                .duration(400)
                .playOn(findViewById(R.id.LoginCardView));*/
        Intent intento = getIntent();
        final Bundle dirIntent = intento.getExtras();
        switch (dirIntent.getInt("GOOGLE_NO_AUTH"))
        {
            case 1:
                signIn();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if (true) {
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Ingrese su correo electronico...", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Escriba su contraseña...", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog.setMessage("Registrando Usuario ... ");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "USUARIO REGISTRADO EXITOSAMENTE!!", Toast.LENGTH_SHORT).show();

                        if (firebaseAuth.getCurrentUser() != null) {
                            progressDialog.dismiss();
                            //finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "!!ERROR 666error!!" + task.getException(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        }else
        {
            Toast.makeText(LoginActivity.this,editTextPassword.getText().toString()+"/n"+"---"+editTexReptPassword.getText().toString()+"---", Toast.LENGTH_LONG).show();
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                //progressDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();


            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(LoginActivity.this, "!!ERROR 666!! Sign In failed", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();


                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

/*final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Usuario/0");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                textView1.setText(user.getNombre());
                textView2.setText(user.getPassword());
                textView3.setText(user.getEmail());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/
