package sitetech.NFCcheckPoint;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.DialogHelper;
import sitetech.NFCcheckPoint.Helpers.Listener;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.Helpers.nfcData;
import sitetech.NFCcheckPoint.Helpers.nfcHelper;
import sitetech.NFCcheckPoint.db.Bus;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.db.UsuarioDao;
import sitetech.NFCcheckPoint.ui.buses.BusAgregarFragment;
import sitetech.NFCcheckPoint.ui.nfc.NFCReadFragment;
import sitetech.NFCcheckPoint.ui.nfc.NFCWriteFragment;
import sitetech.NFCcheckPoint.ui.operador.CheckFragment;
import sitetech.routecheckapp.R;

import static sitetech.NFCcheckPoint.Helpers.nfcHelper.getUid;
import static sitetech.NFCcheckPoint.Util.typeConverter.bin2hex;

public class MainActivity extends AppCompatActivity implements Listener {

    private AppBarConfiguration mAppBarConfiguration;
    public MainActivity mainActivity;
    public Usuario usuarioLog;
    private TextView tnombre;
    private TextView trol;
    private Button blogout;
    private NavigationView navigationView;

    private NfcAdapter mNfcAdapter;
    private NFCWriteFragment nfcWriteF;
    private NFCReadFragment nfcReadF;
    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;
    public BusAgregarFragment busAgragarF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //ACTIVAR ACTION BAR

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_usuarios, R.id.nav_empresas, R.id.nav_buses,
                R.id.nav_rutas, R.id.nav_historial, R.id.nav_horarios, R.id.nav_dias_festivos, R.id.nav_puntos, R.id.nav_configuracion)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        mainActivity = this;

        cargarControles();
        cargarUsuario();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        startService(new Intent(this, miServicio.class)); //INICIAR EL SERVICIO
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) { //OCULTAR TECLADO CUANDO TOCA EN CUALQUIER PARTE
        /*if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        */
        return super.dispatchTouchEvent(ev);
    }

    private void cargarControles(){
        View header = navigationView.getHeaderView(0);
        tnombre = header.findViewById(R.id.tusuario);
        trol = header.findViewById(R.id.trol);
        blogout = header.findViewById(R.id.blogout);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void cargarUsuario(){
        long uid = getIntent().getLongExtra("usuario", 0);
        Usuario ux = AppController.daoSession.getUsuarioDao().queryBuilder().where(UsuarioDao.Properties.Id.eq(uid)).unique();
        if (ux != null) {
            usuarioLog = ux;
            tnombre.setText(usuarioLog.getNombre().toString());
            trol.setText(usuarioLog.getRol().toString());
        }

        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.showAsk2(navigationView, "Cerrar Sesion", "¿Quiere cerrar sesion ahora?", "Cerrar ahora", "Cancelar", new myDialogInterface() {
                    @Override
                    public View onBuildDialog() {  return null; }

                    @Override
                    public void onCancel() { }

                    @Override
                    public void onResult(View vista) {
                        Configuraciones.setUsuarioLog(getBaseContext(), null);
                        mainActivity.finish();
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /***************************************************************************/
    private Bus busNFC;
    public void escribirNFC(Bus bus, BusAgregarFragment fragmento){
        busAgragarF = fragmento;
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
        nfcReadF.show(getFragmentManager(),NFCReadFragment.TAG);
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
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{ndefDetected,techDetected,tagDetected};

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

    public void updateBus(String uid){
        if (busAgragarF != null)
            busAgragarF.asignarTarjeta(uid);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        ToastHelper.info(bin2hex(tag.getId()));

        //String[] techList = tag.getTechList();
        //Toast.makeText(this, "UID: " + bin2hex(tag.getId()), Toast.LENGTH_SHORT).show();

        //Log.d("TECH LIST", "");
        //for ( String x : techList)
        //    Log.d("l", x);


        if(tag != null) {
            Ndef ndef = Ndef.get(tag);
            //if (ndef == null) ndef = formatearTag(ndef, tag);

            Gson g = new Gson();
            //ToastHelper.aviso(ndef.toString());
            if (isDialogDisplayed) {
                if (isWrite) {
                    nfcWriteF.onNfcDetected(ndef, getUid(tag));
                    updateBus(getUid(tag));
                    //nfcData nfcdata = new nfcData(busNFC);
                    //String messageToWrite = nfcHelper.convertnfcData(nfcdata);
                    //nfcWriteF = (NFCWriteFragment) getFragmentManager().findFragmentByTag(NFCWriteFragment.TAG);
                    //nfcWriteF.onNfcDetected(ndef,messageToWrite);


                } /*else {
                    nfcReadF = (NFCReadFragment)getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
                    nfcReadF.onNfcDetected(ndef);
                }*/
            }
        }
        else ToastHelper.aviso("Error al intentar leer la tarjeta, intanta de nuevo.");
    }

    private Ndef formatearTag(Ndef ndefTag, Tag tag){
        if (ndefTag.get(tag) == null) {
            try {
                Log.d("FORMATEANDO TAG tipo", ndefTag.getType());
                ndefTag.writeNdefMessage(new NdefMessage(new NdefRecord(NdefRecord.TNF_EMPTY, null, null, null)));
            } catch (Exception e) {
                Log.d("FORMATEANDO TAG", e.getMessage());
            }
        }

        return ndefTag;
    }

}
