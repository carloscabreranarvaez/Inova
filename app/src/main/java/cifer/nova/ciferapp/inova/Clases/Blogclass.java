package cifer.nova.ciferapp.inova.Clases;

/**
 * Created by Cifer on 04/04/2017.
 */

public class Blogclass {
    private String Imagen;
    private String Titulo;
    private String Descripcion;
    private String UsuarioId;
    private String my_url;
    private double lat;
    private double lng;

    public Blogclass(){}


    public Blogclass(String imagen, String titulo, String descripcion, String usuarioId, String my_url, double lat, double lng) {
        Imagen = imagen;
        Titulo = titulo;
        Descripcion = descripcion;
        UsuarioId = usuarioId;
        this.my_url = my_url;
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getMy_url() {
        return my_url;
    }

    public void setMy_url(String my_url) {
        this.my_url = my_url;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getUsuarioId() {
        return UsuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        UsuarioId = usuarioId;
    }
}
