package sitetech.NFCcheckPoint.Helpers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Set;

public class checkHelper {
    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public static boolean isNfcEnable(Context context){
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);

        if (nfcAdapter == null) {
            return false;
        } else if (!nfcAdapter.isEnabled()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean haveNfc(Context context){
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (nfcAdapter == null)
            return false;
        else
            return true;
    }

    public static boolean isPrinterConnected(){

        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice d: pairedDevices) {
                String deviceName = d.getName();
                String macAddress = d.getAddress();
                Log.i("Dispositivos conectados", "paired device: " + deviceName + " at " + macAddress);
                if (isAPrinter(d)) return true;
            }
        }

        return true;
        //return false;
    }

    private static boolean isAPrinter(BluetoothDevice device){
        // int priterMask = 0b000001000000011010000000; // HAbilitar para impresoras
        int priterMask = 263168;
        int fullCod = device.getBluetoothClass().hashCode();
        Log.d("UUID", "FULL COD: " + fullCod);
        Log.d("UUID","MASK RESULT " + (fullCod & priterMask));
        return (fullCod & priterMask) == priterMask;
    }
}
