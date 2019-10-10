package sitetech.NFCcheckPoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import sitetech.NFCcheckPoint.Adapters.TabsAdapter;
import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.Dialog;
import sitetech.NFCcheckPoint.Helpers.Listener;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.checkHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.Helpers.nfcData;
import sitetech.NFCcheckPoint.Helpers.nfcHelper;
import sitetech.NFCcheckPoint.db.Bus;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.db.UsuarioDao;
import sitetech.NFCcheckPoint.ui.nfc.NFCReadFragment;
import sitetech.NFCcheckPoint.ui.nfc.NFCWriteFragment;
import sitetech.NFCcheckPoint.ui.operador.CheckFragment;
import sitetech.NFCcheckPoint.ui.operador.HistoryFragment;
import sitetech.routecheckapp.R;

public class OperadorActivity extends AppCompatActivity implements Listener {

    public Usuario usuarioLog;
    private TextView tusuario;
    private Button blogout;

    private int mInterval = 2000; // 2 seconds by default, can be changed later
    private Handler mHandler;

    private NfcAdapter mNfcAdapter;
    private NFCWriteFragment nfcWriteF;
    private NFCReadFragment nfcReadF;
    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operador);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //ACTIVAR ACTION BAR

        tusuario = findViewById(R.id.tusuario);
        blogout = findViewById(R.id.blogout);


        cargarTabs();
        cargarUsuario();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
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


    boolean isCheckActivity = false;
    private void checking(){
        if (checkHelper.isNfcEnable(this) && checkHelper.isTimeAutomatic(this) && checkHelper.isPrinterConnected()) {
            isCheckActivity = false;
            finishActivity(2);
        }
        else {
            if (!isCheckActivity) {
                Intent intent = new Intent(this, LockActivity.class);
                startActivityForResult(intent, 2);
                isCheckActivity = true;
            }
        }
    }

    void startChecking() {
        CheckerTime.run();
    }

    void stopChecking() {
        mHandler.removeCallbacks(CheckerTime);
    }

    private TabsAdapter tabsAdapter;
    private  void cargarTabs(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager);
        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                int color = ContextCompat.getColor(getBaseContext(), R.color.colorAccent);
                tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);

                if (tab.getPosition() == 1) {
                    HistoryFragment hf = (HistoryFragment) tabsAdapter.getItem(tab.getPosition());
                    hf.cargarLista();
                }

                ToastHelper.aviso("TAB SELECCIONADA " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int color = ContextCompat.getColor(getBaseContext(), R.color.colorAccentDark);
                tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //tabLayout.getTabAt(0).select();
    }

    public void cargarUsuario(){
        long uid = getIntent().getLongExtra("usuario", 0);
        Usuario ux = AppController.daoSession.getUsuarioDao().queryBuilder().where(UsuarioDao.Properties.Id.eq(uid)).unique();
        if (ux != null) {
            usuarioLog = ux;
            tusuario.setText(usuarioLog.getNombre().toString());
        }

        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.showAsk2(v, "Cerrar Sesion", "¿Quiere cerrar sesion ahora?", "Cerrar ahora", "Cancelar", new myDialogInterface() {
                    @Override
                    public View onBuildDialog() {  return null; }

                    @Override
                    public void onCancel() { }

                    @Override
                    public void onResult(View vista) {
                        finish();
                    }
                });

            }
        });
    }


    @Override
    public void onDestroy() { // AL FINALIZAR LA ACTIVIDAD
        super.onDestroy();
        stopChecking();
    }

    /***************************************************************************/
    private Bus busNFC;
    public void escribirNFC(Bus bus){
        isWrite = true;
        busNFC = bus;
        nfcWriteF = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);

        if (nfcWriteF == null) {
            nfcWriteF = NFCWriteFragment.newInstance();
        }

        nfcWriteF.show(getFragmentManager(),NFCWriteFragment.TAG);
    }

    public void leerNFC() {
        nfcReadF = (NFCReadFragment) getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);

        if (nfcReadF == null) {
            nfcReadF = NFCReadFragment.newInstance();
        }
        nfcReadF.show(getFragmentManager(), NFCReadFragment.TAG);
    }

    public void updateCheckFragment(String tag){
        CheckFragment chf = (CheckFragment)tabsAdapter.getItem(0);
        if (chf != null)
            chf.callBackNfc(tag);
    }

    @Override
    public void onDialogDisplayed() {
        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {
        isDialogDisplayed = false;
        isWrite = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d("NEW INTENT", "on New Intent: "+intent.getAction());

        if(tag != null) {
            Toast.makeText(this, "Tarjeta detectada.", Toast.LENGTH_SHORT).show();
            //ToastHelper.aviso(tag.toString());
            Ndef ndef = Ndef.get(tag);

            if (isDialogDisplayed) {
                if (isWrite) {
                    nfcData nfcdata = new nfcData(busNFC);
                    String messageToWrite = nfcHelper.convertnfcData(nfcdata);
                    nfcWriteF = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);
                    nfcWriteF.onNfcDetected(ndef,messageToWrite);

                } else {
                    nfcReadF = (NFCReadFragment)getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);

                    nfcReadF.onNfcDetected(ndef);
                }
            }
        }
    }
}
