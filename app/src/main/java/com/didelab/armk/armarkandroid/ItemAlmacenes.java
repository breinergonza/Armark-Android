package com.didelab.armk.armarkandroid;

/**
 * Información del formulario
 *
 * @author Eliana Ximena Gonzalez Morales
 *
 */
class ItemAlmacenes {

    int id;
    String nombreAlmacen;
    String descripcion1;
    String descripcion2;
    String rutaLogo;
    boolean promocion;


    /**
     *
     * @param id
     * @param nombreAlmacen
     * @param descripcion1
     * @param descripcion2
     * @param rutaLogo
     * @param promocion
     */
    public ItemAlmacenes(int id, String nombreAlmacen, String descripcion1, String descripcion2,
                         String rutaLogo, boolean promocion) {
        super();
        this.id = id;
        this.nombreAlmacen = nombreAlmacen;
        this.descripcion1 = descripcion1;
        this.descripcion2 = descripcion2;
        this.rutaLogo = rutaLogo;
        this.promocion = promocion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
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

    public String getRutaLogo() {
        return rutaLogo;
    }

    public void setRutaLogo(String rutaLogo) {
        this.rutaLogo = rutaLogo;
    }


    public boolean isPromocion() {
        return promocion;
    }

    public void setPromocion(boolean promocion) {
        this.promocion = promocion;
    }

}
