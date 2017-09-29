package cifer.nova.ciferapp.inova.Usuario;

/**
 * Created by Cifer on 26/10/2016.
 */

public class Usuario {
    private String Nombre;
    private String Apellido;
    private double Initlng;
    private double Initlat;
    /*------------------*/
    private double lng;
    private double lat;



    public Usuario() {
    }

    public Usuario(String apellido, String nombre, double initlat, double initlng ,
                   double lng ,double lat ) {
        Apellido = apellido;
        Nombre = nombre;
        this.Initlat = initlat;
        this.Initlng = initlng;
        this.lat = lat;
        this.lng = lng;
    }

    public Usuario( double initlat, double initlng) {

        lat = initlat;
        lng = initlng;
    }

    public double getInitlat() {
        return Initlat;
    }

    public void setInitlat(double initlat) {
        Initlat = initlat;
    }

    public double getInitlng() {
        return Initlng;
    }

    public void setInitlng(double initlng) {
        Initlng = initlng;
    }

    /*public Usuario(String apellido, String nombre) {
        Apellido = apellido;
        Nombre = nombre;
    }*/

   /* public Usuario(Ruta rutas) {
        this.rutas = rutas;
    }*/

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }


}
