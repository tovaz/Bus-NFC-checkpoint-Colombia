package sitetech.NFCcheckPoint;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class miServicio extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
    }
    public void onStart(Intent intent, int startId){
        System.out.println("El servicio a Comenzado");
        this.stopSelf();
    }
    public void onDestroy(){
        super.onDestroy();
        System.out.println("El servicio a Terminado");
    }

    public Context getContext(){
        return this.getApplicationContext();
    }
}
