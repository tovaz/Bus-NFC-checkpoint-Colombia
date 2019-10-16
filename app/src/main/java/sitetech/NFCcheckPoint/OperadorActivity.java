package sitetech.NFCcheckPoint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import sitetech.NFCcheckPoint.Adapters.TabsAdapter;
import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.DialogHelper;
import sitetech.NFCcheckPoint.Helpers.Listener;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.Helpers.checkHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.db.Bus;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.db.UsuarioDao;
import sitetech.NFCcheckPoint.ui.historial.HistorySearchOP;
import sitetech.NFCcheckPoint.ui.nfc.NFCReadFragment;
import sitetech.NFCcheckPoint.ui.nfc.NFCWriteFragment;
import sitetech.NFCcheckPoint.ui.operador.CheckFragment;
import sitetech.NFCcheckPoint.ui.operador.HistoryFragment;
import sitetech.routecheckapp.R;

import static sitetech.NFCcheckPoint.Helpers.nfcHelper.getUid;

public class OperadorActivity extends AppCompatActivity implements Listener, Serializable {

    public Usuario usuarioLog;
    private TextView tusuario;
    private Button blogout;
    private ImageView bbuses;

    private int mInterval = 2000; // 2 seconds by default, can be changed later
    private Handler mHandler;

    private NfcAdapter mNfcAdapter;
    private NFCWriteFragment nfcWriteF;
    private NFCReadFragment nfcReadF;
    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;
    private CoordinatorLayout contendor;
    private AppBarLayout appbar;
    private LinearLayout contenedor;
    private TextView tpunto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operador);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //ACTIVAR ACTION BAR

        cargarControles();

        cargarTabs();
        cargarUsuario();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mHandler = new Handler();
        startChecking();

        Click();
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

    private void cargarControles(){
        tusuario = findViewById(R.id.tusuario);
        blogout = findViewById(R.id.blogout);
        contendor = findViewById(R.id.contenedor);
        bbuses = findViewById(R.id.bbuses);
        appbar = findViewById(R.id.appbar);
        contenedor = findViewById(R.id.opcontenedor);
        tpunto = findViewById(R.id.tpunto);

        tpunto.setText(Configuraciones.getPuntodeControl(this));
    }

    private void Click() {
        bbuses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarHistorial();
            }
        });
        tpunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText tinput = new EditText(v.getContext());
                tinput.setText(tpunto.getText());
                tinput.setInputType(View.AUTOFILL_TYPE_TEXT);
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Punto de Control")
                        .setMessage("¿Desea Cambiar el nombre del punto de control?")
                        .setView(tinput)
                        .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                tpunto.setText(tinput.getText());
                                Configuraciones.setPuntodeControl(getBaseContext(), tinput.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
            }
        });
    }

    boolean isCheckActivity = false;
    boolean expiro = false;
    private void checking() {
        if (checkHelper.verificarNFC(this) && checkHelper.verificarImpresora(this) && checkHelper.verificarFecha(this)) {
            if (!expiro) {
                isCheckActivity = false;
                finishActivity(2);
            }

        } else {
            if (!isCheckActivity) {
                Intent intent = new Intent(this, LockActivity.class);
                startActivityForResult(intent, 2);
                isCheckActivity = true;
            }
        }

        if (!expiro){
            if (TimeHelper.yaExpiro()){
                Intent intent = new Intent(this, LockActivity.class);
                startActivityForResult(intent, 2);
                expiro = true;
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

    private void cargarTabs() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
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

                //ToastHelper.aviso("TAB SELECCIONADA " + tab.getPosition());
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

    public void cargarUsuario() {
        long uid = getIntent().getLongExtra("usuario", 0);
        Usuario ux = AppController.daoSession.getUsuarioDao().queryBuilder().where(UsuarioDao.Properties.Id.eq(uid)).unique();
        if (ux != null) {
            usuarioLog = ux;
            tusuario.setText(usuarioLog.getNombre().toString());
        }

        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.showAsk2(v, "Cerrar Sesion", "¿Quiere cerrar sesion ahora?", "Cerrar ahora", "Cancelar", new myDialogInterface() {
                    @Override
                    public View onBuildDialog() {
                        return null;
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onResult(View vista) {
                        finish(true);
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

    public void escribirNFC(Bus bus) {
        isWrite = true;
        busNFC = bus;
        nfcWriteF = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);

        if (nfcWriteF == null) {
            nfcWriteF = NFCWriteFragment.newInstance();
        }

        nfcWriteF.show(getFragmentManager(), NFCWriteFragment.TAG);
    }

    CheckFragment checkFragment;

    public void leerNFC(CheckFragment fragmentCheck) {
        //nfcReadF = (NFCReadFragment) getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);

        //if (nfcReadF == null) {
        //    nfcReadF = NFCReadFragment.newInstance();
        //}
        //nfcReadF.show(getFragmentManager(), NFCReadFragment.TAG);
        checkFragment = fragmentCheck;
    }

    public void updateCheckFragment(String tag) {
        CheckFragment chf = (CheckFragment) tabsAdapter.getItem(0);
        if (chf != null)
            chf.callBackNfcUid(tag);
    }

    @Override
    public void onDialogDisplayed() {
        //isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {
        //isDialogDisplayed = false;
        //isWrite = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected, tagDetected, ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d("NEW INTENT", "on New Intent: " + intent.getAction());

        if (tag != null) {
            //Toast.makeText(this, "Tarjeta detectada. ID: " + new BigInteger(1, tag.getId()).toString(), Toast.LENGTH_SHORT).show();
            ToastHelper.info("Tarjeta detectada, buscando registro ...");
            //ToastHelper.aviso(tag.toString());
            Ndef ndef = Ndef.get(tag);

            if (checkFragment != null) {
                if (Configuraciones.getUsuarioLog(this) != null)
                    checkFragment.callBackNfcUid(getUid(tag));
                else {
                    ToastHelper.error("Debe de logearse para poder hacer registros.");
                    finish();
                }
            }

            /*if (isDialogDisplayed) {
                if (isWrite) {
                    nfcData nfcdata = new nfcData(busNFC);
                    String messageToWrite = nfcHelper.convertnfcData(nfcdata);
                    nfcWriteF = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);
                    nfcWriteF.onNfcDetected(ndef,messageToWrite);

                } else {
                    nfcReadF = (NFCReadFragment)getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
                    nfcReadF.onNfcDetected(null, getUid(tag));
                }
            }*/
        }
    }

    private HistorySearchOP hsp;

    private void mostrarHistorial() {
        if (hsp == null)
            hsp = new HistorySearchOP();

        appbar.setVisibility(View.GONE);
        contenedor.setVisibility(View.VISIBLE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.opcontenedor, hsp);
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.commit();

        //activityHelper.cargarFragmento2(this, hsp.getTargetFragment(), R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }

    public void cerrarHistorial() {
        appbar.setVisibility(View.VISIBLE);
        contenedor.setVisibility(View.GONE);
    }


    public void finish(boolean salir){
        if (salir)
            super.finish();
    }

    @Override
    public void finish() {
        return ;
    }
}
