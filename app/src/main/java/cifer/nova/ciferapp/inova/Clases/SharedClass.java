package cifer.nova.ciferapp.inova.Clases;

/**
 * Created by Cifer on 01/07/2017.
 */

public class SharedClass {

    private String titulo;
    private String usuario;
    private String fecha;
    private int Imagen;
    private String our_url;

    public SharedClass() {
    }

    public SharedClass(String title, String user, String feecha, int imagen, String wurl) {
        titulo = title;
        usuario = user;
        fecha = feecha;
        Imagen = imagen;
        this.our_url = wurl;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getImagen() {
        return Imagen;
    }

    public void setImagen(int imagen) {
        Imagen = imagen;
    }

    public String getOur_url() {
        return our_url;
    }

    public void setOur_url(String our_url) {
        this.our_url = our_url;
    }
}
