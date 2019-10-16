package sitetech.NFCcheckPoint.ui.operador;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.List;

import sitetech.NFCcheckPoint.Adapters.onEditClick;
import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.Adapters.registroAdapter;
import sitetech.NFCcheckPoint.Adapters.rutaSelAdapter;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Core.BluetoothPrinter;
import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.DialogHelper;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.Helpers.printHelper;
import sitetech.NFCcheckPoint.OperadorActivity;
import sitetech.NFCcheckPoint.db.Registro_Turno;
import sitetech.NFCcheckPoint.db.Registro_TurnoDao;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.RutaDao;
import sitetech.NFCcheckPoint.db.Turno;
import sitetech.routecheckapp.R;

public class HistoryFragment extends Fragment {

    private OmegaRecyclerView rlista;
    public View vista;
    private Turno turnoActual;
    Registro_TurnoDao registrosManager = AppController.daoSession.getRegistro_TurnoDao();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        vista = inflater.inflate(R.layout.operador_history_fragment, viewGroup, false);

        rlista = vista.findViewById(R.id.rlista);
        cargarLista();

        return vista;
    }

    registroAdapter dataAdapter;
    List<Registro_Turno> listaTemp;
    public void cargarLista(){
        turnoActual = Configuraciones.getTurnoAbierto();
        List<Registro_Turno> lista = registrosManager.queryBuilder()
                .where(Registro_TurnoDao.Properties.TurnoId.eq(turnoActual.getId()))
                .list();

        if (listaTemp == null) { listaTemp = lista; actualizarRegistros(lista); }
        else  if (lista.size() > listaTemp.size()) {
            actualizarRegistros(lista);
            listaTemp = lista;
        }

    }

    public void itemEditado(Registro_Turno rx){
        dataAdapter.updateData(rx);
    }

    public void actualizarRegistros(List<Registro_Turno> lista){
        final List<Registro_Turno> lnueva = lista;
        //ToastHelper.aviso(String.valueOf(lista.size()));

        dataAdapter = new registroAdapter(lnueva, new onItemClick() {
            @Override
            public void onClickItemList(View v, final int position) {
                //ToastHelper.info("Desea Reimprimirlo" + lnueva.get(position).getBus().getPlaca());
                DialogHelper.showAsk2(v, "¿Reimprimir comprobante?", "Desea Reimprimir este registro.", "Imprimir", "Cancelar", new myDialogInterface() {
                    @Override
                    public View onBuildDialog() {
                        return null;
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onResult(View vista) {
                        printHelper.imprimirRegistro(lnueva.get(position), true, true);
                    }

                });
            }


        }, new onEditClick() {
            @Override
            public void onEditItemClick(View v, int position) {
                ((OperadorActivity)getActivity()).editarRegistro(lnueva.get(position));
            }
        });


        if (rlista != null) {
            rlista.setHasFixedSize(true);
            rlista.setLayoutManager(new LinearLayoutManager(getContext()));
            rlista.setAdapter(dataAdapter);
        }
    }
}
