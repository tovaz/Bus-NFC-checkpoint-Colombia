package sitetech.NFCcheckPoint;

import android.app.Application;
import org.greenrobot.greendao.database.Database;

import sitetech.NFCcheckPoint.db.DaoMaster;
import sitetech.NFCcheckPoint.db.DaoSession;

/**
 * Created by Akinsete on 1/14/16.
 */

public class AppController extends Application {
    public static final boolean ENCRYPTED = true;
    public static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"nfc-checkpoint-db"); //The users-db here is the name of our database.
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        ///// Using the below lines of code we can toggle ENCRYPTED to true or false in other to use either an encrypted database or not.
      //DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "NFCcheckPoint-encripted" : "NFCcheckPoint");
      //Database db = ENCRYPTED ? helper.getEncryptedWritableDb("correr123") : helper.getWritableDb();
      //daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}