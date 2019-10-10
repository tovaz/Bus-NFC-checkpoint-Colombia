package sitetech.NFCcheckPoint.ui.buses;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sitetech.NFCcheckPoint.Adapters.customAdapter;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.Listener;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.MainActivity;
import sitetech.NFCcheckPoint.db.Bus;
import sitetech.NFCcheckPoint.db.BusDao;
import sitetech.NFCcheckPoint.db.Empresa;
import sitetech.NFCcheckPoint.db.EmpresaDao;
import sitetech.NFCcheckPoint.ui.nfc.NFCWriteFragment;
import sitetech.routecheckapp.R;

public class BusAgregarFragment extends Fragment implements AdapterView.OnItemSelectedListener, Serializable {
    private static final String MAINFRAGMENT_KEY = "mainFragment";
    private BusesFragment mainFragment;
    private View vista;

    private TextView tplaca;
    private TextView tinterno;
    private TextView ttitulo;
    private Spinner selempresa;
    private Button bescribirNFC;
    private Button bcancelar;
    private Button bguardar;

    List<Empresa> listaEmpresas;
    ArrayAdapter<String> empresasAdapter;

    public BusAgregarFragment() { }

    public static BusAgregarFragment newInstance(BusesFragment _main){
        BusAgregarFragment fragment = new BusAgregarFragment();
        Bundle args = new Bundle();
        args.putSerializable(MAINFRAGMENT_KEY, _main);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mainFragment = (BusesFragment) getArguments().getSerializable(MAINFRAGMENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.bus_agregar_fragment, container, false);
        cargarControles();
        onClick();

        bescribirNFC.setVisibility(View.GONE);
        if (mainFragment.Itemseleccionado != null)
            cargarInfo();
        return vista;
    }

    private void cargarInfo(){
        Bus bus = mainFragment.Itemseleccionado;
        ttitulo.setText("Modificar Bus");
        tplaca.setText(bus.getPlaca());
        tinterno.setText(bus.getInterno());

        bescribirNFC.setVisibility(View.VISIBLE);
        for (int i=0; i<=listaEmpresas.size()-1; i++)
            if ((listaEmpresas.get(i).getId() + " - " + listaEmpresas.get(i).getNombre()).equals(bus.getEmpresa().getId() + " - " + bus.getEmpresa().getNombre()))
                selempresa.setSelection(i);
    }

    private void cargarControles(){
        ttitulo = vista.findViewById(R.id.ttitulo);
        tplaca = vista.findViewById(R.id.tnombre);
        tinterno = vista.findViewById(R.id.tcedula);
        selempresa = vista.findViewById(R.id.selempresa);
        bcancelar = vista.findViewById(R.id.bcancelar);
        bguardar = vista.findViewById(R.id.bguardar);

        bescribirNFC = vista.findViewById(R.id.bescribirNFC);
        cargarEmpresas();
    }

    /**********************************************************************************/
    private void cargarEmpresas(){
        EmpresaDao empresaDao = AppController.daoSession.getEmpresaDao();
        selempresa.setOnItemSelectedListener(this);

        ArrayList<String> lEmpresas = new ArrayList<>();
        listaEmpresas = empresaDao.loadAll();
        empresa = listaEmpresas.get(0);

        selempresa.setOnItemSelectedListener(this);

        customAdapter CustomAdapter=new customAdapter(AppController.getAppContext(), listaEmpresas);
        selempresa.setAdapter(CustomAdapter);
    }

    private Empresa empresa;
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        //Toast.makeText(AppController.getAppContext(), listaEmpresas.get(position).getNombre(), Toast.LENGTH_LONG).show();
        empresa = listaEmpresas.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

        /**********************************************************************************/

        private boolean isWrite;
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
                BusDao busManager = AppController.daoSession.getBusDao();
                long inserted = -1;

                Bus bus = new Bus();
                if (mainFragment.Itemseleccionado != null)
                    bus = mainFragment.Itemseleccionado;

                bus.setPlaca(tplaca.getText().toString());
                bus.setInterno(tinterno.getText().toString());
                bus.setEmpresa(empresa);

                if (mainFragment.Itemseleccionado == null) {
                    bus.setEliminado(false);
                    inserted = busManager.insert(bus);
                }
                else {
                    busManager.update(bus);
                    inserted = bus.getId();
                }

                if (inserted > -1) {
                    mainFragment.updateList(bus);
                    activityHelper.goBackStack(vista);
                }
            }
        });

        bescribirNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)getActivity();
                activity.escribirNFC(mainFragment.Itemseleccionado);
            }
        });
    }

}
