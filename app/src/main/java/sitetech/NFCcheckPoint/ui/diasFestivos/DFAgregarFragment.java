package sitetech.NFCcheckPoint.ui.diasFestivos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Calendar;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.DiaFestivo;
import sitetech.NFCcheckPoint.db.DiaFestivoDao;
import sitetech.routecheckapp.R;

public class DFAgregarFragment extends Fragment implements Serializable {
    private static final String MAINFRAGMENT_KEY = "mainFragment";
    private DiasFestivoFragment mainFragment;
    private View vista;

    private TextView tnombre;
    private TextView ttitulo;
    private DatePicker dpfecha;
    private Button bcancelar;
    private Button bguardar;
    public DFAgregarFragment() {
        // Required empty public constructor
    }


    public static DFAgregarFragment newInstance(DiasFestivoFragment _main) {
        DFAgregarFragment fragment = new DFAgregarFragment();
        Bundle args = new Bundle();
        args.putSerializable(MAINFRAGMENT_KEY, _main);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mainFragment = (DiasFestivoFragment) getArguments().getSerializable(MAINFRAGMENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista =  inflater.inflate(R.layout.dia_festivo_agregar_fragment, container, false);
        cargarControles();
        onClick();

        if (mainFragment.Itemseleccionado != null)
            cargarInfo();
        return vista;
    }

    private void cargarInfo(){
        DiaFestivo dx = mainFragment.Itemseleccionado;
        ttitulo.setText("Modificar Dia Festivo");
        tnombre.setText(dx.getNombre());

        Calendar cal = Calendar.getInstance();
        cal.setTime(dx.getDia());
        dpfecha.updateDate(cal.getTime().getYear(), cal.getTime().getMonth(), cal.getTime().getDay());
    }

    private void cargarControles(){
        ttitulo = vista.findViewById(R.id.ttitulo); // Titulo
        tnombre = vista.findViewById(R.id.tnombre);
        dpfecha = vista.findViewById(R.id.dpfecha);

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
                DiaFestivoDao diaManager = AppController.daoSession.getDiaFestivoDao();
                long inserted = -1;

                DiaFestivo dia = new DiaFestivo();
                if (mainFragment.Itemseleccionado != null)
                    dia = mainFragment.Itemseleccionado;

                Calendar calendar = Calendar.getInstance();
                calendar.set(dpfecha.getYear(), dpfecha.getMonth(), dpfecha.getDayOfMonth());

                dia.setNombre(tnombre.getText().toString());
                dia.setDia(calendar.getTime());

                if (mainFragment.Itemseleccionado == null) {
                    dia.setEliminado(false);
                    inserted = diaManager.insert(dia);
                }
                else {
                    diaManager.update(dia);
                    inserted = dia.getId();
                }

                if (inserted > -1) {
                    mainFragment.updateList(dia);
                    activityHelper.goBackStack(vista);
                }
            }
        });

    }
}
