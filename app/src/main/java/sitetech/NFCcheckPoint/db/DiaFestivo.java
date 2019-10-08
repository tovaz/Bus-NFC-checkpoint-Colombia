package sitetech.NFCcheckPoint.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "DIA_FESTIVO".
 */
@Entity
public class DiaFestivo {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private java.util.Date dia;
    private boolean eliminado;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public DiaFestivo() {
    }

    public DiaFestivo(Long id) {
        this.id = id;
    }

    @Generated
    public DiaFestivo(Long id, String nombre, java.util.Date dia, boolean eliminado) {
        this.id = id;
        this.nombre = nombre;
        this.dia = dia;
        this.eliminado = eliminado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getNombre() {
        return nombre;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setNombre(@NotNull String nombre) {
        this.nombre = nombre;
    }

    @NotNull
    public java.util.Date getDia() {
        return dia;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDia(@NotNull java.util.Date dia) {
        this.dia = dia;
    }

    public boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
