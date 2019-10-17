package sitetech.NFCcheckPoint.dbHelpers;

import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.db.Registro_Turno;
import sitetech.NFCcheckPoint.db.Registro_TurnoDao;
import sitetech.NFCcheckPoint.db.Turno;

public class RegistrosManager {
    public static List<Registro_Turno> getUltimosRegistros(){
        Turno tn = Configuraciones.getTurnoAbierto();
        List<Registro_Turno> ultimos2 = AppController.daoSession.getRegistro_TurnoDao().queryBuilder()
                .where(Registro_TurnoDao.Properties.TurnoId.eq(tn.getId())).limit(2).orderDesc(Registro_TurnoDao.Properties.Id).list();
        return ultimos2;
    }

    public static Registro_Turno getUltimoRegistro(){
        List<Registro_Turno> lr = getUltimosRegistros();
        try {
            if (lr.size() > 0)
                return lr.get(0);
        }
        catch (Exception e){
            ToastHelper.error("ULTIMO: " + e.getMessage());
            return null;
        }

        return null;
    }

    public static Registro_Turno getRegistroAnterior(){
        List<Registro_Turno> lr = getUltimosRegistros();
        try {
            if (lr.size() > 1 )
                return lr.get(1);
        }
        catch (Exception e){
            ToastHelper.error("ANTERIOR: " + e.getMessage()); return null;
        }
        return null;
    }
}
