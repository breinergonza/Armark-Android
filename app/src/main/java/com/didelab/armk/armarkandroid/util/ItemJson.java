package com.didelab.armk.armarkandroid.util;

/**
 * Created by ElianaXimena on 04/09/2016.
 */
public class ItemJson {

    String nombre;
    String valor;

    public ItemJson(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
