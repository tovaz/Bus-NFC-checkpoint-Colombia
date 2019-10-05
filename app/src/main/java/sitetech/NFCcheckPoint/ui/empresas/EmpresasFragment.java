package sitetech.NFCcheckPoint.ui.empresas;

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

import sitetech.NFCcheckPoint.Adapters.empresaAdapter;
import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Empresa;
import sitetech.NFCcheckPoint.db.EmpresaDao;
import sitetech.routecheckapp.R;

public class EmpresasFragment extends Fragment implements Serializable {

    private View vista;
    private Button bagregar;
    private OmegaRecyclerView ulista;
    private TextView lnotificar;
    private empresaAdapter bAdapter;
    private EmpresasFragment fragmento;
    public Empresa Itemseleccionado;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_empresas, container, false);
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
                activityHelper.cargarFragmento(fragmento, new EmpresaAgregarFragment());
            }
        });

        showList();

        return root;
    }

    private void showList(){
        EmpresaDao empresaDao = AppController.daoSession.getEmpresaDao(); // QUERY PARA OBTENER TODOS MENOS LOS ELIMINADOS
        List<Empresa> lista = empresaDao.queryBuilder()
                .where(EmpresaDao.Properties.Eliminado.eq(false))
                .list();

        bAdapter = new empresaAdapter(lista, new onItemClick() {
            @Override
            public void onClick(View v, int position) {
                fragmento.Itemseleccionado = bAdapter.lista.get(position);
                activityHelper.cargarFragmento(fragmento, new EmpresaAgregarFragment());
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


    public void updateList(Empresa bx){
        bAdapter.updateData(bx);
    }
}