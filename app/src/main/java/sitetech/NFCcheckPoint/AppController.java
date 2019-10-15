package sitetech.NFCcheckPoint;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sitetech.NFCcheckPoint.Core.calculos;
import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.TestDataHelper;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
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

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"nfc-checkpoint-db-1.4.5"); //The users-db here is the name of our database.
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        Configuracion = new Configuraciones();
        //AgregarColumna(db, "HORARIO", "NOMBRE", "TEXT");
        AppController.context = getApplicationContext();
        checkTurno(); // PRIMORDIAL PARA CREAR EL PRIMER TURNO.

        if (esPrimerUso()) TestDataHelper.crearData();

        testFunction();
    }

    public void testFunction(){
        /*Log.d("TIEMPO CALCULO", TimeHelper.calcularDiferencia("10:50:00", "13:05:00").toString());
        Log.d("TIEMPO CALCULO", TimeHelper.calcularDiferencia("11:25:00", "13:05:00").toString());
        Log.d("TIEMPO CALCULO", TimeHelper.calcularDiferencia("13:00:00", "13:05:00").toString());
        Log.d("TIEMPO CALCULO", TimeHelper.calcularDiferencia("13:35:00", "13:05:00").toString());
        Log.d("TIEMPO CALCULO", TimeHelper.calcularDiferencia("13:45:00", "13:05:00").toString());
        */
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        c.add(Calendar.DATE, +1);
        Date nowMinus15 = c.getTime();

        c.add(Calendar.DATE, +1);
        Date now2 = c.getTime();

        c.add(Calendar.DATE, +1);
        Date now3 = c.getTime();

        /*Log.d("ES DIA FESTIVO", String.valueOf(calculos.esDiaFestivo(new Date())));
        Log.d("ES DIA FESTIVO MAÑANA", String.valueOf(calculos.esDiaFestivo(nowMinus15)));
        Log.d("ES DIA FESTIVO PASADO MA", String.valueOf(calculos.esDiaFestivo(now2)));*/

        Log.d("DIFERENCIA TIEMPO ", TimeHelper.getStringDiferencia("10:13:25", "10:30:12"));

        try {
            Date date1 = new SimpleDateFormat("HH:mm:ss").parse("09:41:00");
            calculos.getHorarioCercano(daoSession.getRutaDao().loadAll().get(0), date1);
        }catch (Exception e){}



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

            turno.setTotalBuses(new Long(0));
            turno.setTotalAtiempo(0);
            turno.setTotalAdelantados(0);
            turno.setTotalDemorados(0);

            turnoManager.insert(turno);
        }
    }

    private boolean esPrimerUso(){
        if (daoSession.getUsuarioDao().loadAll().size() == 0) return true;
        return false;
    }
}