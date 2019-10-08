package sitetech.NFCcheckPoint.ui.operador;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.expandable_recycler_view.OmegaExpandableRecyclerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.Adapters.rutaAdapter;
import sitetech.NFCcheckPoint.Adapters.rutaSelAdapter;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.RutaDao;
import sitetech.NFCcheckPoint.ui.rutas.RutaAgregarFragment;
import sitetech.routecheckapp.R;

public class CheckFragment extends Fragment {
    public OmegaRecyclerView rlista;
    public View vista;
    private TextView tfecha;
    private TextView tusuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        vista =  inflater.inflate(R.layout.operador_check_fragment, viewGroup, false);

        cargarControles();
        cargarLista();
        showFechayHora();
        return vista;
    }

    private void cargarControles(){
        rlista = vista.findViewById(R.id.rlista);
        tfecha = vista.findViewById(R.id.tfecha);
        tusuario = vista.findViewById(R.id.tusuario);
    }

    private void showFechayHora(){
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        tfecha.setText(currentDateTimeString);
    }

    rutaSelAdapter dataAdapter;
    private void cargarLista(){
        RutaDao rutaDao = AppController.daoSession.getRutaDao(); // QUERY PARA OBTENER TODOS MENOS LOS ELIMINADOS
        final List<Ruta> lista = rutaDao.queryBuilder()
                .where(RutaDao.Properties.Eliminada.eq(false))
                .list();

        dataAdapter = new rutaSelAdapter(lista, new onItemClick() {
            @Override
            public void onClickItemList(View v, int position) {
                ToastHelper.info(lista.get(position).getNombre().toString());
            }
        });

        rlista.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rlista.setAdapter(dataAdapter);
    }


}

