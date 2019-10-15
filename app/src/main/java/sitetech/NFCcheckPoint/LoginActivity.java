package sitetech.NFCcheckPoint;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.printHelper;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.db.UsuarioDao;
import sitetech.routecheckapp.R;

public class LoginActivity extends AppCompatActivity {

    private Button biniciar;
    private Button biniciar2;
    private TextView lnotificacion;
    private EditText tnombre;
    private EditText tcontraseña;
    private UsuarioDao userD;
    private Usuario uLogeado;

    private Button b1, b2, b3, b4;

    private AppCompatActivity activity;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //ACTIVAR ACTION BAR

        activity = this;
        cargarControles();
        requestPermisos();
    }

    private void cargarControles(){
        userD = AppController.daoSession.getUsuarioDao();

        lnotificacion = findViewById(R.id.lnotificacion);
        biniciar = findViewById(R.id.biniciar);
        biniciar2 = findViewById(R.id.biniciar2);
        tnombre = findViewById(R.id.tnombre);
        tcontraseña = findViewById(R.id.tcontraseña);

        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);

        Click();
        checkDatabase();
    }

    private void requestPermisos() {
        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance // Must be done during an initialization phase like onCreate
        rxPermissions.request(Manifest.permission.NFC);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        rxPermissions.request(Manifest.permission.BLUETOOTH);
        rxPermissions.request(Manifest.permission.BLUETOOTH_ADMIN);
        rxPermissions.request(Manifest.permission.BLUETOOTH_PRIVILEGED);

    }

    private void Click(){
        biniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
                //iniciarAdmin();
            }
        });

        biniciar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uLogeado = AppController.daoSession.getUsuarioDao().queryBuilder().where(UsuarioDao.Properties.Id.eq(1)).unique();
                iniciarAdmin();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printHelper.pruerba1(activity);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printHelper.pruerba2(activity);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printHelper.pruerba3(activity);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //printHelper.pruerba4(activity);
            }
        });
    }

    private void checkDatabase(){
        if (Configuraciones.getPrimerUso(this)) {
            lnotificacion.setVisibility(View.VISIBLE);
            lnotificacion.setText("SE DETECTO PRIMER USO: Se han creado 2 usuarios de prueba, admin y A ambos sin contraseña.");
        }
    }

    private void checkLogin(){
        List<Usuario> lusuarios = obtenerUsuarios();
        uLogeado = null;
        String contraseña= "", username = "";
        if (tcontraseña.getText() != null) contraseña = tcontraseña.getText().toString();
        if (tnombre.getText() != null) username = tnombre.getText().toString();

        for( Usuario u : lusuarios){
            //Log.d("USUARIO: ", u.getNombre() + " - " + u.getPassword());
            if (u.getNombre().equals(username) && u.getPassword().equals(contraseña))
                uLogeado = u;
            if (u.getCedula().equals(username) && u.getPassword().equals(contraseña))
                uLogeado = u;
        }

        if (uLogeado != null) {
            Configuraciones.setPrimerUso(this, false);
            lnotificacion.setVisibility(View.GONE);
            Configuraciones.setUsuarioLog(this, uLogeado);

            tnombre.setText("");
            tcontraseña.setText("");

            if (uLogeado.getRol().equals("Administrador"))
                iniciarAdmin();
            if (uLogeado.getRol().equals("Operador"))
                iniciarOperador();
        }
        else
            ToastHelper.error("Usuario o contraseña invalida");

    }

    private List<Usuario> obtenerUsuarios(){
        return   userD.queryBuilder().where(UsuarioDao.Properties.Eliminado.eq(false), UsuarioDao.Properties.Activo.eq(true)).list();
    }

    private void iniciarAdmin(){
        ToastHelper.exito("Logiado como Administrador");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("usuario", uLogeado.getId());
        startActivity(intent);
    }

    private void iniciarOperador(){
        ToastHelper.exito("Logiado como Operador");
        Intent intent = new Intent(this, OperadorActivity.class);
        intent.putExtra("usuario", uLogeado.getId());
        startActivity(intent);
    }


}
