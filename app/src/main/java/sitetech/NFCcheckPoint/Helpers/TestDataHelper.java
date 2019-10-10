package sitetech.NFCcheckPoint.Helpers;

import android.util.Log;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.db.Bus;
import sitetech.NFCcheckPoint.db.Empresa;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.db.horarioPorRuta;

public class TestDataHelper {
    public static void crearData() {
        crearUsuarios();
        crearEmpresa();
        crearBus();
        crearRutas();
        crearHorariosTest();
        relacionarHorarios();
    }

    public static void crearEmpresa(){
        Empresa em = new Empresa();
        em.setEliminado(false);
        em.setNombre("Empresa ejemplo 1");
        em.setTelefono("5555-9999");
        AppController.daoSession.getEmpresaDao().insert(em);
        Log.d("TEST DATA", "Empresa creada con exito");
    }

    public static void crearBus(){
        Bus bus = new Bus();
        bus.setEliminado(false);
        bus.setEmpresa(AppController.daoSession.getEmpresaDao().loadAll().get(1));
        bus.setInterno("0000");
        bus.setPlaca("000-000");
        AppController.daoSession.getBusDao().insert(bus);
        Log.d("TEST DATA", "Bus creado con exito");
    }

    public static void crearUsuarios(){
        Usuario ux = new Usuario();
        ux.setNombre("admin");
        ux.setCedula("000");
        ux.setActivo(true);
        ux.setPassword("");
        ux.setEliminado(false);
        ux.setRol("Administrador");
        ux.setTelefono("");
        AppController.daoSession.getUsuarioDao().insert(ux);

        Log.d("TEST DATA", "Horario creado con exito");

        ux = new Usuario();
        ux.setNombre("A");
        ux.setCedula("000");
        ux.setActivo(true);
        ux.setPassword("");
        ux.setEliminado(false);
        ux.setRol("Operador");
        ux.setTelefono("");
        AppController.daoSession.getUsuarioDao().insert(ux);

        Log.d("TEST DATA", "Horario creado con exito");
    }

    public static void crearRutas(){
        Ruta rx = new Ruta();
        rx.setEliminada(false);
        rx.setNombre("Ruta Ejemplo 1");
        AppController.daoSession.getRutaDao().insert(rx);

        Log.d("TEST DATA", "Ruta creada con exito");

        rx = new Ruta();
        rx.setEliminada(false);
        rx.setNombre("Ruta Ejemplo 2");
        AppController.daoSession.getRutaDao().insert(rx);

        Log.d("TEST DATA", "Ruta creada con exito");
    }

    public static void crearHorariosTest(){
        Horario h = new Horario();
        h.setNombre("Horario ejemplo 1");
        h.setEliminado(false);
        h.setHoraDesde("08:00");
        h.setMaxMinutos(3);

        h.setHoraFestivoDesde("9:00");
        h.setMaxMinutosFestivo(10);
        AppController.daoSession.getHorarioDao().insert(h);

        h = new Horario();
        h.setNombre("Horario ejemplo 1");
        h.setEliminado(false);
        h.setHoraDesde("08:30");
        h.setMaxMinutos(3);

        h.setHoraFestivoDesde("9:30");
        h.setMaxMinutosFestivo(10);
        AppController.daoSession.getHorarioDao().insert(h);

        Log.d("TEST DATA", "Horarios creados con exito");
    }

    public static void relacionarHorarios(){
        horarioPorRuta hr = new horarioPorRuta();
        hr.setEliminado(false);
        hr.setHorario(AppController.daoSession.getHorarioDao().loadAll().get(1));
        hr.setRuta(AppController.daoSession.getRutaDao().loadAll().get(1));
        AppController.daoSession.getHorarioPorRutaDao().insert(hr);

        hr = new horarioPorRuta();
        hr.setEliminado(false);
        hr.setHorario(AppController.daoSession.getHorarioDao().loadAll().get(2));
        hr.setRuta(AppController.daoSession.getRutaDao().loadAll().get(1));
        AppController.daoSession.getHorarioPorRutaDao().insert(hr);

        hr = new horarioPorRuta();
        hr.setEliminado(false);
        hr.setHorario(AppController.daoSession.getHorarioDao().loadAll().get(1));
        hr.setRuta(AppController.daoSession.getRutaDao().loadAll().get(2));
        AppController.daoSession.getHorarioPorRutaDao().insert(hr);

        Log.d("TEST DATA", "Rutas relacionadas con exito");
    }
}
