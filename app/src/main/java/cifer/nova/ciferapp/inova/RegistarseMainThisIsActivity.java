package cifer.nova.ciferapp.inova;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cifer.nova.ciferapp.inova.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegistarseMainThisIsActivity extends AppCompatActivity implements   GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "GoogleActivity";
    // login whit google
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    //botones
    private ImageView fbButton;
    private ImageView googleButton;
    //botones textos
    private TextView googleregis;
    private TextView fbregis;
    //progressDialog
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registarse_main_this_is);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        googleButton =(ImageView) findViewById(R.id.sign_in_button_google);
        googleregis = (TextView) findViewById(R.id.text_google_conect);
        fbregis = (TextView) findViewById(R.id.text_fb_conect);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        mAuth = FirebaseAuth.getInstance();
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                signIn();
            }
        });

        fbButton = (ImageView) findViewById(R.id.fb_login_button);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistarseMainThisIsActivity.this,RegisterForEmailActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d(TAG, "signInWithCredential:success"+user);
                    updateUI(user);
                    Intent i = new Intent(RegistarseMainThisIsActivity.this,ProfileActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(RegistarseMainThisIsActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
                // [START_EXCLUDE]
                hideProgressDialog();
                // [END_EXCLUDE]
            }
        });
    }

    private void showProgressDialog() {

        pd = new ProgressDialog(RegistarseMainThisIsActivity.this);
        pd.setMessage("loading");
        pd.show();
    }
    private void hideProgressDialog(){
        pd.dismiss();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
    private void updateUI(FirebaseUser user) {

        if (user != null) {
            //Toast.makeText(this, "hola "+user.getEmail()+" ☺", Toast.LENGTH_SHORT).show();
            if(user.getDisplayName() == null){
                fbregis.setText(user.getEmail()+"");
                googleregis.setText("Regístrate con Google"+ user.getDisplayName());
            }else {
                googleregis.setText("Continuar como " + user.getDisplayName());
                fbregis.setText("Regístrate con E-mail.");
            }
        } else {
            googleregis.setText("Regístrate con Google.");
            fbregis.setText("Regístrate con E-mail.");
        }
    }
}
