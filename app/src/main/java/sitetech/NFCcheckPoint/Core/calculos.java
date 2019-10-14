package sitetech.NFCcheckPoint.Core;

import android.text.format.DateUtils;
import android.util.Log;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.db.DiaFestivo;
import sitetech.NFCcheckPoint.db.DiaFestivoDao;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.horarioPorRuta;
import sitetech.NFCcheckPoint.db.horarioPorRutaDao;

public class calculos {
    public static Horario getHorarioCercano(Ruta rx, Date fecha){
        String horarioRegistro = TimeHelper.getDate(fecha, "HH:mm:ss");
        Log.d("ALGO ", rx.getNombre());
        List<horarioPorRuta> hrl = AppController.daoSession.getHorarioPorRutaDao().queryBuilder() //Obtenemos los horarios de la ruta
                .where(horarioPorRutaDao.Properties.RutaId.eq(rx.getId()))
                .list();

        List<Horario> listaHorarios = new ArrayList();
        for ( horarioPorRuta hrx : hrl) { // Obtenemos la lista de horarios de esa ruta.
            listaHorarios.add(hrx.getHorario());
        }

        listaHorarios = ordenarLista(listaHorarios); // Ordenamos la lista
        //Log.d("VERIFICAR HORARIO", horarioRegistro);
        //Log.d("HORARIO hasta", obtenerCercano(listaHorarios, TimeHelper.getTime(horarioRegistro)).getHoraHasta());
        //Log.d("HORARIO hasta festivo", obtenerCercano(listaHorarios, TimeHelper.getTime(horarioRegistro)).getHoraFestivoHasta());

        return obtenerCercano(listaHorarios, TimeHelper.getTime(horarioRegistro));
    }

    public static void mostrarLista(List<Horario> l){
        for (int i=0; i<l.size(); i++)
            Log.d("LISTA " + i, l.get(i).getNombre() + " -- hasta " + l.get(i).getHoraHasta());
    }

    public static List<Horario>  ordenarLista(List<Horario> lista){
        for(int i=0;i<(lista.size()-1);i++){
            for(int j=i+1;j<lista.size();j++){
                if(TimeHelper.getTime(lista.get(i).getHoraHasta() ) > TimeHelper.getTime(lista.get(j).getHoraHasta() )){
                    Horario variableauxiliar = lista.get(i);
                    lista.set(i, lista.get(j));
                    lista.set(j, variableauxiliar);
                }
            }
        }
        return lista;
    }

    private static Horario obtenerCercano(List<Horario> lista, long hregistro) {
        Horario cercano = null;
        long diferencia = Integer.MAX_VALUE; //inicializado valor máximo de variable de tipo int
        for (int i = 0; i < lista.size(); i++) {
            long n = 0;

            if (TimeHelper.esFindeSemana(new Date()) || esDiaFestivo(new Date()))
                n = TimeHelper.getTime(lista.get(i).getHoraFestivoHasta());
            else
                n = TimeHelper.getTime(lista.get(i).getHoraHasta());

            if (n == hregistro) {
                return lista.get(i);
            } else {
                if(Math.abs(n-hregistro)<diferencia){
                    cercano=lista.get(i);
                    diferencia = Math.abs(n-hregistro);
                }
            }
        }
        return cercano;
    }

    public static boolean esDiaFestivo(Date date){
        //Log.d("CHECK DIA FESTIVO", TimeHelper.getDate(date));
        List<DiaFestivo> listDF = AppController.daoSession.getDiaFestivoDao().queryBuilder()
                .where(DiaFestivoDao.Properties.Eliminado.eq(false)).list();

        for ( DiaFestivo df : listDF) {
            if (compararFechas(date, df.getDia()))
                return true;
        }

        return false;
    }

    private static boolean compararFechas(Date d1, Date d2){
        if (d1.getMonth() == d2.getMonth())
            if (d1.getDay() == d2.getDay())
                return true;

        return false;
    }


}
