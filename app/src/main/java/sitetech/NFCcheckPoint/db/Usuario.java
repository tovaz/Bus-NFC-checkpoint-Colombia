package sitetech.NFCcheckPoint.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "USUARIO".
 */
@Entity
public class Usuario {

    @Id(autoincrement = true)
    private Long id;
    private String cedula;

    @NotNull
    private String nombre;
    private String password;
    private String telefono;

    @NotNull
    private String rol;
    private boolean activo;
    private boolean eliminado;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Usuario() {
    }

    public Usuario(Long id) {
        this.id = id;
    }

    @Generated
    public Usuario(Long id, String cedula, String nombre, String password, String telefono, String rol, boolean activo, boolean eliminado) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.password = password;
        this.telefono = telefono;
        this.rol = rol;
        this.activo = activo;
        this.eliminado = eliminado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    @NotNull
    public String getNombre() {
        return nombre;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setNombre(@NotNull String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @NotNull
    public String getRol() {
        return rol;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setRol(@NotNull String rol) {
        this.rol = rol;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
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
