package sitetech.NFCcheckPoint.ui.diasFestivos;

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

import sitetech.NFCcheckPoint.Adapters.diaFestivoAdapter;
import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.DiaFestivo;
import sitetech.NFCcheckPoint.db.DiaFestivoDao;
import sitetech.NFCcheckPoint.db.EmpresaDao;
import sitetech.routecheckapp.R;

public class DiasFestivoFragment extends Fragment implements Serializable {

    private View vista;
    private Button bagregar;
    private OmegaRecyclerView ulista;
    private TextView lnotificar;
    private diaFestivoAdapter bAdapter;
    private DiasFestivoFragment fragmento;
    public DiaFestivo Itemseleccionado;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dia_festivo_fragment, container, false);
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
                activityHelper.cargarFragmento(fragmento, new DFAgregarFragment());
            }
        });

        showList();

        return root;
    }

    private void showList(){
        DiaFestivoDao diaDao = AppController.daoSession.getDiaFestivoDao(); // QUERY PARA OBTENER TODOS MENOS LOS ELIMINADOS
        List<DiaFestivo> lista = diaDao.queryBuilder()
                .where(DiaFestivoDao.Properties.Eliminado.eq(false))
                .list();

        bAdapter = new diaFestivoAdapter(lista, new onItemClick() {
            @Override
            public void onClickItemList(View v, int position) {
                fragmento.Itemseleccionado = bAdapter.lista.get(position);
                activityHelper.cargarFragmento(fragmento, new DFAgregarFragment());
            }
        });

        /*for ( Bus b : BusDao.loadAll() ) {
            Log.d(b.getPlaca(), b.getInterno());
        }*/

        if (bAdapter.getItemCount() == 0) {
            ulista.setVisibility(View.GONE);
            lnotificar.setVisibility(View.VISIBLE);
        }
        else {
            ulista.setVisibility(View.VISIBLE);
            lnotificar.setVisibility(View.GONE);
        }


        ulista.setLayoutManager(new LinearLayoutManager(getContext()));
        ulista.setAdapter(bAdapter);
    }


    public void updateList(DiaFestivo bx){
        bAdapter.updateData(bx);
    }
}