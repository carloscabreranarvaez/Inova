package cifer.nova.ciferapp.inova.Clases;

/**
 * Created by Cifer on 18/04/2017.
 */

public class JakuClass {

    Integer Imagen;
    String Texto;
    String Url;


    public JakuClass(Integer imagen, String texto, String url) {
        Imagen = imagen;
        Texto = texto;
        Url = url;
    }

    public Integer getImagen() {
        return Imagen;
    }

    public void setImagen(Integer imagen) {
        Imagen = imagen;
    }

    public String getTexto() {
        return Texto;
    }

    public void setTexto(String texto) {
        Texto = texto;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
