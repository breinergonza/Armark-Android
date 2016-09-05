package com.didelab.armk.armarkandroid;

/**
 * Información del formulario
 *
 * @author Eliana Ximena Gonzalez Morales
 *
 */
class ItemProductos {

    int id;
    String nombrePromocion;
    String rutaImagenProducto;


    /**
     *
     * @param id
     * @param nombrePromocion
     * @param rutaImagenProducto
     */
    public ItemProductos(int id, String nombrePromocion, String rutaImagenProducto) {
        super();
        this.id = id;
        this.nombrePromocion = nombrePromocion;
        this.rutaImagenProducto = rutaImagenProducto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombrePromocion() {
        return nombrePromocion;
    }

    public void setNombrePromocion(String nombrePromocion) {
        this.nombrePromocion = nombrePromocion;
    }

    public String getRutaImagenProducto() {
        return rutaImagenProducto;
    }

    public void setRutaImagenProducto(String rutaImagenProducto) {
        this.rutaImagenProducto = rutaImagenProducto;
    }



}
