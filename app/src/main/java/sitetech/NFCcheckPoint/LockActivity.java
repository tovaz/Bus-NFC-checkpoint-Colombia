package sitetech.NFCcheckPoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import sitetech.NFCcheckPoint.Helpers.checkHelper;
import sitetech.routecheckapp.R;

public class LockActivity extends AppCompatActivity {

    private Button bnfc;
    private Button bfecha;
    private Button bimpresora;

    private int mInterval = 2000; // 2 seconds by default, can be changed later
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        bnfc = findViewById(R.id.bnfc);
        bfecha = findViewById(R.id.bfecha);
        bimpresora = findViewById(R.id.bimpresora);

        Drawable checkImg = getResources().getDrawable( R.drawable.check_24x24 );
        mHandler = new Handler();
        startChecking();
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
        if (checkHelper.isNfcEnable(this) && checkHelper.isTimeAutomatic(this) && checkHelper.isPrinterConnected())
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
