package sitetech.NFCcheckPoint.ui.empresas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Empresa;
import sitetech.NFCcheckPoint.db.EmpresaDao;
import sitetech.routecheckapp.R;

public class EmpresaAgregarFragment extends Fragment implements Serializable {
    private static final String MAINFRAGMENT_KEY = "mainFragment";
    private EmpresasFragment mainFragment;
    private View vista;

    private TextView tnombre;
    private TextView ttitulo;
    private TextView ttelefono;
    private Button bcancelar;
    private Button bguardar;
    public EmpresaAgregarFragment() {
        // Required empty public constructor
    }


    public static EmpresaAgregarFragment newInstance(EmpresasFragment _main) {
        EmpresaAgregarFragment fragment = new EmpresaAgregarFragment();
        Bundle args = new Bundle();
        args.putSerializable(MAINFRAGMENT_KEY, _main);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mainFragment = (EmpresasFragment) getArguments().getSerializable(MAINFRAGMENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista =  inflater.inflate(R.layout.empresa_agregar_fragment, container, false);
        cargarControles();
        onClick();

        if (mainFragment.Itemseleccionado != null)
            cargarInfo();
        return vista;
    }

    private void cargarInfo(){
        Empresa empresa = mainFragment.Itemseleccionado;
        ttitulo.setText("Modificar Empresa");
        tnombre.setText(empresa.getNombre());
        ttelefono.setText(empresa.getTelefono());
    }

    private void cargarControles(){
        ttitulo = vista.findViewById(R.id.ttitulo); // Titulo
        tnombre = vista.findViewById(R.id.tnombre);
        ttelefono = vista.findViewById(R.id.tcedula);

        bcancelar = vista.findViewById(R.id.bcancelar);
        bguardar = vista.findViewById(R.id.bguardar);
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
                EmpresaDao empresaManager = AppController.daoSession.getEmpresaDao();
                long inserted = -1;

                Empresa empresa = new Empresa();
                if (mainFragment.Itemseleccionado != null)
                    empresa = mainFragment.Itemseleccionado;

                empresa.setNombre(tnombre.getText().toString());
                empresa.setTelefono(ttelefono.getText().toString());

                if (mainFragment.Itemseleccionado == null) {
                    empresa.setEliminado(false);
                    inserted = empresaManager.insert(empresa);
                }
                else {
                    empresaManager.update(empresa);
                    inserted = empresa.getId();
                }

                if (inserted > -1) {
                    mainFragment.updateList(empresa);
                    activityHelper.goBackStack(vista);
                }
            }
        });

    }
}
