package sitetech.NFCcheckPoint.ui.buses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.io.Serializable;
import java.util.List;

import sitetech.NFCcheckPoint.Adapters.busAdapter;
import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.MainActivity;
import sitetech.NFCcheckPoint.db.Bus;
import sitetech.NFCcheckPoint.db.BusDao;
import sitetech.routecheckapp.R;

public class BusesFragment extends Fragment implements Serializable {

    private View vista;
    private Button bagregar;
    private OmegaRecyclerView ulista;
    private TextView lnotificar;
    private busAdapter bAdapter;
    private BusesFragment fragmento;
    public Bus Itemseleccionado;
    public FragmentActivity fragmentContext;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.buses_fragment, container, false);
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
                activityHelper.cargarFragmento(fragmento, new BusAgregarFragment());


            }
        });

        showList();

        return root;
    }

    private void showList(){
        BusDao busDao = AppController.daoSession.getBusDao(); // QUERY PARA OBTENER TODOS MENOS LOS ELIMINADOS
        List<Bus> lista = busDao.queryBuilder()
                .where(BusDao.Properties.Eliminado.eq(false))
                .list();

        bAdapter = new busAdapter(lista, new onItemClick() {
            @Override
            public void onClickItemList(View v, int position) {
                fragmento.Itemseleccionado = bAdapter.lista.get(position);
                activityHelper.cargarFragmento(fragmento, new BusAgregarFragment());
            }
        });

        if (bAdapter.getItemCount() == 0) {
            ulista.setVisibility(View.GONE);
            lnotificar.setVisibility(View.VISIBLE);
        }

        ulista.setLayoutManager(new LinearLayoutManager(getContext()));
        ulista.setAdapter(bAdapter);
    }


    public void updateList(Bus bx){
        bAdapter.updateData(bx);
    }

}