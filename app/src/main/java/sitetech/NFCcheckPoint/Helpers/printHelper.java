package sitetech.NFCcheckPoint.Helpers;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

import sitetech.NFCcheckPoint.Core.BluetoothPrinter;
import sitetech.NFCcheckPoint.Core.Utils;
import sitetech.NFCcheckPoint.db.Registro_Turno;
import sitetech.routecheckapp.R;

import static sitetech.NFCcheckPoint.Core.BluetoothPrinter.ALIGN_CENTER;
import static sitetech.NFCcheckPoint.Core.BluetoothPrinter.decodeBitmap;

public class printHelper {

    public static boolean imprimirRegistro(final Registro_Turno ultimoRegistro, final boolean esPrimero, final boolean reimpresion){
        final boolean[] result = {false};
        try {
            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice mBtDevice = btAdapter.getBondedDevices().iterator().next();   // Get first paired device

            final BluetoothPrinter mPrinter = new BluetoothPrinter(mBtDevice);

            mPrinter.connectPrinter(new BluetoothPrinter.PrinterConnectListener() {

                @Override
                public void onConnected() {
                    imprimir(mPrinter, ultimoRegistro, esPrimero, reimpresion);
                    ToastHelper.exito("Registro impreso correctamente.");
                    result[0] = true;
                }

                @Override
                public void onFailed() {
                    ToastHelper.error("Error al enviar la impresion, verifica la impresora.");
                    Log.d("Impresora bluetooth", "Error de conexion");
                }

            });
        }
        catch (Exception e){
            ToastHelper.error("Error al intentar imprimir. " + e.getMessage());
        }

        return result[0];
    }

    private static void imprimir(BluetoothPrinter impresora, Registro_Turno registro, boolean esPrimero, boolean reimpresion){
        imprimirEncabezado(impresora, registro, esPrimero, reimpresion);
        imprimirTopRegistro(impresora, registro, esPrimero, reimpresion);

        if (esPrimero)
            imprimirVehiculo(impresora, registro);
        else
            imprimirVehiculoAnterior(impresora, registro);

        imprimirPie(impresora, registro, esPrimero);
    }

    private static void imprimirEncabezado(BluetoothPrinter impresora, Registro_Turno registro, boolean esPrimero, boolean reimpresion){
        if (esPrimero) {
            //Bitmap logoEmpresa = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.empresa_logox24);
            //impresora.setAlign(BluetoothPrinter.ALIGN_LEFT);
            //impresora.printImage(logoEmpresa);
            //impresora.addNewLine();
        }


        impresora.setBold(true);
        impresora.printText("PUNTO DE CONTROL");
        impresora.addNewLine();


        impresora.setBold(false);
        impresora.printText("Nombre del punto");
        impresora.addNewLine();
    }

    private static void imprimirTopRegistro(BluetoothPrinter impresora, Registro_Turno registro, boolean esPrimero, boolean reimpresion){
        if (registro != null)
            imprimirValor(impresora, "TIEMPO AL PUNTO ", registro.getDespacho());
        else
            imprimirValor(impresora, "TIEMPO AL PUNTO ", " ");

        impresora.addNewLine();
        if (reimpresion){
            impresora.setAlign(BluetoothPrinter.ALIGN_RIGHT);
            impresora.printText("** reimpresion **");
            impresora.addNewLine();
        }

        if (esPrimero)
            impresora.printText("DETALLE DE CONTROL");
    }

    private static void imprimirVehiculoAnterior(BluetoothPrinter impresora, Registro_Turno registro){
        impresora.setBold(true);
        impresora.setAlign(BluetoothPrinter.ALIGN_LEFT);
        impresora.printText("VEHICULO ANTERIOR");

        impresora.setBold(false);
        imprimirVehiculo(impresora, registro);
    }

    private static void imprimirVehiculo(BluetoothPrinter impresora, Registro_Turno registro){
        if (registro != null) {
            String empresa = "ninguna";
            try {
                empresa = registro.getBus().getEmpresa().getNombre().toUpperCase();
            } catch (Exception e) {
                empresa = "Error entity detached";
            }

            impresora.addNewLine();
            imprimirValor(impresora, "EMPRESA ", empresa);
            impresora.addNewLine();
            imprimirValor(impresora, "PLACA ", registro.getBus().getPlaca().toUpperCase());
            impresora.addNewLine();
            imprimirValor(impresora, "INTERNO ", registro.getBus().getInterno().toUpperCase());
            impresora.addNewLine();
            imprimirValor(impresora, "FECHA ", TimeHelper.getDate(registro.getFecha()));
            impresora.addNewLine();
            imprimirValor(impresora, "HORA DESPACHO ", registro.getDespacho());
            impresora.addNewLine();
            imprimirValor(impresora, "HORA REGISTRO ", TimeHelper.getTime(registro.getFecha()));

            impresora.addNewLine();
            if (registro.getMinAdelantado().equals("00:00:00"))
                imprimirValor(impresora, "Demorado ", registro.getMinAtrazado());
            else
                imprimirValor(impresora, "Adelantado ", registro.getMinAdelantado());
        }
        else {
            imprimirValor(impresora, "EMPRESA ", " ");
            impresora.addNewLine();
            imprimirValor(impresora, "PLACA ", " ");
            impresora.addNewLine();
            imprimirValor(impresora, "INTERNO ", " ");
            impresora.addNewLine();
            imprimirValor(impresora, "FECHA ", " ");
            impresora.addNewLine();
            imprimirValor(impresora, "HORA DESPACHO ", " ");
            impresora.addNewLine();
            imprimirValor(impresora, "HORA REGISTRO ", " ");
        }
    }

    public static void imprimirPie(BluetoothPrinter impresora, Registro_Turno registro, boolean esPrimero){
        impresora.setAlign(BluetoothPrinter.ALIGN_CENTER);
        impresora.addNewLine();
        impresora.printText("OPERARIO");
        impresora.addNewLine();
        impresora.printText(registro.getUsuario().getNombre().toUpperCase());
        impresora.addNewLine();

        impresora.printLine();
        impresora.feedPaper();

        impresora.finish();
    }
    /****************************************************************************************************/

    public static void imprimirValor(BluetoothPrinter print, String campo, String valor){
        print.writePrint(BluetoothPrinter.ESC_ALIGN_CENTER, "| " + campo + " : " + valor +" |");
        print.setAlign(BluetoothPrinter.ALIGN_LEFT);
    }

    private static void imprimirRegistro(Registro_Turno registro){
        
    }

    /*******************************************************************************************/
    public static void pruerba1(final Activity ac){
        try {
            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice mBtDevice = btAdapter.getBondedDevices().iterator().next();   // Get first paired device

            final BluetoothPrinter mPrinter = new BluetoothPrinter(mBtDevice);

            mPrinter.connectPrinter(new BluetoothPrinter.PrinterConnectListener() {

                @Override
                public void onConnected() {
                    try {
                        Bitmap bmp = BitmapFactory.decodeResource(ac.getResources(),
                                R.drawable.empresa_logox24);
                        if(bmp!=null){
                            byte[] command = Utils.decodeBitmap(bmp);
                            mPrinter.setAlign(ALIGN_CENTER);
                            mPrinter.printUnicode(command);
                            mPrinter.feedPaper();
                            mPrinter.finish();
                        }else{
                            ToastHelper.exito("Error al imprimir la foto " + " no existe el archivo");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastHelper.exito("Catch Exception " + e.getMessage());
                    }
                }

                @Override
                public void onFailed() {
                    ToastHelper.error("Error al enviar la impresion, verifica la impresora.");
                    Log.d("Impresora bluetooth", "Error de conexion");
                }

            });
        }
        catch (Exception e){
            ToastHelper.error("Error al intentar imprimir. " + e.getMessage());
        }

    }

    public static void pruerba2(final AppCompatActivity ax){
        try {
            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice mBtDevice = btAdapter.getBondedDevices().iterator().next();   // Get first paired device

            final BluetoothPrinter mPrinter = new BluetoothPrinter(mBtDevice);

            mPrinter.connectPrinter(new BluetoothPrinter.PrinterConnectListener() {

                @Override
                public void onConnected() {
                    try {
                        Bitmap bmp = BitmapFactory.decodeResource(ax.getResources(),
                                R.drawable.logo_empresa2);
                        if(bmp!=null){
                            byte[] command = Utils.decodeBitmap(bmp);
                            mPrinter.setAlign(ALIGN_CENTER);
                            mPrinter.printUnicode(command);
                            mPrinter.feedPaper();
                            mPrinter.finish();
                        }else{
                            ToastHelper.exito("Error al imprimir la foto " + " no existe el archivo");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastHelper.exito("Catch Exception " + e.getMessage());
                    }
                }

                @Override
                public void onFailed() {
                    ToastHelper.error("Error al enviar la impresion, verifica la impresora.");
                    Log.d("Impresora bluetooth", "Error de conexion");
                }

            });
        }
        catch (Exception e){
            ToastHelper.error("Error al intentar imprimir. " + e.getMessage());
        }

    }

    public static void pruerba3(final AppCompatActivity ax){
        try {
            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice mBtDevice = btAdapter.getBondedDevices().iterator().next();   // Get first paired device

            final BluetoothPrinter mPrinter = new BluetoothPrinter(mBtDevice);

            mPrinter.connectPrinter(new BluetoothPrinter.PrinterConnectListener() {

                @Override
                public void onConnected() {
                    try {
                        Bitmap bmp = BitmapFactory.decodeResource(ax.getResources(),
                                R.drawable.empresa_logo_100);
                        if(bmp!=null){
                            byte[] command = Utils.decodeBitmap(bmp);
                            mPrinter.setAlign(ALIGN_CENTER);
                            mPrinter.printUnicode(command);
                            mPrinter.feedPaper();
                            mPrinter.finish();
                        }else{
                            ToastHelper.exito("Error al imprimir la foto " + " no existe el archivo");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastHelper.exito("Catch Exception " + e.getMessage());
                    }
                }

                @Override
                public void onFailed() {
                    ToastHelper.error("Error al enviar la impresion, verifica la impresora.");
                    Log.d("Impresora bluetooth", "Error de conexion");
                }

            });
        }
        catch (Exception e){
            ToastHelper.error("Error al intentar imprimir. " + e.getMessage());
        }

    }

    public static void pruerba4(final AppCompatActivity ax){


    }
}
