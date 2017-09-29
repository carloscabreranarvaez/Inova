package cifer.nova.ciferapp.inova.Fragmnets;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cifer.nova.ciferapp.inova.Clases.Blogclass;
import cifer.nova.ciferapp.inova.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class BlogMainFragment extends Fragment {
    private View v;
    private RecyclerView mRecyclerView ;

    private DatabaseReference mDatabaseReference;
    private static FirebaseAuth firebaseAuth;


    public BlogMainFragment()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // v =  inflater.inflate(R.layout.fragment_main_tabed, container, false);

       // mRecyclerView =(RecyclerView) v.findViewById(R.id.BlogIdRV);

        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post_lugares");
        PolateFireBaseRecyclerAdapter();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        return mRecyclerView;

    }


    private void PolateFireBaseRecyclerAdapter() {
        FirebaseRecyclerAdapter<Blogclass,BlogMainFragment.BlogViewHolderJ> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blogclass, BlogViewHolderJ>(
                Blogclass.class,
                R.layout.blog_item_rv,
                BlogViewHolderJ.class,
                mDatabaseReference

        )
        {
            @Override
            protected void populateViewHolder(BlogViewHolderJ viewHolder, Blogclass model, int position) {
                viewHolder.SetTitulo(model.getTitulo());
                viewHolder.SetDesc(model.getDescripcion());
                viewHolder.SetAuth(model.getUsuarioId());
                viewHolder.SetImage(getActivity(),model.getImagen());
            }

        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    private class BlogViewHolderJ extends RecyclerView.ViewHolder {
        View mView;

        public BlogViewHolderJ(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void SetTitulo(String title)
        {
            TextView post_titulo = (TextView) mView.findViewById(R.id.titulo_blog_id);
            post_titulo.setText(title);
        }
        public void SetDesc(String desc)
        {
            TextView post_desc = (TextView) mView.findViewById(R.id.descripcion_blog_id);
            post_desc.setText(desc);
        }
        public void SetAuth(String desc)
        {
            //final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            TextView post_auth = (TextView) mView.findViewById(R.id.usuario_blog_id);
            post_auth.setText(desc);
        }
        public void SetImage(Context ctx, String imagen){
            ImageView post_img = (ImageView) mView.findViewById(R.id.image_blog);
            Picasso.with(ctx).load(imagen).into(post_img);
        }

    }
}
