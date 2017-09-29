package cifer.nova.ciferapp.inova.Clases;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import cifer.nova.ciferapp.inova.Interfaces.I_ViewComents;
import cifer.nova.ciferapp.inova.R;

/**
 * Created by Cifer on 05/07/2017.
 */

public class ViewComents implements I_ViewComents {

    static FirebaseRecyclerAdapter<Comentario, ViewComentViewHolder> recyclerAdapter;

    @Override
    public void GetComendtOfDataBase(FirebaseRecyclerAdapter firebaseRecyclerAdapter, DatabaseReference reference, RecyclerView recyclerView) {

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Comentario, ViewComentViewHolder>(
                Comentario.class,
                R.layout.cardview_comentarios,
                ViewComentViewHolder.class,
                reference
        ) {
            @Override
            protected void populateViewHolder(ViewComentViewHolder viewHolder, Comentario model, int position) {
                viewHolder.SetDesc(model.getComentario());
                viewHolder.SetUse(model.getUsuario());
            }
        };
        recyclerAdapter = firebaseRecyclerAdapter;
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ViewComentViewHolder extends RecyclerView.ViewHolder {
        View viiew;

        public ViewComentViewHolder(View itemView) {
            super(itemView);
            viiew = itemView;
        }

        public void SetDesc(String des) {
            TextView post_titulo = (TextView) viiew.findViewById(R.id.coment_descripcion);
            post_titulo.setText(des);
        }

        public void SetUse(String us) {
            TextView por = (TextView) viiew.findViewById(R.id.coment_usuario);
            por.setText(us);
        }
    }
}
