package cifer.nova.ciferapp.inova.Clases;

import android.support.design.widget.FloatingActionButton;

/**
 * Created by Cifer on 24/11/2016.
 */

public class OpcionesCardView {
    private int imagen;
    private String texto;
    private FloatingActionButton boton;
    private int color;

    public OpcionesCardView(int color, FloatingActionButton boton, int imagen, String texto) {
        this.imagen = imagen;
        this.texto = texto;
        this.boton = boton;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getImagen() {
        return imagen;
    }
    public FloatingActionButton getBoton() {

        return boton;
    }

    public void setBoton(FloatingActionButton boton) {
        this.boton = boton;
        boton.setImageResource(imagen);
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
