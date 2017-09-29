package cifer.nova.ciferapp.inova;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import cifer.nova.ciferapp.inova.Clases.Buid_dialog_all;
import cifer.nova.ciferapp.inova.Clases.Create_Dialog_help;
import cifer.nova.ciferapp.inova.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ListaTuristicosActivity extends AppCompatActivity {


    private ImageButton imageButton;
    private EditText mEditTextTitle;
    private EditText mEditTextDetail;
    private TextView textVievv;
    private RatingBar mRatingBar;
    private Button mButtonSumint;
    private Button buttonThisLoc;
    private Uri imageUri = null;

    private ProgressDialog progressDialog;

    private static final int GALLERY_REQUEST = 3;
    private static final int CAMERA_REQUEST = 1888;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private Intent intento;
    Bundle dirIntent;

    private Integer count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_turisticos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);


        storageReference = FirebaseStorage.getInstance().getReference();
        intento = getIntent();
        dirIntent = intento.getExtras();
        switch (dirIntent.getInt("idd")) {
            case 0:
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("cajamarca");
                break;
            case 1:
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("porcon");
                break;
            case 2:
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("Otusco");
                break;
            case 3:
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("Cumbe");
                break;
            case 4:
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("Banios");
                break;
            case 5:
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("santa");
                break;
            case 6:
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("belen");
                break;
            case 7:
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares").child("Celendin");
                break;
        }

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        imageButton = (ImageButton) findViewById(R.id.imageSubirButton);
        mEditTextTitle = (EditText) findViewById(R.id.editTextTituloListaTuristicos);
        mEditTextDetail = (EditText) findViewById(R.id.editTextDescripcionListaTuristicos);
        mButtonSumint = (Button) findViewById(R.id.buttonSunir);
        buttonThisLoc = (Button) findViewById(R.id.ubicacion);
        textVievv = (TextView) findViewById(R.id.coordenadas);
        textVievv.setText("coordenadas: "+dirIntent.get("LOCATION_L") +"/"+ dirIntent.get("LOCATION_O"));
        buttonThisLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaTuristicosActivity.this,VerMisRutasohYeahActivity.class);
                intent.putExtra("GET_RUTA",1);
                intent.putExtra("THI_LOC",dirIntent.getInt("idd"));
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                //galleryIntent.setType("image/*");
                //startActivityForResult(galleryIntent,GALLERY_REQUEST);
                //if (ContextCompat.checkSelfPermission(ListaTuristicosActivity.this, Manifest.permission.MAPS_RECEIVE) == PackageManager.PERMISSION_DENIED)
                if(dirIntent.get("LOCATION_L") == null) {
                    //progressDialog.dismiss();
                    Toast.makeText(ListaTuristicosActivity.this, "Debe generar una ubicación primero…", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,0);
                }



            }
        });
        mButtonSumint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startPosting();
            }
        });
        Button cancel = (Button) findViewById(R.id.buttoncanunir);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void startPosting() {
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        final DatabaseReference nameRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        final String url = databaseReference.push() + "";
        count++;
        final String title_val = mEditTextTitle.getText().toString().trim();
        final String descrip_val = mEditTextDetail.getText().toString().trim();
        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(descrip_val) && imageUri != null && dirIntent.get("LOCATION_L")!=null) {
            StorageReference fileup = storageReference.child("Blog_image").child(imageUri.getLastPathSegment());
            fileup.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    DatabaseReference dataref = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
                    dataref.child("Titulo").setValue(title_val);
                    dataref.child("Descripcion").setValue(descrip_val);
                    dataref.child("Imagen").setValue(downloadUri.toString());
                    if(firebaseAuth.getCurrentUser().getDisplayName() != null) {
                        dataref.child("UsuarioId").setValue("subido por: " + firebaseAuth.getCurrentUser().getDisplayName());
                    }else{
                        dataref.child("UsuarioId").setValue("subido por: " + firebaseAuth.getCurrentUser().getEmail());
                    }
                    dataref.child("rating").setValue(0);
                    dataref.child("ubication").child("lat").setValue(dirIntent.get("LOCATION_L"));
                    dataref.child("ubication").child("long").setValue(dirIntent.get("LOCATION_O"));
                    dataref.child("my_url").setValue(url);
                    progressDialog.dismiss();
                    finish();
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Algunos campos están vacíos ... ", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                imageUri = data.getData();
                //imageButton.setImageURI(imageUri);
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageButton.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_for_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_new_fs:
                int idd = item.getItemId();
                //noinspection SimplifiableIfStatement

            case R.id.action_close_fs:
                  finish();
                return true;
            case R.id.action_save_fs:
                startPosting();
                return true;
        }
        /*if (id == R.id.action_salir) {
            //return true;
            //startActivity(new Intent(MainActivity.this,MejorRutaActivity.class));
            finish();
        } else {
            if (id == R.id.action_opciones) {
                return true;
                //startActivity(new Intent(MainActivity.this,MejorRutaActivity.class));
                //finish();
            }
        }*/
        if (id == R.id.action_help_fs)
        {
            Create_Dialog_help help = new Create_Dialog_help();
            Buid_dialog_all buidDialogAll = new Buid_dialog_all(help);
            buidDialogAll.i_create_dialog_help.dialog(ListaTuristicosActivity.this,getString(R.string.guardar_lugar));
        }

        return super.onOptionsItemSelected(item);
    }

}
