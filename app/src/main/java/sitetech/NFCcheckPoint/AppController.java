package sitetech.NFCcheckPoint;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import sitetech.NFCcheckPoint.db.DaoMaster;
import sitetech.NFCcheckPoint.db.DaoSession;
import sitetech.NFCcheckPoint.db.HorarioDao;

/**
 * Created by Akinsete on 1/14/16.
 */

public class AppController extends Application {
    public static final boolean ENCRYPTED = true;
    public static DaoSession daoSession;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"nfc-checkpoint-db-1.1"); //The users-db here is the name of our database.
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        //AgregarColumna(db, "HORARIO", "NOMBRE", "TEXT");
        AppController.context = getApplicationContext();
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
}