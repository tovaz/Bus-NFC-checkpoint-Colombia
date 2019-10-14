package sitetech.NFCcheckPoint.Helpers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import sitetech.NFCcheckPoint.Core.BluetoothPrinter;
import sitetech.NFCcheckPoint.db.Registro_Turno;

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
                    //limpiarInfo();
                    //bimprimir.setVisibility(View.GONE);
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
        if (esPrimero) {
            //Bitmap logoEmpresa = BitmapFactory.decodeResource(getResources(), R.drawable.logo_empresa2);
            impresora.setAlign(BluetoothPrinter.ALIGN_LEFT);
            //impresora.printImage(logoEmpresa);
            //impresora.addNewLine();

            impresora.printLine();
            impresora.addNewLine();

            impresora.setBold(true);
            impresora.printText("PUNTO DE CONTROL");
            impresora.addNewLine();


            impresora.setBold(false);
            impresora.printText("Nombre del punto");
            impresora.addNewLine();
            //impresora.setAlign(BluetoothPrinter.ALIGN_LEFT);
            //impresora.setBold(false);

            //impresora.printText(registro.getRuta().getNombre().toUpperCase());

            impresora.setBold(true);
            imprimirValor(impresora, "TIEMPO AL PUNTO ", registro.getDespacho());
            impresora.addNewLine();

            impresora.printText("DETALLE DE CONTROL");
        }
        else {
            impresora.setBold(true);
            impresora.setAlign(BluetoothPrinter.ALIGN_LEFT);
            impresora.printText("VEHICULO ANTERIOR");
        }

        if (reimpresion) {
            impresora.setAlign(BluetoothPrinter.ALIGN_RIGHT);
            impresora.printText("reimpresion");
        }

        impresora.setBold(false);
        //impresora.setLineSpacing(1);
        impresora.addNewLine();

        String empresa = "ninguna";
        try {
            empresa = registro.getBus().getEmpresa().getNombre().toUpperCase();
        } catch (Exception e){ empresa = "Error entity detached"; }

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


        impresora.setAlign(BluetoothPrinter.ALIGN_CENTER);
        impresora.addNewLine();
        impresora.printText("OPERARIO");
        impresora.addNewLine();
        impresora.printText(registro.getUsuario().getNombre().toUpperCase());


        impresora.printLine();
        impresora.feedPaper();

        impresora.finish();
    }

    public static void imprimirValor(BluetoothPrinter print, String campo, String valor){
        print.writePrint(BluetoothPrinter.ESC_ALIGN_CENTER, "| " + campo + " : " + valor +" |");
        print.setAlign(BluetoothPrinter.ALIGN_LEFT);
    }
}
