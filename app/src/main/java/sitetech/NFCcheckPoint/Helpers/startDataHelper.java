package sitetech.NFCcheckPoint.Helpers;

import android.util.Log;

import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.horarioPorRuta;

public class startDataHelper {

    public static void loadAllData(){
        crearHorarios();
        crearRutas();
        asignarHorarios();
    }

    public static void crearHorarios(){
        crearHorario("Lun-Sab Dia", "04:40:00", "05:22:59", "04:50:00", "05:32:59");
        crearHorario("Lun-Sab Dia", "04:47:00", "05:30:59", "04:58:00", "05:41:59");
        crearHorario("Lun-Sab Dia", "04:54:00", "05:38:59", "05:06:00", "05:50:59");
        crearHorario("Lun-Sab Dia", "05:01:00", "05:46:59", "05:14:00", "05:59:59");
        crearHorario("Lun-Sab Dia", "05:06:00", "05:52:59", "05:22:00", "06:08:59");
        crearHorario("Lun-Sab Dia", "05:11:00", "05:57:59", "05:30:00", "06:16:59");
        crearHorario("Lun-Sab Dia", "05:16:00", "06:02:59", "05:38:00", "06:24:59");
        crearHorario("Lun-Sab Dia", "05:21:00", "06:07:59", "05:46:00", "06:32:59");
        crearHorario("Lun-Sab Dia", "05:26:00", "06:12:59", "05:54:00", "06:40:59");
        crearHorario("Lun-Sab Dia", "05:31:00", "06:17:59", "06:02:00", "06:48:59");
        crearHorario("Lun-Sab Dia", "05:36:00", "06:22:59", "06:10:00", "06:56:59");
        crearHorario("Lun-Sab Dia", "05:41:00", "06:27:59", "06:18:00", "07:04:59");
        crearHorario("Lun-Sab Dia", "05:46:00", "06:32:59", "06:26:00", "07:12:59");
        crearHorario("Lun-Sab Dia", "05:51:00", "06:37:59", "06:34:00", "07:20:59");
        crearHorario("Lun-Sab Dia", "05:56:00", "06:42:59", "06:42:00", "07:28:59");
        crearHorario("Lun-Sab Dia", "06:01:00", "06:47:59", "06:50:00", "07:36:59");
        crearHorario("Lun-Sab Dia", "06:06:00", "06:52:59", "06:58:00", "07:44:59");
        crearHorario("Lun-Sab Dia", "06:11:00", "06:57:59", "07:05:00", "07:51:59");
        crearHorario("Lun-Sab Dia", "06:16:00", "07:02:59", "07:12:00", "07:58:59");
        crearHorario("Lun-Sab Dia", "06:21:00", "07:07:59", "07:19:00", "08:05:59");
        crearHorario("Lun-Sab Dia", "06:26:00", "07:12:59", "07:26:00", "08:12:59");
        crearHorario("Lun-Sab Dia", "06:31:00", "07:17:59", "07:33:00", "08:20:59");
        crearHorario("Lun-Sab Dia", "06:36:00", "07:22:59", "07:40:00", "08:28:59");
        crearHorario("Lun-Sab Dia", "06:41:00", "07:27:59", "07:47:00", "08:35:59");
        crearHorario("Lun-Sab Dia", "06:46:00", "07:32:59", "07:54:00", "08:42:59");
        crearHorario("Lun-Sab Dia", "06:51:00", "07:37:59", "08:01:00", "08:49:59");
        crearHorario("Lun-Sab Dia", "06:56:00", "07:42:59", "08:08:00", "08:56:59");
        crearHorario("Lun-Sab Dia", "07:01:00", "07:47:59", "08:15:00", "09:03:59");
        crearHorario("Lun-Sab Dia", "07:06:00", "07:52:59", "08:22:00", "09:10:59");
        crearHorario("Lun-Sab Dia", "07:11:00", "07:57:59", "08:29:00", "09:17:59");
        crearHorario("Lun-Sab Dia", "07:16:00", "08:02:59", "08:36:00", "09:24:59");
        crearHorario("Lun-Sab Dia", "07:21:00", "08:07:59", "08:43:00", "09:31:59");
        crearHorario("Lun-Sab Dia", "07:26:00", "08:12:59", "08:50:00", "09:38:59");
        crearHorario("Lun-Sab Dia", "07:31:00", "08:17:59", "08:57:00", "09:45:59");
        crearHorario("Lun-Sab Dia", "07:36:00", "08:22:59", "09:04:00", "09:52:59");
        crearHorario("Lun-Sab Dia", "07:41:00", "08:27:59", "09:11:00", "09:59:59");
        crearHorario("Lun-Sab Dia", "07:46:00", "08:32:59", "09:18:00", "10:06:59");
        crearHorario("Lun-Sab Dia", "07:53:00", "08:40:59", "09:25:00", "10:13:59");
        crearHorario("Lun-Sab Dia", "08:00:00", "08:48:59", "09:32:00", "10:20:59");
        crearHorario("Lun-Sab Dia", "08:07:00", "08:55:59", "09:39:00", "10:27:59");
        crearHorario("Lun-Sab Dia", "08:14:00", "09:02:59", "09:46:00", "10:34:59");
        crearHorario("Lun-Sab Dia", "08:21:00", "09:09:59", "09:53:00", "10:41:59");
        crearHorario("Lun-Sab Dia", "08:28:00", "09:16:59", "10:00:00", "10:48:59");
        crearHorario("Lun-Sab Dia", "08:35:00", "09:23:59", "10:07:00", "10:55:59");
        crearHorario("Lun-Sab Dia", "08:42:00", "09:30:59", "10:14:00", "11:02:59");
        crearHorario("Lun-Sab Dia", "08:49:00", "09:37:59", "10:21:00", "11:09:59");
        crearHorario("Lun-Sab Dia", "08:56:00", "09:44:59", "10:28:00", "11:16:59");

        crearHorario("Lun-Sab Dia", "09:03:00", "09:51:59", "10:35:00", "11:23:59");
        crearHorario("Lun-Sab Dia", "09:10:00", "09:58:59", "10:42:00", "11:30:59");
        crearHorario("Lun-Sab Dia", "09:17:00", "10:05:59", "10:49:00", "11:37:59");
        crearHorario("Lun-Sab Dia", "09:24:00", "10:12:59", "10:56:00", "11:44:59");
        crearHorario("Lun-Sab Dia", "09:31:00", "10:19:59", "11:03:00", "11:51:59");
        crearHorario("Lun-Sab Dia", "09:38:00", "10:26:59", "11:10:00", "11:58:59");
        crearHorario("Lun-Sab Dia", "09:45:00", "10:33:59", "11:17:00", "12:05:59");
        crearHorario("Lun-Sab Dia", "09:52:00", "10:40:59", "11:24:00", "12:12:59");
        crearHorario("Lun-Sab Dia", "09:59:00", "10:47:59", "11:31:00", "12:19:59");
        crearHorario("Lun-Sab Dia", "10:06:00", "10:54:59", "11:38:00", "12:26:59");
        crearHorario("Lun-Sab Dia", "10:13:00", "11:01:59", "11:45:00", "12:33:59");
        crearHorario("Lun-Sab Dia", "10:20:00", "11:08:59", "11:52:00", "12:40:59");
        crearHorario("Lun-Sab Dia", "10:27:00", "11:15:59", "11:59:00", "12:47:59");
        crearHorario("Lun-Sab Dia", "10:34:00", "11:22:59", "12:06:00", "12:54:59");
        crearHorario("Lun-Sab Dia", "10:41:00", "11:29:59", "12:11:00", "13:01:59");
        crearHorario("Lun-Sab Dia", "10:48:00", "11:36:59", "12:20:00", "13:08:59");
        crearHorario("Lun-Sab Dia", "10:55:00", "11:43:59", "12:27:00", "13:15:59");
        crearHorario("Lun-Sab Dia", "11:02:00", "11:50:59", "12:34:00", "13:22:59");
        crearHorario("Lun-Sab Dia", "11:09:00", "11:57:59", "12:41:00", "13:29:59");
        crearHorario("Lun-Sab Dia", "11:16:00", "12:04:59", "12:48:00", "13:36:59");
        crearHorario("Lun-Sab Dia", "11:23:00", "12:11:59", "12:55:00", "13:43:59");
        crearHorario("Lun-Sab Dia", "11:30:00", "12:18:59", "13:02:00", "13:50:59");
        crearHorario("Lun-Sab Dia", "11:37:00", "12:25:59", "13:09:00", "13:57:59");
        crearHorario("Lun-Sab Dia", "11:44:00", "12:32:59", "13:16:00", "14:04:59");
        crearHorario("Lun-Sab Dia", "11:51:00", "12:39:59", "13:23:00", "14:11:59");
        crearHorario("Lun-Sab Dia", "11:58:00", "12:46:59", "13:30:00", "14:18:59");
        crearHorario("Lun-Sab Dia", "12:05:00", "12:53:59", "13:37:00", "14:25:59");
        crearHorario("Lun-Sab Dia", "12:12:00", "13:00:59", "13:44:00", "14:32:59");
        crearHorario("Lun-Sab Dia", "12:19:00", "13:07:59", "13:51:00", "14:39:59");
        crearHorario("Lun-Sab Dia", "12:26:00", "13:14:59", "13:58:00", "14:46:59");
        crearHorario("Lun-Sab Dia", "12:33:00", "13:21:59", "14:05:00", "14:53:59");
        crearHorario("Lun-Sab Dia", "12:40:00", "13:28:59", "14:12:00", "15:00:59");
        crearHorario("Lun-Sab Dia", "12:47:00", "13:35:59", "14:19:00", "15:07:59");
        crearHorario("Lun-Sab Dia", "12:54:00", "13:42:59", "14:26:00", "15:14:59");
        crearHorario("Lun-Sab Dia", "13:01:00", "13:49:59", "14:33:00", "15:21:59");
        crearHorario("Lun-Sab Dia", "13:08:00", "13:56:59", "14:40:00", "15:28:59");
        crearHorario("Lun-Sab Dia", "13:15:00", "14:03:59", "14:47:00", "15:35:59");
        crearHorario("Lun-Sab Dia", "13:22:00", "14:10:59", "14:54:00", "15:42:59"); /***/
        crearHorario("Lun-Sab Dia", "13:29:00", "14:17:59", "15:01:00", "15:49:59");
        crearHorario("Lun-Sab Dia", "13:36:00", "14:24:59", "15:08:00", "15:56:59");
        crearHorario("Lun-Sab Dia", "13:43:00", "14:31:59", "15:15:00", "16:04:59");
        crearHorario("Lun-Sab Dia", "13:50:00", "14:38:59", "15:22:00", "16:10:59");
        crearHorario("Lun-Sab Dia", "13:57:00", "14:45:59", "15:29:00", "16:17:59");
        crearHorario("Lun-Sab Dia", "14:04:00", "14:52:59", "15:36:00", "16:24:59");
        crearHorario("Lun-Sab Dia", "14:11:00", "14:59:59", "15:43:00", "16:31:59");
        crearHorario("Lun-Sab Dia", "14:18:00", "15:06:59", "15:50:00", "16:38:59");
        crearHorario("Lun-Sab Dia", "14:25:00", "15:13:59", "15:57:00", "16:45:59");

        crearHorario("Lun-Sab Dia", "14:32:00", "15:20:59", "16:04:00", "16:52:59");
        crearHorario("Lun-Sab Dia", "14:38:00", "15:26:59", "16:11:00", "16:59:59");
        crearHorario("Lun-Sab Dia", "14:44:00", "15:32:59", "16:18:00", "17:06:59");
        crearHorario("Lun-Sab Dia", "14:50:00", "15:38:59", "16:25:00", "17:13:59");
        crearHorario("Lun-Sab Dia", "14:56:00", "15:44:59", "16:32:00", "17:20:59");
        crearHorario("Lun-Sab Dia", "15:02:00", "15:50:59", "16:39:00", "17:27:59");
        crearHorario("Lun-Sab Dia", "15:08:00", "15:56:59", "16:46:00", "17:34:59");
        crearHorario("Lun-Sab Dia", "15:14:00", "16:02:59", "16:53:00", "17:41:59");
        crearHorario("Lun-Sab Dia", "15:20:00", "16:08:59", "17:00:00", "17:48:59");
        crearHorario("Lun-Sab Dia", "15:26:00", "16:14:59", "17:07:00", "17:55:59");
        crearHorario("Lun-Sab Dia", "15:32:00", "16:21:59", "17:14:00", "18:02:59");
        crearHorario("Lun-Sab Dia", "15:38:00", "16:27:59", "17:21:00", "18:09:59");
        crearHorario("Lun-Sab Dia", "15:44:00", "16:33:59", "17:28:00", "18:16:59");
        crearHorario("Lun-Sab Dia", "15:50:00", "16:39:59", "17:35:00", "18:23:59");
        crearHorario("Lun-Sab Dia", "15:56:00", "16:45:59", "17:42:00", "18:30:59");
        crearHorario("Lun-Sab Dia", "16:02:00", "16:51:59", "17:49:00", "18:37:59");
        crearHorario("Lun-Sab Dia", "16:08:00", "16:57:59", "17:56:00", "18:44:59");
        crearHorario("Lun-Sab Dia", "16:14:00", "17:03:59", "18:03:00", "18:51:59");
        crearHorario("Lun-Sab Dia", "16:20:00", "17:09:59", "18:10:00", "18:58:59");
        crearHorario("Lun-Sab Dia", "16:26:00", "17:15:59", "18:17:00", "19:05:59");
        crearHorario("Lun-Sab Dia", "16:32:00", "17:21:59", "18:24:00", "19:12:59");
        crearHorario("Lun-Sab Dia", "16:38:00", "17:27:59", "18:31:00", "19:19:59");
        crearHorario("Lun-Sab Dia", "16:44:00", "17:33:59", "18:38:00", "19:26:59");
        crearHorario("Lun-Sab Dia", "16:50:00", "17:39:59", "18:45:00", "19:33:59");
        crearHorario("Lun-Sab Dia", "16:56:00", "17:45:59", "18:52:00", "19:40:59");
        crearHorario("Lun-Sab Dia", "17:02:00", "17:51:59", "18:59:00", "19:47:59");
        crearHorario("Lun-Sab Dia", "17:08:00", "17:57:59", "19:06:00", "19:54:59");
        crearHorario("Lun-Sab Dia", "17:14:00", "18:03:59", "19:13:00", "20:01:59");
        crearHorario("Lun-Sab Dia", "17:20:00", "18:09:59", "19:20:00", "20:08:59");
        crearHorario("Lun-Sab Dia", "17:26:00", "18:15:59", "19:30:00", "20:18:59");
        crearHorario("Lun-Sab Dia", "17:32:00", "18:21:59", "19:45:00", "20:33:59");
        crearHorario("Lun-Sab Dia", "17:38:00", "18:27:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "17:44:00", "18:33:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "17:50:00", "18:39:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "17:56:00", "18:45:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "18:02:00", "18:51:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "18:08:00", "18:56:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "18:14:00", "19:02:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "18:20:00", "19:08:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "18:27:00", "19:15:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "18:34:00", "19:22:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "18:41:00", "19:29:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "18:48:00", "19:36:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "18:55:00", "19:43:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "19:02:00", "19:50:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "19:10:00", "19:58:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "19:20:00", "20:08:59", "00:00:00", "00:00:00");

        crearHorario("Lun-Sab Dia", "19:30:00", "20:18:59", "00:00:00", "00:00:00");
        crearHorario("Lun-Sab Dia", "19:45:00", "20:33:59", "00:00:00", "00:00:00");




    }

    public static void crearRutas(){
        crearRuta("Cajica - Zipa");
        crearRuta("Zipa - Cajica");
    }

    public static void asignarHorarios(){
        List<Ruta> lrutas = AppController.daoSession.getRutaDao().loadAll();
        List<Horario> lhorarios = AppController.daoSession.getHorarioDao().loadAll();
        for (Ruta rx : lrutas){
            for (Horario hr : lhorarios){
                asignarHorarioaRuta(rx, hr);
            }
        }
    }

    public static void asignarHorarioaRuta(Ruta r, Horario h){
        horarioPorRuta hr = new horarioPorRuta();
        hr.setEliminado(false);
        hr.setHorario(h);
        hr.setRuta(r);

        AppController.daoSession.getHorarioPorRutaDao().insert(hr);
        Log.d("START DATA", "Asignando horario a ruta: " + r.getNombre() + " < Horario: " + h.getNombre());
    }

    public static void crearHorario(String nombre, String desde, String hasta, String festivoDesde, String festivoHasta){
        Horario h = new Horario();
        h.setNombre(nombre);
        h.setEliminado(false);
        h.setHoraDesde(desde);
        h.setHoraHasta(hasta);
        h.setTiempoNormal(getDiferencia(desde, hasta));
        h.setMaxMinutos(3);

        h.setHoraFestivoDesde(festivoDesde);
        h.setHoraFestivoHasta(festivoHasta);
        h.setTiempoDiaFestivo(getDiferencia(festivoDesde, festivoHasta));
        h.setMaxMinutosFestivo(5);

        AppController.daoSession.getHorarioDao().insert(h);
        Log.d("START DATA", "Horario creado: " + nombre + " - desde: " + desde + " a " + hasta);
    }

    public static String getDiferencia(String desde, String hasta){
        Long diferencia = TimeHelper.calcularDiferencia(desde, hasta);
        return TimeHelper.segundosahoras(diferencia);
    }

    public static void crearRuta(String nombre){
        Ruta rx = new Ruta();
        rx.setEliminada(false);
        rx.setNombre(nombre);
        AppController.daoSession.getRutaDao().insert(rx);
        Log.d("START DATA", "Ruta creada: " + nombre);
    }

    private void generarHorarios(String desdeNormal, String desdeFestivo){
        for (int i=0; i<=144; i++){
            Long tdesdeN = TimeHelper.getTime(desdeNormal);
            Long thastaN = tdesdeN + (7*60);
            Long tdesdeF = TimeHelper.getTime(desdeFestivo);
            Long thastaF = tdesdeF + (9*60);

        }
    }
}
