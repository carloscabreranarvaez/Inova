package cifer.nova.ciferapp.inova.Clases;

/**
 * Created by Cifer on 05/07/2017.
 */

public class Comentario {

    String comentario;
    String usuario;

    public Comentario() {
    }

    public Comentario(String comentario, String usuario) {
        this.comentario = comentario;
        this.usuario = usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
