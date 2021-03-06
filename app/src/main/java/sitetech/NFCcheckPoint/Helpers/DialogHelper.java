package sitetech.NFCcheckPoint.Helpers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

public class DialogHelper {
    public static synchronized boolean showAsk(View vista, String titulo, String mensaje, String boton1, String boton2){
        final boolean[] respuesta = new boolean[1];
        AlertDialog.Builder alert = new AlertDialog.Builder(vista.getContext());
        alert.setTitle(titulo);
        alert.setMessage(mensaje);

        alert.setPositiveButton(boton1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                respuesta[0] = true;
                notify();
            }
        });

        alert.setNegativeButton(boton2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                respuesta[0] = false;
                notify();
            }
        });

        alert.show();

        return respuesta[0];
    }

    public static void showAsk2(View vista, String titulo, String mensaje, String boton1, String boton2, final myDialogInterface dginterface) {
        AlertDialog.Builder alert = new AlertDialog.Builder(vista.getContext());
        alert.setTitle(titulo);
        alert.setMessage(mensaje);
        // build the dialog
        final View v = dginterface.onBuildDialog();
        // put the view obtained from the interface into the dialog
        if (v != null) { alert.setView(v);}
        // procedure for when the ok button is clicked.
        alert.setPositiveButton(boton1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // ** HERE IS WHERE THE MAGIC HAPPENS! **
                dginterface.onResult(v);
                dialog.dismiss();
                return;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dginterface.onCancel();
                dialog.dismiss();
                return;
            }
        });
        alert.show();
    }

    public static void showAlert(View v, String titulo, String mensaje){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
        builder1.setTitle(titulo);
        builder1.setMessage(mensaje);
        builder1.setCancelable(true);

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
