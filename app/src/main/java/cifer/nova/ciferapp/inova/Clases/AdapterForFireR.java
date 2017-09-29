package cifer.nova.ciferapp.inova.Clases;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import cifer.nova.ciferapp.inova.Interfaces.I_AdapterForFireR;
import cifer.nova.ciferapp.inova.R;
import cifer.nova.ciferapp.inova.VerMisRutasODMActivitye;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Cifer on 01/07/2017.
 */

public class AdapterForFireR implements I_AdapterForFireR{

    static FirebaseRecyclerAdapter<SharedClass, SharedViewHolder> recyclerAdapter;

    @Override
    public void SetUpAdapter(FirebaseRecyclerAdapter firebaseRecyclerAdapter,DatabaseReference mDatabaseReference,RecyclerView mRecyclerView) {


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SharedClass, SharedViewHolder>(
                SharedClass.class,
                R.layout.shared_routes,
                SharedViewHolder.class,
                mDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(SharedViewHolder viewHolder, SharedClass model, int position) {
                viewHolder.SetTitulo(model.getTitulo());
                viewHolder.getFecha(model.getFecha());
                viewHolder.getUser(model.getUsuario());
                viewHolder.setRutaMyurl(model.getOur_url());
                viewHolder.getOneRute(getApplicationContext(),position);
            }
        };
        recyclerAdapter = firebaseRecyclerAdapter;
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class SharedViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public SharedViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void SetTitulo(String title)
        {
            TextView post_titulo = (TextView) mView.findViewById(R.id.id_titulo);
            post_titulo.setText(title);

        }
        public void getFecha(String s)
        {
            TextView post_titulo = (TextView) mView.findViewById(R.id.id_fecha);
            post_titulo.setText(s);

        }
        public void getUser(String s)
        {
            TextView post_titulo = (TextView) mView.findViewById(R.id.id_users);
            post_titulo.setText("Subido por: "+s);

        }
        public void setRutaMyurl (String urls)
        {
            TextView urlTextView = (TextView) mView.findViewById(R.id.id_myurl);
            urlTextView.setText(urls);
        }
        public void getOneRute(final Context ctx, final int pocition) {
            ImageView cono = (ImageView) mView.findViewById(R.id.imageView2);
            cono.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, VerMisRutasODMActivitye.class);
                    intent.putExtra("URL", recyclerAdapter.getItem(pocition).getOur_url());
                    intent.putExtra("SHARE_BUTT",1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                }
            });
        }
    }
}
