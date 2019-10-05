package sitetech.NFCcheckPoint.Helpers;

import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import sitetech.NFCcheckPoint.AppController;

public class ToastHelper {
    public static void normal(String mensaje){
        Toasty.normal(AppController.getAppContext(), mensaje).show();
    }

    public static void error(String mensaje){
        Toasty.error(AppController.getAppContext(), mensaje, Toast.LENGTH_SHORT, true).show();
    }

    public static void exito(String mensaje){
        Toasty.success(AppController.getAppContext(), mensaje, Toast.LENGTH_SHORT, true).show();
    }

    public static void info(String mensaje){
        Toasty.info(AppController.getAppContext(), mensaje, Toast.LENGTH_SHORT, true).show();
    }

    public static void aviso(String mensaje){
        Toasty.warning(AppController.getAppContext(), mensaje, Toast.LENGTH_SHORT, true).show();
    }
}
