package sitetech.NFCcheckPoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private LinearLayout lcontenedor;
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
    }

    private void cargarControles(){
        userD = AppController.daoSession.getUsuarioDao();

        lnotificacion = findViewById(R.id.lnotificacion);
        biniciar = findViewById(R.id.biniciar);
        biniciar2 = findViewById(R.id.biniciar2);
        tnombre = findViewById(R.id.tnombre);
        tcontraseña = findViewById(R.id.tcontraseña);
        lcontenedor = findViewById(R.id.lcontenedor);

        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);

        Click();
        checkDatabase();

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        lcontenedor.startAnimation(hyperspaceJumpAnimation);
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
        uLogeado = null;
        String contraseña= "", username = "";
        if (tcontraseña.getText() != null) contraseña = tcontraseña.getText().toString();
        if (tnombre.getText() != null) username = tnombre.getText().toString();

        checkLogin(username, contraseña);
    }

    private void checkLogin(String usuario, String contraseña){
        List<Usuario> lusuarios = obtenerUsuarios();
        for( Usuario u : lusuarios){
            //Log.d("USUARIO: ", u.getNombre() + " - " + u.getPassword());
            if (u.getNombre().equals(usuario) && u.getPassword().equals(contraseña))
                uLogeado = u;
            if (u.getCedula().equals(usuario) && u.getPassword().equals(contraseña))
                uLogeado = u;
        }

        loginUser(uLogeado);
    }

    private void loginUser(Usuario ulog){
        if (ulog != null) {
            Configuraciones.setPrimerUso(this, false);
            lnotificacion.setVisibility(View.GONE);
            Configuraciones.setUsuarioLog(this, ulog);

            tnombre.setText("");
            tcontraseña.setText("");

            if (ulog.getRol().equals("Administrador"))
                iniciarAdmin();
            if (ulog.getRol().equals("Operador"))
                iniciarOperador();
        }
        else
            ToastHelper.error("Usuario o contraseña invalida");
    }
    private List<Usuario> obtenerUsuarios(){
        return   userD.queryBuilder().where(UsuarioDao.Properties.Eliminado.eq(false), UsuarioDao.Properties.Activo.eq(true)).list();
    }

    private void iniciarAdmin(){
        ToastHelper.exito("Bienvenido " + uLogeado.getNombre());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("usuario", uLogeado.getId());
        startActivity(intent);
    }

    private void iniciarOperador(){
        ToastHelper.exito("Bienvenido " + uLogeado.getNombre());
        Intent intent = new Intent(this, OperadorActivity.class);
        intent.putExtra("usuario", uLogeado.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Usuario logeado = Configuraciones.getUsuarioLog(this);
        String contraseña= "", username = "";

        if (logeado != null) {
            uLogeado = logeado;
            loginUser(logeado);
        }
    }
}
