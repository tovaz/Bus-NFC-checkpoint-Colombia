package sitetech.NFCcheckPoint.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.RutaDao;
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

    public static void setRutaDefault(Context context, Ruta rx){
        SharedPreferences sharedPref = context.getSharedPreferences("confApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("rutaDefault", rx.getId());
        editor.commit();
    }

    public static Ruta getRutaDefault(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("confApp", Context.MODE_PRIVATE);
        Long id = sharedPref.getLong("rutaDefault", -1);
        if (id != -1) {
            return AppController.daoSession.getRutaDao().queryBuilder().where(RutaDao.Properties.Id.eq(id)).unique();
        }

        return null;
    }

    public static String getUltimoTag(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("confApp", Context.MODE_PRIVATE);
        String tag = sharedPref.getString("ultimoTag", "-1");
        return tag;
    }

    public static void setUltimoTag(Context context, String tag){
        SharedPreferences sharedPref = context.getSharedPreferences("confApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ultimoTag", tag);
        editor.commit();
    }

}
