package cifer.nova.ciferapp.inova;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import cifer.nova.ciferapp.inova.Usuario.Ruta;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextNombreA;
    private TextView editTexUid;

    private Button buttonDatos;
    private ImageView imageViewProfile;
    private FloatingActionButton mFloatingActionButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference mStorageReference;

    private Ruta ruta;
    private Uri imageUri = null;

    private ProgressDialog progressDialog;

    private static final int GALLERY_REQUEST = 1;

    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("profile_fotos");

        mReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");


        editTextNombreA = (EditText) findViewById(R.id.EditNombre);
        imageViewProfile = (ImageView) findViewById(R.id.EditProfileImage);
        editTexUid = (TextView) findViewById(R.id.textprofileid);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fabprofileback);

        editTexUid.setText(firebaseAuth.getCurrentUser().getUid() + "");

        buttonDatos = (Button) findViewById(R.id.EditProfileGuardar);
        LatLng a = new LatLng(0.0, 0.0);
        LatLng b = new LatLng(1.1, 0.0);
        LatLng c = new LatLng(2.2, 0.0);

        List<LatLng> lan = new ArrayList<>();
        lan.add(a);
        lan.add(b);
        lan.add(c);

        ruta = new Ruta(lan);

        //getUserName();

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference ref = databaseReference.child("Usuarios").child(firebaseUser.getUid().toString().trim());

        final ProgressDialog dialog = new ProgressDialog(EditProfileActivity.this);
        dialog.setMessage("Cargando...");
        dialog.show();

        onDatabaseCahnge(ref,dialog);

        /*if (ref == null) {
            doDefautInfo(dialog);
        } else {


        }*/

        buttonDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                if (TextUtils.isEmpty(editTextNombreA.getText())) {
                    Toast.makeText(EditProfileActivity.this, "Ingrese nombre completo.", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isDigitsOnly(editTextNombreA.getText())) {
                        Toast.makeText(EditProfileActivity.this, "No se permiten solo números.", Toast.LENGTH_SHORT).show();
                    } else {
                        saveUserInfo();
                    }
                }

            }
        });
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void onDatabaseCahnge(final DatabaseReference ref, final ProgressDialog dialog) {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("nombre").getValue() != null) {
                    //Toast.makeText(EditProfileActivity.this, "Actualize su informacion", Toast.LENGTH_SHORT).show();
                    editTextNombreA.setHint(dataSnapshot.child("nombre").getValue().toString());
                    Picasso.with(EditProfileActivity.this).load(dataSnapshot.child("foto").getValue().toString()).into(imageViewProfile);
                    dialog.dismiss();
                } else {
                    doDefautInfo(dialog);
                    /*editTextNombreA.setHint(dataSnapshot.child("nombre").getValue().toString());
                    Picasso.with(EditProfileActivity.this).load(dataSnapshot.child("foto").getValue().toString()).into(imageViewProfile);*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void doDefautInfo(ProgressDialog dialog) {

        DatabaseReference base = mReference.child(firebaseAuth.getCurrentUser().getUid().toString());
        base.child("foto").setValue("https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/profile_fotos%2Fimage_2.png?alt=media&token=0d609366-d5b4-458d-9000-4bda8232cc5f");
        base.child("nombre").setValue("usuario jaku :D");
        dialog.dismiss();
    }

    private void saveUserInfo() {
        progressDialog.setMessage(" Actualizando información•••");
        progressDialog.show();
        final String name = editTextNombreA.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && imageUri != null) {
            StorageReference filePath = mStorageReference.child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String dowloadUri = taskSnapshot.getDownloadUrl().toString();
                    databaseReference.child("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("nombre").setValue(name);
                    databaseReference.child("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("foto").setValue(dowloadUri);
                    finish();
                    startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                    progressDialog.dismiss();
                }
            });
        } else {
            if (imageUri == null) {
                databaseReference.child("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("nombre").setValue(name);
                progressDialog.dismiss();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();

                imageViewProfile.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}
