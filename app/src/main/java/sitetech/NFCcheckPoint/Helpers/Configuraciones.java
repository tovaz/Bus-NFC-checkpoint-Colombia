package sitetech.NFCcheckPoint.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.db.Turno;
import sitetech.NFCcheckPoint.db.TurnoDao;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.db.UsuarioDao;

public class Configuraciones{
    public static Usuario getUsuarioLog(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("confApp", Context.MODE_PRIVATE);
        Long id = sharedPref.getLong("usuarioId", -1);
        if (id != -1) {
            return AppController.daoSession.getUsuarioDao().queryBuilder().where(UsuarioDao.Properties.Id.eq(id)).unique();
        }

        return null;
    }

    public static void setUsuarioLog(Context context, Usuario ux){
        SharedPreferences sharedPref = context.getSharedPreferences("confApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("usuarioId", ux.getId());
        editor.commit();
    }

    public static Turno getTurnoAbierto(){
        return AppController.daoSession.getTurnoDao().queryBuilder().where(TurnoDao.Properties.FechaCierre.isNull()).unique();
    }
}
