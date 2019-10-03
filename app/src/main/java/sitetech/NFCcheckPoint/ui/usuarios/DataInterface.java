package sitetech.NFCcheckPoint.ui.usuarios;

import java.util.List;

import sitetech.NFCcheckPoint.db.Usuario;

public interface DataInterface {
    void crear(Class c);
    void eliminar(Class c);
    void buscar(String nombre, List<Class> lista);
    void modificar(Class c);
    void listar();
    void actualizarLista();
}
