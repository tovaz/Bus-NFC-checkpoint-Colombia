package sitetech.NFCcheckPoint;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import java.util.Date;

import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.TestDataHelper;
import sitetech.NFCcheckPoint.db.DaoMaster;
import sitetech.NFCcheckPoint.db.DaoSession;
import sitetech.NFCcheckPoint.db.Turno;
import sitetech.NFCcheckPoint.db.TurnoDao;

/**
 * Created by Akinsete on 1/14/16.
 */

public class AppController extends Application {
    public static final boolean ENCRYPTED = true;
    public static DaoSession daoSession;
    private static Context context;
    public static Configuraciones Configuracion;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"nfc-checkpoint-db-1.2"); //The users-db here is the name of our database.
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        Configuracion = new Configuraciones();
        //AgregarColumna(db, "HORARIO", "NOMBRE", "TEXT");
        AppController.context = getApplicationContext();
        checkTurno(); // PRIMORDIAL PARA CREAR EL PRIMER TURNO.

        if (esPrimerUso()) TestDataHelper.crearData();
    }

    public void AgregarColumna(Database db, String tabla, String columna, String tipo){
        Log.d("ALTERANDO TABLA " + tabla, "ALTERANDO TABLE " + tabla + ", AGREGANDO " + columna + " tipo " + tipo);
        db.execSQL("ALTER TABLE " + tabla + " ADD " + columna + " " + tipo);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static Context getAppContext() {
        return AppController.context;
    }

    private void checkTurno(){
        TurnoDao turnoManager = daoSession.getTurnoDao();
        if (turnoManager.loadAll().size() == 0){
            Turno turno = new Turno();
            turno.setFechaCreacion(new Date());
            turno.setEliminada(false);
            turno.setFechaCierre(null);
            turnoManager.insert(turno);
        }
    }

    private boolean esPrimerUso(){
        if (daoSession.getUsuarioDao().loadAll().size() == 0) return true;
        return false;
    }
}