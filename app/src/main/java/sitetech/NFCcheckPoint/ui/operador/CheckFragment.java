package sitetech.NFCcheckPoint.ui.operador;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.expandable_recycler_view.OmegaExpandableRecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.Adapters.rutaAdapter;
import sitetech.NFCcheckPoint.Adapters.rutaSelAdapter;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.Listener;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.Helpers.nfcData;
import sitetech.NFCcheckPoint.Helpers.nfcHelper;
import sitetech.NFCcheckPoint.MainActivity;
import sitetech.NFCcheckPoint.OperadorActivity;
import sitetech.NFCcheckPoint.db.BusDao;
import sitetech.NFCcheckPoint.db.Registro_Turno;
import sitetech.NFCcheckPoint.db.Registro_TurnoDao;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.RutaDao;
import sitetech.NFCcheckPoint.db.Turno;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.NFCcheckPoint.ui.rutas.RutaAgregarFragment;
import sitetech.routecheckapp.R;

public class CheckFragment extends Fragment implements Listener {
    public RecyclerView rlista;
    public View vista;
    private TextView tfecha, tusuario;
    private Button bguardar;
    private Button bpruebas;

    private TextView tplaca, tinterno, tempresa, thoraregistro, tultimocheck, tjustificacion;

    private Registro_TurnoDao registrosManager = AppController.daoSession.getRegistro_TurnoDao();

    private Turno turno;
    private Date fechaCheck;
    private Ruta rutaSeleccionada;
    private final String nfcPrueba = "{\"bus\":{\"eliminado\":false,\"empresa\":{\"eliminado\":false,\"id\":1,\"nombre\":\"Tacana\",\"telefono\":\"565959\"},\"empresaId\":1,\"id\":1,\"interno\":\"4694876\",\"placa\":\"1736HGY\"},\"empresa\":{\"eliminado\":false,\"id\":1,\"nombre\":\"Tacana\",\"telefono\":\"565959\"},\"ultimoCheck\":\"Oct 8, 2019 4:15:57 PM\"}";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        vista =  inflater.inflate(R.layout.operador_check_fragment, viewGroup, false);

        cargarControles();
        cargarLista();
        showFechayHora();
        Click();
        pruebas();
        return vista;
    }

    private void cargarControles(){
        rlista = vista.findViewById(R.id.rlista);
        tfecha = vista.findViewById(R.id.tfecha);
        tusuario = vista.findViewById(R.id.tusuario);
        bguardar = vista.findViewById(R.id.bguardar);
        bpruebas = vista.findViewById(R.id.bpruebas);

        //Datos de la tarjeta leida
        tplaca = vista.findViewById(R.id.tplaca);
        tempresa = vista.findViewById(R.id.tempresa);
        tinterno = vista.findViewById(R.id.tinterno);
        thoraregistro = vista.findViewById(R.id.thoraregistro);
        tultimocheck = vista.findViewById(R.id.tultimocheck);
        tjustificacion = vista.findViewById(R.id.tjustificacion);

        bguardar.setVisibility(View.GONE);
    }

    private void Click(){
        bguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registro_Turno nuevoRegistro = new Registro_Turno();
                nuevoRegistro.setFecha(fechaCheck);
                nuevoRegistro.setBus(infoTarjeta.getBus());
                nuevoRegistro.setRuta(rutaSeleccionada);
                nuevoRegistro.setTurno(Configuraciones.getTurnoAbierto());
                nuevoRegistro.setUsuario(Configuraciones.getUsuarioLog(getContext()));
                nuevoRegistro.setEliminado(false);
                registrosManager.insert(nuevoRegistro);

                ToastHelper.exito("Bus registrado con exito.");
                limpiarInfo();
            }
        });
    }

    private void pruebas(){
        bpruebas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Log.d("NFC DATA EXAMPLE: ", nfcHelper.convertnfcData(new nfcData(
                        AppController.daoSession.getBusDao().queryBuilder().where(BusDao.Properties.Id.eq(1)).unique()
                )));*/

                if (rutaSeleccionada != null)
                    leerTarjeta();
                else
                    ToastHelper.exito("Debe de seleccionar una ruta, para poder escanear una tarjeta.");
            }
        });
    }


    private void showFechayHora(){
        tfecha.setText(TimeHelper.getDate(new Date()));
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
                ToastHelper.info("RUTA SELECCIONADA : " + lista.get(position).getNombre().toString());
                Configuraciones.setRutaDefault(getContext(), lista.get(position));
            }
        });

        rlista.setHasFixedSize(true);
        rlista.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rlista.setAdapter(dataAdapter);

        Ruta rx = Configuraciones.getRutaDefault(getContext());
        if (rx != null) { // SELECCIONAMOS EL ITEM QUE ANTES TENIAMOS
            dataAdapter.setSelectedItem(rx);
            rutaSeleccionada = rx;
        }

    }


    /******************************************************************************/
    nfcData infoTarjeta;

    public void callBackNfc(String mensaje){
        ToastHelper.exito("LLEGO HASTA AQUI");
        infoTarjeta = nfcHelper.getnfcData(mensaje);
        if (infoTarjeta != null)
            cargarInfo();
    }

    private void leerTarjeta(){
        OperadorActivity activity = (OperadorActivity) getActivity();
        activity.leerNFC();
    }

    private void cargarInfo(){
        infoTarjeta.setUltimoCheck(new Date());
        fechaCheck = new Date();

        tplaca.setText(infoTarjeta.getBus().getPlaca());
        tinterno.setText(infoTarjeta.getBus().getInterno());
        tempresa.setText(infoTarjeta.getEmpresa().getNombre());
        thoraregistro.setText(TimeHelper.getTime(fechaCheck));
        tultimocheck.setText(TimeHelper.getDate(infoTarjeta.getUltimoCheck(), "dd MMM yyyy - HH:mm:ss"));

        bguardar.setVisibility(View.VISIBLE);
    }

    private void limpiarInfo(){
        tplaca.setText("");
        tinterno.setText("");
        tempresa.setText("");
        thoraregistro.setText("");
        tultimocheck.setText("");

        bguardar.setVisibility(View.GONE);
    }

    @Override
    public void onDialogDisplayed() {

    }

    @Override
    public void onDialogDismissed() {

    }

}

