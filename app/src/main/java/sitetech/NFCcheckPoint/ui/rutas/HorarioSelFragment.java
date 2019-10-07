package sitetech.NFCcheckPoint.ui.rutas;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sitetech.NFCcheckPoint.Adapters.horarioSelAdapter;
import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.HorarioDao;
import sitetech.NFCcheckPoint.db.horarioPorRuta;
import sitetech.NFCcheckPoint.db.horarioPorRutaDao;
import sitetech.routecheckapp.R;

public class HorarioSelFragment extends Fragment implements  Serializable{

    private static final String MAINFRAGMENT_KEY = "mainFragment";
    private RutaAgregarFragment mainFragment;
    private View vista;

    private OmegaRecyclerView hlista;
    private Button bseleccionar;
    private Button bcancelar;

    public static HorarioSelFragment newInstance(RutaAgregarFragment _main) {
        HorarioSelFragment fragment = new HorarioSelFragment();
        Bundle args = new Bundle();
        args.putSerializable(MAINFRAGMENT_KEY, _main);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mainFragment = (RutaAgregarFragment) getArguments().getSerializable(MAINFRAGMENT_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista =  inflater.inflate(R.layout.horario_sel_fragment, container, false);
        cargarControles();
        showList();

        bseleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        activityHelper.cambiarTitulo(vista, "Seleccionar Horario");
        return vista;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityHelper.cambiarTitulo(vista, "Ruta");
    }

    private void cargarControles(){
        hlista = vista.findViewById(R.id.hlista);
        bseleccionar = vista.findViewById(R.id.bseleccionar);
    }

    final public List<Horario> matchList(List<Horario> lhorarios, List<horarioPorRuta> lhr){
        Log.d("SIZE LHR: ", String.valueOf(lhr.size()));
        Log.d("SIZE LHORARIO: ", String.valueOf(lhorarios.size()));
        List<Horario> nuevaLista = new ArrayList<Horario>();

        if (lhr.size() > 0) {
            for (int i = 0; i < lhorarios.size(); i++) {
                boolean agregar = true;
                for (int j = 0; j < lhr.size(); j++) {
                    if (lhorarios.get(i).getId() == lhr.get(j).getHorario().getId())
                        agregar = false;
                }
                if (agregar){
                    Log.d("AGREGADO : ", lhorarios.get(i).getNombre());
                    nuevaLista.add(lhorarios.get(i));
                }
            }
        }
        else return lhorarios;

        return  nuevaLista;
    }

    private void showList(){
        HorarioDao horariosDao = AppController.daoSession.getHorarioDao();
        List<Horario> lTemp = horariosDao.queryBuilder()
                .where(HorarioDao.Properties.Eliminado.eq(false))
                .list();

        final List<Horario> horariosLista = matchList(lTemp, mainFragment.horariosAdapter.lista);

        final horarioSelAdapter dataAdapter;
        OmegaRecyclerView listView = (OmegaRecyclerView) vista.findViewById(R.id.hlista);
        dataAdapter = new horarioSelAdapter(horariosLista, new onItemClick() {
            @Override
            public void onClickItemList(View v, int position) {
                Horario horarioSel = horariosLista.get(position);

                horarioPorRutaDao hxr = AppController.daoSession.getHorarioPorRutaDao();
                horarioPorRuta hr_nuevo = new horarioPorRuta();
                hr_nuevo.setEliminado(false);
                hr_nuevo.setHorario(horarioSel);
                hr_nuevo.setRuta(mainFragment.RutaSeleccionada);

                hxr.insert(hr_nuevo);
                mainFragment.agregarHorario(hr_nuevo);

                //dataAdapter.deleteData(horariosLista.get(position));

                //ToastHelper.info("Selecciono el horario: " + horariosLista.get(position).getNombre());
            }
        });

        listView.setAdapter(dataAdapter);
    }
}
