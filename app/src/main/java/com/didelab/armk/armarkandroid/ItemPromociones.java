package com.didelab.armk.armarkandroid;

/**
 * Información del formulario
 *
 * @author Eliana Ximena Gonzalez Morales
 *
 */
class ItemPromociones {

    int id;
    String nombrePromocion;
    String descripcion1;
    String descripcion2;
    String rutaImagenProducto;


    /**
     *
     * @param id
     * @param nombrePromocion
     * @param descripcion1
     * @param descripcion2
     * @param rutaImagenProducto
     */
    public ItemPromociones(int id, String nombrePromocion, String descripcion1, String descripcion2,
                           String rutaImagenProducto) {
        super();
        this.id = id;
        this.nombrePromocion = nombrePromocion;
        this.descripcion1 = descripcion1;
        this.descripcion2 = descripcion2;
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

    public String getDescripcion1() {
        return descripcion1;
    }

    public void setDescripcion1(String descripcion1) {
        this.descripcion1 = descripcion1;
    }

    public String getDescripcion2() {
        return descripcion2;
    }

    public void setDescripcion2(String descripcion2) {
        this.descripcion2 = descripcion2;
    }

    public String getRutaImagenProducto() {
        return rutaImagenProducto;
    }

    public void setRutaImagenProducto(String rutaImagenProducto) {
        this.rutaImagenProducto = rutaImagenProducto;
    }



}
