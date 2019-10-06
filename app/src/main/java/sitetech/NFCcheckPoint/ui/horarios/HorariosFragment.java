package sitetech.NFCcheckPoint.ui.horarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.io.Serializable;
import java.util.List;

import sitetech.NFCcheckPoint.Adapters.horarioAdapter;
import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.Adapters.rutaAdapter;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.HorarioDao;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.RutaDao;
import sitetech.NFCcheckPoint.ui.rutas.RutaAgregarFragment;
import sitetech.NFCcheckPoint.ui.rutas.RutasFragment;
import sitetech.routecheckapp.R;

public class HorariosFragment extends Fragment implements Serializable {
    private View vista;
    private Button bagregar;
    private OmegaRecyclerView ulista;
    private TextView lnotificar;
    private horarioAdapter dataAdapter;
    private HorariosFragment fragmento;
    public Horario Itemseleccionado;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.horarios_fragment, container, false);
        bagregar = root.findViewById(R.id.bagregar);
        ulista = root.findViewById(R.id.ulista);
        lnotificar = root.findViewById(R.id.lnotificar);
        Itemseleccionado = null;

        vista = root;
        fragmento = this;
        bagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Itemseleccionado = null;
                activityHelper.cargarFragmento(fragmento, new HorarioAgregarFragment());
            }
        });

        showList();

        return root;
    }

    private void showList(){
        HorarioDao horarioDao = AppController.daoSession.getHorarioDao(); // QUERY PARA OBTENER TODOS MENOS LOS ELIMINADOS
        List<Horario> lista = horarioDao.queryBuilder()
                .where(HorarioDao.Properties.Eliminado.eq(false))
                .list();

        dataAdapter = new horarioAdapter(lista, new onItemClick() {
            @Override
            public void onClickItemList(View v, int position) {
                fragmento.Itemseleccionado = dataAdapter.lista.get(position);
                activityHelper.cargarFragmento(fragmento, new HorarioAgregarFragment());
            }
        });

        /*for ( Bus b : BusDao.loadAll() ) {
            Log.d(b.getPlaca(), b.getInterno());
        }*/

        if (dataAdapter.getItemCount() == 0) {
            ulista.setVisibility(View.GONE);
            lnotificar.setVisibility(View.VISIBLE);
        }
        else {
            ulista.setVisibility(View.VISIBLE);
            lnotificar.setVisibility(View.GONE);
        }


        ulista.setLayoutManager(new LinearLayoutManager(getContext()));
        ulista.setAdapter(dataAdapter);
    }


    public void updateList(Horario bx){
        dataAdapter.updateData(bx);
    }
}