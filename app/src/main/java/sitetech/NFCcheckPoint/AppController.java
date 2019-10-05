package sitetech.NFCcheckPoint;

import android.app.Application;
import android.content.Context;

import org.greenrobot.greendao.database.Database;

import sitetech.NFCcheckPoint.db.DaoMaster;
import sitetech.NFCcheckPoint.db.DaoSession;

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

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"nfc-checkpoint-db"); //The users-db here is the name of our database.
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        AppController.context = getApplicationContext();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static Context getAppContext() {
        return AppController.context;
    }
}