package sitetech.NFCcheckPoint.ui.puntos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.Serializable;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Punto;
import sitetech.NFCcheckPoint.db.PuntoDao;
import sitetech.routecheckapp.R;

public class PuntosAgregarFragment extends Fragment implements  Serializable{
    private static final String MAINFRAGMENT_KEY = "mainFragment";
    public PuntosFragment mainFragment;
    private View vista;

    private TextView ttitulo;
    private TextView tnombre;


    private Button bcancelar;
    private Button bguardar;


    public static PuntosAgregarFragment newInstance(PuntosFragment _main) {
        PuntosAgregarFragment fragment = new PuntosAgregarFragment();
        Bundle args = new Bundle();
        args.putSerializable(MAINFRAGMENT_KEY, _main);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mainFragment = (PuntosFragment) getArguments().getSerializable(MAINFRAGMENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista =  inflater.inflate(R.layout.puntos_agregar_fragment, container, false);
        cargarControles();
        onClick();


        if (mainFragment.Itemseleccionado != null)  cargarInfo();

        return vista;
    }

    private void cargarInfo(){
        Punto punto = mainFragment.Itemseleccionado;
        ttitulo.setText("Modificar Punto de Control");
        tnombre.setText(punto.getNombre());
    }

    private void cargarControles(){
        ttitulo = vista.findViewById(R.id.ttitulo); // Titulo
        tnombre = vista.findViewById(R.id.tnombre);

        bcancelar = vista.findViewById(R.id.bcancelar);
        bguardar = vista.findViewById(R.id.bguardar);
    }

    private void onClick(){
        bguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PuntoDao rutaManager = AppController.daoSession.getPuntoDao();
                long inserted = -1;

                Punto punto = new Punto();
                if (mainFragment.Itemseleccionado != null)
                    punto = mainFragment.Itemseleccionado;

                punto.setNombre(tnombre.getText().toString());

                if (mainFragment.Itemseleccionado == null) {
                    punto.setEliminada(false);
                    inserted = rutaManager.insert(punto);
                }
                else {
                    rutaManager.update(punto);
                    inserted = punto.getId();
                }

                if (inserted > -1) {
                    mainFragment.updateList(punto);
                    activityHelper.goBackStack(vista);
                }
            }
        });

        bcancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHelper.goBackStack(vista);
            }
        });
    }

}
