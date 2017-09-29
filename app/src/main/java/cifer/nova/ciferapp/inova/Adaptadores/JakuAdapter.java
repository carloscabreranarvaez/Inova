package cifer.nova.ciferapp.inova.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cifer.nova.ciferapp.inova.BlogMainActivity;
import cifer.nova.ciferapp.inova.Clases.JakuClass;
import cifer.nova.ciferapp.inova.R;


import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Cifer on 18/04/2017.
 */

public class JakuAdapter extends RecyclerView.Adapter<JakuAdapter.JakuAdapterViewHolder> {

    private List<JakuClass> item;
    private Context context;
    private LayoutInflater inflater;

    public class JakuAdapterViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTextView;
        public ImageView mImageView;
        public JakuAdapterViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.JBlogTexyView);
            mImageView = (ImageView) itemView.findViewById(R.id.JBlogImageView);
        }
    }

    public JakuAdapter (Context context, List<JakuClass> item)
    {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.item = item;
    }

    @Override
    public JakuAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.jaku_blog_cv,parent,false);
        return new JakuAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(JakuAdapterViewHolder holder, final int position) {
        //String url = "https://firebasestorage.googleapis.com/v0/b/starlit-brand-127316.appspot.com/o/blog_jaku_images%2Fotuzco01.jpg?alt=media&token=e9d77ab2-5079-46b7-8c07-cfaa7a95b66c";
        holder.mTextView.setText(item.get(position).getTexto());
        holder.mImageView.setImageResource(item.get(position).getImagen());
        Picasso.with(context).load(item.get(position).getUrl()).fit().centerCrop().into(holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position)
                {
                    case 0:
                        //context.startActivity(new Intent(context,LocationActivity.class));
                        //context.startActivity(new Intent(context,ListaTuristicosActivity.class));
                        Intent i = new Intent(context,BlogMainActivity.class);
                        i.putExtra("id",0);
                        context.startActivity(i);
                        break;
                    case 1:
                        //context.startActivity(new Intent(context,LocationActivity.class));
                        //context.startActivity(new Intent(context,ListaTuristicosActivity.class));
                        Intent iwq = new Intent(context,BlogMainActivity.class);
                        iwq.putExtra("id",1);
                        context.startActivity(iwq);
                        break;
                    case 2:
                        //context.startActivity(new Intent(context,BlogMainActivity.class));
                        Intent j = new Intent(context,BlogMainActivity.class);
                        j.putExtra("id",2);
                        context.startActivity(j);
                        break;
                    case 3:
                        //Intent im = new Intent(context, MejorRutaActivity.class);
                        //context.startActivity(im);
                        Intent kw = new Intent(context,BlogMainActivity.class);
                        kw.putExtra("id",3);
                        context.startActivity(kw);
                        break;
                    case 4:
                        Intent ko = new Intent(context,BlogMainActivity.class);
                        ko.putExtra("id",4);
                        context.startActivity(ko);
                        //context.startActivity(new Intent(context,JakuBlogActivity.class));
                        //Toast.makeText(context,"click: "+position,Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Intent kwa = new Intent(context,BlogMainActivity.class);
                        kwa.putExtra("id",5);
                        context.startActivity(kwa);
                        //context.startActivity(new Intent(context,EditProfileActivity.class));
                        //Toast.makeText(context,"click: "+position,Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Intent kwq = new Intent(context,BlogMainActivity.class);
                        kwq.putExtra("id",6);
                        context.startActivity(kwq);
                        break;
                    case 7:
                        Intent kiuy = new Intent(context,BlogMainActivity.class);
                        kiuy.putExtra("id",7);
                        context.startActivity(kiuy);
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }



}
