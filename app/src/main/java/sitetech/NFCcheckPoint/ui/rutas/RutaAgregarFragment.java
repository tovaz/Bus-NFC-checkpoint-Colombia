package sitetech.NFCcheckPoint.ui.rutas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.io.Serializable;
import java.util.List;

import sitetech.NFCcheckPoint.Adapters.horarioPorRutaAdapter;
import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.RutaDao;
import sitetech.NFCcheckPoint.db.horarioPorRuta;
import sitetech.NFCcheckPoint.db.horarioPorRutaDao;
import sitetech.routecheckapp.R;

public class RutaAgregarFragment extends Fragment implements  Serializable{
    private static final String MAINFRAGMENT_KEY = "mainFragment";
    public RutasFragment mainFragment;
    private RutaAgregarFragment agregarFragment;
    private View vista;

    private TextView ttitulo;
    private TextView tnombre;
    private TextView lnotificar;

    private OmegaRecyclerView hlista;
    private Button basignar;

    private Button bcancelar;
    private Button bguardar;

    public Ruta RutaSeleccionada;

    public static RutaAgregarFragment newInstance(RutasFragment _main) {
        RutaAgregarFragment fragment = new RutaAgregarFragment();
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

        agregarFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista =  inflater.inflate(R.layout.ruta_agregar_fragment, container, false);
        cargarControles();
        onClick();


        basignar.setVisibility(View.GONE);
        if (mainFragment.Itemseleccionado != null)  cargarInfo();

        return vista;
    }

    private void cargarInfo(){
        Ruta ruta = mainFragment.Itemseleccionado;
        ttitulo.setText("Modificar Ruta");
        tnombre.setText(ruta.getNombre());
        basignar.setVisibility(View.VISIBLE);
        showListHorarios();
    }

    private void cargarControles(){
        ttitulo = vista.findViewById(R.id.ttitulo); // Titulo
        tnombre = vista.findViewById(R.id.tnombre);
        basignar = vista.findViewById(R.id.basignar);
        hlista = vista.findViewById(R.id.hlista);
        lnotificar = vista.findViewById(R.id.lnotificar);

        bcancelar = vista.findViewById(R.id.bcancelar);
        bguardar = vista.findViewById(R.id.bguardar);
    }

    private void onClick(){
        basignar.setOnClickListener(new View.OnClickListener() { //ABRIR DIALOGO PARA SELECCIONAR HORARIO
            @Override
            public void onClick(View v) {
                RutaSeleccionada =  mainFragment.Itemseleccionado;
                activityHelper.cargarFragmento(agregarFragment, new HorarioSelFragment(), R.anim.slide_down, R.anim.slide_up, R.anim.slide_down, R.anim.slide_down);
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

        bcancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHelper.goBackStack(vista);
            }
        });
    }

    public horarioPorRutaAdapter horariosAdapter;
    private void showListHorarios(){
        horarioPorRutaDao horarioDao = AppController.daoSession.getHorarioPorRutaDao(); // QUERY PARA OBTENER TODOS MENOS LOS ELIMINADOS
        List<horarioPorRuta> lista = horarioDao.queryBuilder()
                .where(horarioPorRutaDao.Properties.RutaId.eq(mainFragment.Itemseleccionado.getId()),
                        horarioPorRutaDao.Properties.Eliminado.eq(false))

                .orderDesc(horarioPorRutaDao.Properties.Id)
                .list();

        horariosAdapter = new horarioPorRutaAdapter(lista, new onItemClick() {
            @Override
            public void onClickItemList(View v, int position) {
                ToastHelper.info(horariosAdapter.lista.get(position).getHorario().getHoraDesde().toString());
            }
        });

        if (horariosAdapter.getItemCount() == 0) {
            hlista.setVisibility(View.GONE);
            lnotificar.setVisibility(View.VISIBLE);
        }


        hlista.setLayoutManager(new LinearLayoutManager(getContext()));
        hlista.setAdapter(horariosAdapter);
    }

    public void agregarHorario(horarioPorRuta h){
        Log.d("AGREGADO :: ", h.getHorario().getNombre());
        horariosAdapter.updateData(h);
        if (horariosAdapter.getItemCount() > 0) {
            hlista.setVisibility(View.VISIBLE);
            lnotificar.setVisibility(View.GONE);
        }
    }
}
