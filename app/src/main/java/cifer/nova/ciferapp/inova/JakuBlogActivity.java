package cifer.nova.ciferapp.inova;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cifer.nova.ciferapp.inova.Adaptadores.JakuAdapter;
import cifer.nova.ciferapp.inova.Clases.JakuClass;

import cifer.nova.ciferapp.inova.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class JakuBlogActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager IManagement;
    //data storage
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jaku_blog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Firebase storage referense
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReferenceFromUrl("gs://starlit-brand-127316.appspot.com/");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //VerOpciones();
    }

    private void VerOpciones() {
        String url1 ="https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fotuzco01.jpg?alt=media&token=e9d77ab2-5079-46b7-8c07-cfaa7a95b66c";
        String url_2 ="https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fcumbe.jpg?alt=media&token=412eb24a-4713-42c7-9f5b-53cac1500cc8";
        String url3 ="https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fbanios.jpg?alt=media&token=0e24907f-d08a-4701-aa57-25e023dc63d8";
        String url4 ="https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fsantapa.jpg?alt=media&token=9e6d6e26-7a86-401c-97ad-bdd87d32fe1b";
        String url5 ="https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fbelencom.jpg?alt=media&token=3fdef150-a064-470d-8737-68e1dc6dd743";


        final List<JakuClass> mClasses = new ArrayList<>();
        mClasses.add(new JakuClass(R.mipmap.image_4,"Otusco",url1));
        mClasses.add(new JakuClass(R.mipmap.image_4,"Lugar_2",url_2));
        mClasses.add(new JakuClass(R.mipmap.image_4,"Lugar_3",url3));
        mClasses.add(new JakuClass(R.mipmap.image_4,"Lugar_3",url3));
        mClasses.add(new JakuClass(R.mipmap.image_4,"Lugar_3",url3));

        recycler = (RecyclerView) findViewById(R.id.JBlogRV);
        IManagement = new GridLayoutManager(this,2);
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
