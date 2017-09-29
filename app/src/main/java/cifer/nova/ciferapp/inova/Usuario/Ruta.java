package cifer.nova.ciferapp.inova.Usuario;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Cifer on 30/11/2016.
 */

public class Ruta {

    private List<LatLng> latLngs;

    public Ruta() {
    }

    public Ruta(List<LatLng> latLngs) {
        this.latLngs = latLngs;
    }

    public List<LatLng> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(List<LatLng> latLngs) {
        this.latLngs = latLngs;
    }
}
