package cifer.nova.ciferapp.inova.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cifer.nova.ciferapp.inova.Clases.OpcionesCardView;
import cifer.nova.ciferapp.inova.EditProfileActivity;
import cifer.nova.ciferapp.inova.LocationActivity;
import cifer.nova.ciferapp.inova.LogOutActivity;
import cifer.nova.ciferapp.inova.MisRutasUsuarioActivity;

import cifer.nova.ciferapp.inova.R;
import cifer.nova.ciferapp.inova.ViewSharedRuoteActivity;

import java.util.List;

/**
 * Created by Cifer on 24/11/2016.
 */

public class OpcionesAdapter extends RecyclerView.Adapter<OpcionesAdapter.OpcioneViewHolder> {

    private List<OpcionesCardView> item;
    private Context context;
    private LayoutInflater inflater;

    public static  class OpcioneViewHolder extends RecyclerView.ViewHolder
    {
        //public ImageView imageView;
        public TextView texto;
        public FloatingActionButton FBoton;

        public OpcioneViewHolder(View itemView) {
            super(itemView);

            FBoton = (FloatingActionButton) itemView.findViewById(R.id.FabOpciones);
            //imageView = (ImageView) itemView.findViewById(R.id.ImageZonaTuristica);
            texto = (TextView) itemView.findViewById(R.id.TextoZonaTuristica);
        }
    }
    public OpcionesAdapter (Context context,List<OpcionesCardView> item)
    {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.item = item;
    }

    @Override
    public OpcioneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewopciones,parent,false);
        return new OpcioneViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OpcioneViewHolder holder, final int position) {
       //holder.imageView.setImageResource(item.get(position).getImagen());
        holder.FBoton.setImageResource(item.get(position).getImagen());
        holder.texto.setText(item.get(position).getTexto());
        holder.FBoton.setBackgroundColor(item.get(position).getColor());

        holder.FBoton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (position)
                {
                    case 0:
                        Intent intent = new Intent(context, LocationActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        //context.startActivity(new Intent(context,ListaTuristicosActivity.class));
                        break;
                    case 1:
                        context.startActivity(new Intent(context,MisRutasUsuarioActivity.class));
                        break;
                    case 3:
                        Intent intentt = new Intent(context, EditProfileActivity.class);
                        intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intentt);
                        //Intent im = new Intent(context, MejorRutaActivity.class);
                        //context.startActivity(im);
                        break;
                    case 4:
                        Intent rantent = new Intent(context, LogOutActivity.class);
                        rantent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(rantent);
                        //context.startActivity(new Intent(context,JakuBlogActivity.class));
                        //Toast.makeText(context,"click: "+position,Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Intent shared = new Intent(context, ViewSharedRuoteActivity.class);
                        //shared.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(shared);
                        //context.startActivity(new Intent(context,EditProfileActivity.class));
                        //Toast.makeText(context,"click: "+position,Toast.LENGTH_SHORT).show();
                        break;

                    case 5:

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
