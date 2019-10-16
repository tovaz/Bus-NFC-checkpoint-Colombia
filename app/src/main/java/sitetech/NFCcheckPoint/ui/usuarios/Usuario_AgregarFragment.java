package sitetech.NFCcheckPoint.ui.usuarios;


import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.Serializable;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.db.UsuarioDao;
import sitetech.routecheckapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Usuario_AgregarFragment extends Fragment implements Serializable {

    private static final String MAINFRAGMENT_KEY = "mainFragment";
    private Spinner selRol;
    private Button bcancelar;
    private Button bguardar;
    private TextView tnombre;
    private TextView tcedula;
    private TextView tpassword;
    private TextView ttelefono;
    private TextView ttitulo;
    private SwitchCompat sactivo;
    private View vista;
    private UsuariosFragment mainFragment;

    public Usuario_AgregarFragment(){}

    public static Usuario_AgregarFragment newInstance(UsuariosFragment _main) {
        Usuario_AgregarFragment fragment = new Usuario_AgregarFragment();
        Bundle args = new Bundle();
        args.putSerializable(MAINFRAGMENT_KEY, _main);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mainFragment = (UsuariosFragment) getArguments().getSerializable(MAINFRAGMENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.usuario_agregar_fragment, container, false);
        cargarControles();
        onClick();

        if (mainFragment.Useleccionado != null)
            cargarInfo();
        return vista;
    }

    private void cargarInfo(){
        Usuario user = mainFragment.Useleccionado;
        ttitulo.setText("Modificar Usuario");
        tnombre.setText(user.getNombre());
        tcedula.setText(user.getCedula());
        tpassword.setText(user.getPassword());
        ttelefono.setText(user.getTelefono());
        sactivo.setChecked(user.getActivo());
    }

    private void cargarControles(){
        ttitulo = vista.findViewById(R.id.ttitulo);
        selRol = vista.findViewById(R.id.selempresa);
        bcancelar = vista.findViewById(R.id.bcancelar);
        bguardar = vista.findViewById(R.id.bguardar);
        tnombre = vista.findViewById(R.id.tnombre);
        tcedula = vista.findViewById(R.id.tcedula);
        tpassword = vista.findViewById(R.id.tpassword);
        ttelefono = vista.findViewById(R.id.ttelefono);
        sactivo = vista.findViewById(R.id.sactivo);
    }

    private void onClick(){
        bcancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHelper.goBackStack(vista);
            }
        });

        bguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsuarioDao userManager = AppController.daoSession.getUsuarioDao();
                long inserted = -1;

                Usuario user = new Usuario();
                if (mainFragment.Useleccionado != null)
                    user = mainFragment.Useleccionado;

                user.setNombre(tnombre.getText().toString());
                user.setCedula(tcedula.getText().toString());
                user.setPassword(tpassword.getText().toString());
                user.setTelefono(ttelefono.getText().toString());
                user.setRol(selRol.getSelectedItem().toString());


                if (mainFragment.Useleccionado == null) {
                    user.setActivo(true);
                    user.setEliminado(false);
                    inserted = userManager.insert(user);
                }
                else {
                    user.setActivo(sactivo.isChecked());
                    userManager.update(user);
                    inserted = user.getId();
                }

                if (inserted > -1) {
                    mainFragment.updateList(user);
                    activityHelper.goBackStack(vista);
                }
            }
        });

    }

}
