package sitetech.NFCcheckPoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.Helpers.checkHelper;
import sitetech.routecheckapp.R;

public class LockActivity extends AppCompatActivity {
    private Button bnfc;
    private Button bfecha;
    private Button bimpresora;
    private TextView tinfo;

    private int mInterval = 2000; // 2 seconds by default, can be changed later
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        bnfc = findViewById(R.id.bnfc);
        bfecha = findViewById(R.id.bfecha);
        bimpresora = findViewById(R.id.bimpresora);
        tinfo = findViewById(R.id.tinfo);

        Drawable checkImg = getResources().getDrawable( R.drawable.check_24x24 );
        mHandler = new Handler();
        startChecking();
    }

    private void Necesita(){
        boolean needNFC = getPreferences(MODE_PRIVATE).getBoolean("nfc", true);
        boolean needBT = getPreferences(MODE_PRIVATE).getBoolean("impresora", true);
        boolean needFecha = getPreferences(MODE_PRIVATE).getBoolean("fecha", true);

        if (!needNFC) bnfc.setVisibility(View.GONE); else bnfc.setVisibility(View.VISIBLE);
        if (!needBT) bimpresora.setVisibility(View.GONE); else bimpresora.setVisibility(View.VISIBLE);
        if (!needFecha) bfecha.setVisibility(View.GONE); else bfecha.setVisibility(View.VISIBLE);
    }

    Runnable CheckerTime = new Runnable() {
        @Override
        public void run() {
            try {
                checking(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(CheckerTime, mInterval);
            }
        }
    };


    private void checking(){
        Necesita();
        if (checkHelper.verificarNFC(this) && checkHelper.verificarImpresora(this) && checkHelper.verificarFecha(this))

            if (TimeHelper.yaExpiro())
                tinfo.setText("DEBE DE COMPRAR LA LICENCIA DEL PROGRAMA ...");
            else
                finish();

        else {
            if (checkHelper.isNfcEnable(this)) bnfc.setBackground(getResources().getDrawable(R.drawable.btn_checked));
            else bnfc.setBackground(getResources().getDrawable(R.drawable.btn_default));

            if (checkHelper.isTimeAutomatic(this)) bfecha.setBackground(getResources().getDrawable(R.drawable.btn_checked));
            else bfecha.setBackground(getResources().getDrawable(R.drawable.btn_default));

            if (checkHelper.isPrinterConnected()) bimpresora.setBackground(getResources().getDrawable(R.drawable.btn_checked));
            else bimpresora.setBackground(getResources().getDrawable(R.drawable.btn_default));
        }
    }

    void startChecking() {
        CheckerTime.run();
    }

    void stopChecking() {
        mHandler.removeCallbacks(CheckerTime);
    }

    @Override
    public void onDestroy() { // AL FINALIZAR LA ACTIVIDAD
        super.onDestroy();
        stopChecking();
    }

    @Override
    public void finish() {
        return ;
    }
}
