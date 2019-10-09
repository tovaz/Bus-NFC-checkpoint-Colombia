package sitetech.NFCcheckPoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.concurrent.locks.Lock;

import sitetech.NFCcheckPoint.Adapters.TabsAdapter;
import sitetech.NFCcheckPoint.Helpers.Dialog;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.checkHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.db.UsuarioDao;
import sitetech.NFCcheckPoint.ui.operador.HistoryFragment;
import sitetech.routecheckapp.R;

public class OperadorActivity extends AppCompatActivity {

    public Usuario usuarioLog;
    private TextView tusuario;
    private Button blogout;

    private int mInterval = 2000; // 2 seconds by default, can be changed later
    private Handler mHandler;
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

    private  void cargarTabs(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager);
        final TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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
}
