package cifer.nova.ciferapp.inova.Clases;

/**
 * Created by Cifer on 09/05/2017.
 */

public class LatLongClass {

    private double latitud;
    private double Longitud;

    public LatLongClass() {
    }

    public LatLongClass(double latitud, double longitud) {
        this.latitud = latitud;
        Longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }
}
