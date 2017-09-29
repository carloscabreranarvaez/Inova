package cifer.nova.ciferapp.inova.Clases;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Cifer on 09/05/2017.
 */

public class MisRutasClass {
    private List<LatLng> latLngs;
    private String fecha;
    private String our_url;
    private String titulo;
    private String general_url;


    public MisRutasClass() {
    }

    public MisRutasClass(List<LatLng> latLngs, String fecha, String our_url, String titulo, String general_url) {
        this.latLngs = latLngs;
        this.fecha = fecha;
        this.our_url = our_url;
        this.titulo = titulo;
        this.general_url = general_url;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGeneral_url() {
        return general_url;
    }

    public void setGeneral_url(String general_url) {
        this.general_url = general_url;
    }

    public String getOur_url() {
        return our_url;
    }

    public void setOur_url(String our_url) {
        this.our_url = our_url;
    }

    public List<LatLng> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(List<LatLng> latLngs) {
        this.latLngs = latLngs;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
