package sitetech.NFCcheckPoint.ui.puntos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.io.Serializable;
import java.util.List;

import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.Adapters.puntoAdapter;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Punto;
import sitetech.NFCcheckPoint.db.PuntoDao;
import sitetech.routecheckapp.R;

public class PuntosFragment extends Fragment implements Serializable {

    private View vista;
    private Button bagregar;
    private OmegaRecyclerView ulista;
    private TextView lnotificar;
    private puntoAdapter dataAdapter;
    public PuntosFragment fragmento;
    public Punto Itemseleccionado;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.puntos_fragmento, container, false);
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
                activityHelper.cargarFragmento(fragmento, new PuntosAgregarFragment());
            }
        });

        showList();

        return root;
    }

    private void showList(){
        PuntoDao puntoDao = AppController.daoSession.getPuntoDao(); // QUERY PARA OBTENER TODOS MENOS LOS ELIMINADOS
        List<Punto> lista = puntoDao.queryBuilder()
                .where(PuntoDao.Properties.Eliminada.eq(false))
                .list();

        dataAdapter = new puntoAdapter(lista, new onItemClick() {
            @Override
            public void onClickItemList(View v, int position) {
                fragmento.Itemseleccionado = dataAdapter.lista.get(position);
                activityHelper.cargarFragmento(fragmento, new PuntosAgregarFragment());
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


    public void updateList(Punto bx){
        dataAdapter.updateData(bx);
    }
}