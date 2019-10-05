package sitetech.NFCcheckPoint.ui.horarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.RutaDao;
import sitetech.NFCcheckPoint.ui.empresas.EmpresaAgregarFragment;
import sitetech.NFCcheckPoint.ui.empresas.EmpresasFragment;
import sitetech.NFCcheckPoint.ui.rutas.RutasFragment;
import sitetech.routecheckapp.R;

public class HorarioAgregarFragment extends Fragment {
    private static final String MAINFRAGMENT_KEY = "mainFragment";
    private RutasFragment mainFragment;
    private View vista;

    private TextView ttitulo;
    private TextView tnombre;
    private Button bcancelar;
    private Button bguardar;

    public HorarioAgregarFragment() { }

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
            mainFragment = (RutasFragment) getArguments().getSerializable(MAINFRAGMENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista =  inflater.inflate(R.layout.horario_agregar_fragment, container, false);
        cargarControles();
        onClick();

        if (mainFragment.Itemseleccionado != null)
            cargarInfo();
        return vista;
    }

    private void cargarInfo(){
        Ruta ruta = mainFragment.Itemseleccionado;
        ttitulo.setText("Modificar Ruta");
        tnombre.setText(ruta.getNombre());
    }

    private void cargarControles(){
        ttitulo = vista.findViewById(R.id.ttitulo); // Titulo
        tnombre = vista.findViewById(R.id.tnombre);

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
                RutaDao rutaManager = AppController.daoSession.getRutaDao();
                long inserted = -1;

                Ruta ruta = new Ruta();
                if (mainFragment.Itemseleccionado != null)
                    ruta = mainFragment.Itemseleccionado;

                ruta.setNombre(tnombre.getText().toString());

                if (mainFragment.Itemseleccionado == null) {
                    ruta.setEliminada(false);
                    inserted = rutaManager.insert(ruta);
                }
                else {
                    rutaManager.update(ruta);
                    inserted = ruta.getId();
                }

                if (inserted > -1) {
                    mainFragment.updateList(ruta);
                    activityHelper.goBackStack(vista);
                }
            }
        });

    }
}
