package sitetech.NFCcheckPoint.Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.horarioPorRuta;
import sitetech.NFCcheckPoint.db.horarioPorRutaDao;

public class calculos {
    public static Horario getHorarioCercano(Ruta rx, Date fecha){
        String horarioRegistro = TimeHelper.getDate(fecha, "HHmmss");
        List<horarioPorRuta> hrl = AppController.daoSession.getHorarioPorRutaDao().queryBuilder() //Obtenemos los horarios de la ruta
                .where(horarioPorRutaDao.Properties.RutaId.eq(rx.getId()))
                .list();

        List<Horario> listaHorarios = new ArrayList();
        for ( horarioPorRuta hrx : hrl) { // Obtenemos la lista de horarios de esa ruta.
            listaHorarios.add(hrx.getHorario());
        }

        Comparator<Horario> compareByHour = new Comparator<Horario>() { //Comparador
            @Override
            public int compare(Horario h1, Horario h2) {
                return h1.getHoraDesde().compareTo(h2.getHoraDesde());
            }
        };

        Collections.sort(listaHorarios, compareByHour); // Ordenamos la lista de horarios.

        Horario horarioCercano = null;
        for ( horarioPorRuta hrx : hr){  //Calculamos el horario mas cercano
            String horaHasta = hrx.getHorario().getHoraHasta();
            Horario h = hrx.getHorario();
            if (horarioCercano == null)
                horarioCercano = hrx.getHorario();
            else {
                Long diferencia = TimeHelper.calcularDiferencia(h.getHoraHasta(), horarioRegistro);
                Long diferenciaAnterior = TimeHelper.calcularDiferencia(horarioCercano.getHoraHasta(), horarioRegistro);
                if (diferencia > 0) {//OK
                    if (diferencia < diferenciaAnterior)
                        horarioCercano = hrx.getHorario();
                }
                else
                    return horarioCercano;
            }
        }

        return null;
    }


}
