package sitetech.NFCcheckPoint.ui.usuarios;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.db.UsuarioDao;
import sitetech.routecheckapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Usuario_AgregarFragment extends Fragment {

    private Spinner selRol;
    private Button bcancelar;
    private Button bguardar;
    private TextView tnombre;
    private TextView tcedula;
    private TextView ttelefono;

    private View vista;
    public Usuario_AgregarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.usuario_agregar_fragment, container, false);
        cargarControles();
        onClick();

        return vista;
    }

    private void cargarControles(){
        selRol = vista.findViewById(R.id.selRol);
        bcancelar = vista.findViewById(R.id.bcancelar);
        bguardar = vista.findViewById(R.id.bguardar);
        tnombre = vista.findViewById(R.id.tnombre);
        tcedula = vista.findViewById(R.id.tcedula);
        ttelefono = vista.findViewById(R.id.ttelefono);
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
                Usuario user = new Usuario();
                user.setNombre(tnombre.getText().toString());
                user.setCedula(tcedula.getText().toString());
                user.setTelefono(ttelefono.getText().toString());
                user.setRol(selRol.getSelectedItem().toString());
                userManager.insert(user);
                activityHelper.goBackStack(vista);
            }
        });
    }

}
