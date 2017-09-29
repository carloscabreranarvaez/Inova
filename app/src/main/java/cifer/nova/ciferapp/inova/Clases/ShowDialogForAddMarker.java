package cifer.nova.ciferapp.inova.Clases;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;

import cifer.nova.ciferapp.inova.Interfaces.I_ShowDialogForAddMarker;
import cifer.nova.ciferapp.inova.R;

/**
 * Created by Cifer on 25/06/2017.
 */

public class ShowDialogForAddMarker implements I_ShowDialogForAddMarker,View.OnClickListener{
    private Button servicio,alerta,lugar,otro;
    private LinearLayout linear_servicio,linear_alerta,linear_lugar,linear_otro;
    private ImageView hotel,restaurante,hospital,choro,restringido,animal,cuidado,paisaje,arqueologia,tienda,cementerio,bosque,insecto,araña,veneno,radio;

    private GoogleMap mGoogleMap;
    private Double mLat;
    private Double mLog;

    private DatabaseReference gDatabase,gDatabase2;
    private Context ctx;
    private AlertDialog dialog;

    @Override
    public void showInterface(final Context context, final DatabaseReference database, final DatabaseReference database_2) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = inflater.inflate(R.layout.dialog_marker_op,null);
        gDatabase = database;
        gDatabase2 = database_2;
        ctx = context;
        StartButtons(mView);
        StartLinears(mView);
        StartImages(mView);
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();

    }

    private void StartImages(View mView) {
        hotel = (ImageView) mView.findViewById(R.id.mrk_hotel);
        hotel.setOnClickListener(this);
        restaurante= (ImageView) mView.findViewById(R.id.mrk_rest);
        restaurante.setOnClickListener(this);
        hospital= (ImageView) mView.findViewById(R.id.mrk_hospital);
        hospital.setOnClickListener(this);
        choro= (ImageView) mView.findViewById(R.id.mrk_choro);
        choro.setOnClickListener(this);
        restringido= (ImageView) mView.findViewById(R.id.mrk_restring);
        restringido.setOnClickListener(this);
        animal= (ImageView) mView.findViewById(R.id.mrk_animalsal);
        animal.setOnClickListener(this);
        cuidado= (ImageView) mView.findViewById(R.id.mrk_alert);
        cuidado.setOnClickListener(this);
        paisaje= (ImageView) mView.findViewById(R.id.mrk_paisa);
        paisaje.setOnClickListener(this);
        arqueologia= (ImageView) mView.findViewById(R.id.mrk_arqui);
        arqueologia.setOnClickListener(this);
        tienda= (ImageView) mView.findViewById(R.id.mrk_shop);
        tienda.setOnClickListener(this);
        cementerio= (ImageView) mView.findViewById(R.id.mrk_cementerio);
        cementerio.setOnClickListener(this);
        bosque= (ImageView) mView.findViewById(R.id.mrk_bosque);
        bosque.setOnClickListener(this);
        insecto= (ImageView) mView.findViewById(R.id.mrk_bugs);
        insecto.setOnClickListener(this);
        araña= (ImageView) mView.findViewById(R.id.mrk_spider);
        araña.setOnClickListener(this);
        veneno= (ImageView) mView.findViewById(R.id.mrk_veneno);
        veneno.setOnClickListener(this);
        radio= (ImageView) mView.findViewById(R.id.mrk_radio);
        radio.setOnClickListener(this);
    }

    @Override
    public void doAddMarkers(GoogleMap googleMap, double lat, double log) {
        mGoogleMap = googleMap;
        mLat = lat;
        mLog = log;
    }

    private void StartLinears(View mView) {
        linear_servicio = (LinearLayout) mView.findViewById(R.id.linear_lugares);
        linear_alerta = (LinearLayout) mView.findViewById(R.id.linear_alert);
        linear_lugar = (LinearLayout) mView.findViewById(R.id.linear_paisaje);
        linear_otro = (LinearLayout) mView.findViewById(R.id.linear_otros);

    }

    private void StartButtons(View mView) {
        servicio = (Button) mView.findViewById(R.id.btn_lugar);
        alerta = (Button) mView.findViewById(R.id.btn_alert);
        lugar = (Button) mView.findViewById(R.id.btn_paisaje);
        otro = (Button) mView.findViewById(R.id.btn_otro);
        //______________________________________________________________
        servicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViLinear(linear_servicio,alerta,lugar,otro);
            }
        });
        alerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViLinear(linear_alerta,servicio,lugar,otro);
            }
        });
        lugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViLinear(linear_lugar,servicio,alerta,otro);

            }
        });
        otro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViLinear(linear_otro,alerta,lugar,servicio);

            }
        });
    }

    private void setViLinear(LinearLayout linear, Button b1, Button b2, Button b3) {
        linear.setVisibility(View.VISIBLE);
        b1.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);
        b3.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mrk_hotel:

                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Hospedaje")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                saveMarker("Hospedaje",mLat,mLog,gDatabase,gDatabase2,"HUE_GREEN");
                dialog.dismiss();
                break;
            case R.id.mrk_rest:
                Toast.makeText(ctx,"Restaurante",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Restaurante")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                saveMarker("Restaurante",mLat,mLog,gDatabase,gDatabase2, "HUE_GREEN");
                dialog.dismiss();
                break;
            case R.id.mrk_hospital:
                Toast.makeText(ctx,"centro medico",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Centro Médico")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                saveMarker("Centro Médico",mLat,mLog,gDatabase,gDatabase2, "HUE_GREEN");
                dialog.dismiss();
                break;
            case R.id.mrk_choro:
                Toast.makeText(ctx,"alerta de robo",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Alerta de robo")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                saveMarker("Alerta de robo",mLat,mLog,gDatabase,gDatabase2, "HUE_RED");
                dialog.dismiss();
                break;
            case R.id.mrk_restring:
                Toast.makeText(ctx,"Zona restringida",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Zona restringida")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                saveMarker("Zona restringida",mLat,mLog,gDatabase,gDatabase2, "HUE_RED");
                dialog.dismiss();
                break;
            case R.id.mrk_animalsal:
                Toast.makeText(ctx,"Peligro con los animales",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Peligro con los animales")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                saveMarker("Peligro con los animalesa",mLat,mLog,gDatabase,gDatabase2, "HUE_RED");
                dialog.dismiss();
                break;
            case R.id.mrk_alert:
                Toast.makeText(ctx,"tener cuidado",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Tener cuidado")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                saveMarker("Tener cuidado",mLat,mLog,gDatabase,gDatabase2, "HUE_RED");
                dialog.dismiss();
                break;
            case R.id.mrk_paisa:
                Toast.makeText(ctx,"Paisaje",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Paisaje")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                saveMarker("Paisaje",mLat,mLog,gDatabase,gDatabase2, "HUE_ORANGE");
                dialog.dismiss();
                break;
            case R.id.mrk_arqui:
                Toast.makeText(ctx,"Zona Arqueologica",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Zona Arqueologica")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                saveMarker("Paisaje",mLat,mLog,gDatabase,gDatabase2, "HUE_ORANGE");
                dialog.dismiss();
                break;
            case R.id.mrk_shop:
                Toast.makeText(ctx,"Tienda",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Tienda")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                saveMarker("Paisaje",mLat,mLog,gDatabase,gDatabase2, "HUE_ORANGE");
                dialog.dismiss();
                break;
            case R.id.mrk_cementerio:
                Toast.makeText(ctx,"Cementerio",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Cementerio")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                saveMarker("Cementerio",mLat,mLog,gDatabase,gDatabase2, "HUE_ORANGE");
                dialog.dismiss();
                break;
            case R.id.mrk_bugs:
                Toast.makeText(ctx,"Aqui hay Insectos",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Aquí hay Insectos")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                saveMarker("Aquí hay Insectos",mLat,mLog,gDatabase,gDatabase2, "HUE_BLUE");
                dialog.dismiss();
                break;
            case R.id.mrk_spider:
                Toast.makeText(ctx,"Aqui hay Arañas",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Aquí hay Arañas")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                saveMarker("Aquí hay Arañas",mLat,mLog,gDatabase,gDatabase2, "HUE_BLUE");
                dialog.dismiss();
                break;
            case R.id.mrk_veneno:
                Toast.makeText(ctx,"Zona toxica",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Zona tóxica")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                saveMarker("Zona tóxica",mLat,mLog,gDatabase,gDatabase2, "HUE_BLUE");
                dialog.dismiss();
                break;
            case R.id.mrk_radio:
                Toast.makeText(ctx,"Zona Radiactiva",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Zona Radioctiva")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                saveMarker("Zona Radioctiva",mLat,mLog,gDatabase,gDatabase2, "HUE_BLUE");
                dialog.dismiss();
                break;
            case R.id.mrk_bosque:
                Toast.makeText(ctx,"Bosque",Toast.LENGTH_SHORT).show();
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLat,mLog)).title("Bosque")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                saveMarker("Bosque",mLat,mLog,gDatabase,gDatabase2, "HUE_ORANGE");
                dialog.dismiss();
                break;

        }
    }

    private void saveMarker(String lugar, Double mlat, Double mlog, DatabaseReference gatabase, DatabaseReference gatabase2, String color){

        DatabaseReference base = gatabase.child("markers").push();
        base.child("lat").setValue(mlat);
        base.child("lng").setValue(mlog);
        base.child("nombre").setValue(lugar);
        base.child("color").setValue(color);

        /*DatabaseReference base2 = gatabase2.child("markers").push();
        base2.child("lat").setValue(mlat);
        base2.child("lng").setValue(mlog);
        base2.child("nombre").setValue(lugar);
        base2.child("color").setValue(color);*/

    }
}
