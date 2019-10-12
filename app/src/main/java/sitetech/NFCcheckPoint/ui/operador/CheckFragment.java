package sitetech.NFCcheckPoint.ui.operador;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.hmspicker.HmsPickerBuilder;
import com.codetroopers.betterpickers.hmspicker.HmsPickerDialogFragment;
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
import sitetech.NFCcheckPoint.Core.BluetoothPrinter;
import sitetech.NFCcheckPoint.Core.calculos;
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
import sitetech.NFCcheckPoint.db.Horario;
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
    private Button bimprimir;

    private TextView tplaca, tinterno, tempresa, thoraregistro, tultimocheck, tminutos, tinfo1;
    private EditText tdespacho;

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
        bimprimir = vista.findViewById(R.id.bimprimir);

        //Datos de la tarjeta leida
        tplaca = vista.findViewById(R.id.tplaca);
        tempresa = vista.findViewById(R.id.tempresa);
        tinterno = vista.findViewById(R.id.tinterno);
        thoraregistro = vista.findViewById(R.id.thoraregistro);
        tultimocheck = vista.findViewById(R.id.tultimocheck);
        tdespacho = vista.findViewById(R.id.tdespacho);
        tminutos = vista.findViewById(R.id.tminutos);
        tinfo1 = vista.findViewById(R.id.tinfo1);

        bguardar.setVisibility(View.GONE);
    }

    private Registro_Turno ultimoRegistro;
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
                nuevoRegistro.setDespacho(tdespacho.getText().toString());

                Long difTiempo = TimeHelper.calcularDiferencia(tdespacho.getText().toString(), tfecha.getText().toString());

                if (difTiempo > 0)
                    nuevoRegistro.setMinAtrazado(TimeHelper.segundosahoras(difTiempo));
                else
                    nuevoRegistro.setMinAdelantado(TimeHelper.segundosahoras(difTiempo));

                nuevoRegistro.setEliminado(false);
                registrosManager.insert(nuevoRegistro);

                ToastHelper.exito("Bus registrado con exito.");
                ultimoRegistro = nuevoRegistro;
            }
        });

        bimprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice mBtDevice = btAdapter.getBondedDevices().iterator().next();   // Get first paired device

                final BluetoothPrinter mPrinter = new BluetoothPrinter(mBtDevice);
                mPrinter.connectPrinter(new BluetoothPrinter.PrinterConnectListener() {

                    @Override
                    public void onConnected() {
                        imprimir(mPrinter);
                        limpiarInfo();
                        ToastHelper.exito("Registro impreso correctamente.");
                    }

                    @Override
                    public void onFailed() {
                        ToastHelper.error("Error al enviar la impresion, verifica la impresora.");
                        Log.d("Impresora bluetooth", "Error de conexion");
                    }

                });
            }
        });

        tdespacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String horaActual = tdespacho.getText().toString();
                HmsPickerBuilder hpb = new HmsPickerBuilder()
                        .setFragmentManager(getActivity().getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                hpb.show();

                hpb.setTimeInSeconds(TimeHelper.getTime(horaActual).intValue());
                hpb.addHmsPickerDialogHandler(new HmsPickerDialogFragment.HmsPickerDialogHandlerV2() {
                    @Override
                    public void onDialogHmsSet(int reference, boolean isNegative, int hours, int minutes, int seconds) {
                        tdespacho.setText(TimeHelper.formatTime(hours, minutes, seconds));
                    }
                });
            }
        });
    }

    private void imprimir(BluetoothPrinter impresora){
        Bitmap logoEmpresa = BitmapFactory.decodeResource(getResources(), R.drawable.logo_empresa);
        impresora.setAlign(BluetoothPrinter.ALIGN_CENTER);
        impresora.printImage(logoEmpresa);
        impresora.addNewLine();

        impresora.setBold(true);
        impresora.printText("PUNTO DE CONTROL");
        impresora.setAlign(BluetoothPrinter.ALIGN_LEFT);
        impresora.setBold(false);
        impresora.printText(ultimoRegistro.getRuta().getNombre().toUpperCase());

        impresora.setBold(true);
        impresora.printText("TIEMPO AL PUNTO: Como se calcula ?");

        impresora.addNewLine();
        impresora.printText("DETALLE DE CONTROL");
        impresora.setBold(false);
        impresora.setLineSpacing(1);
        imprimirValor(impresora, "EMPRESA", ultimoRegistro.getBus().getEmpresa().getNombre().toUpperCase());
        imprimirValor(impresora, "PLACA", ultimoRegistro.getBus().getPlaca().toUpperCase());
        imprimirValor(impresora, "INTERNO", ultimoRegistro.getBus().getInterno().toUpperCase());
        imprimirValor(impresora, "FECHA", TimeHelper.getDate(ultimoRegistro.getFecha()));
        imprimirValor(impresora, "HORA DESPACHO", ultimoRegistro.getDespacho());
        imprimirValor(impresora, "HORA REGISTRO", TimeHelper.getTime(ultimoRegistro.getFecha()));

        impresora.addNewLine();
        imprimirValor(impresora, tinfo1.getText().toString(), tminutos.getText().toString());
        imprimirValor(impresora, "Dato extra 2", "");

        impresora.printLine();
        impresora.feedPaper();

        impresora.finish();
    }

    private void imprimirValor(BluetoothPrinter print, String campo, String valor){
        print.setAlign(BluetoothPrinter.ALIGN_LEFT);
        print.printText(campo);
        print.setAlign(BluetoothPrinter.ALIGN_RIGHT);
        print.printText(valor);
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
                    ToastHelper.aviso("Debe de seleccionar una ruta, para poder escanear una tarjeta.");
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
        ToastHelper.exito("UF LLEGO HASTA AQUI");
        infoTarjeta = nfcHelper.getnfcData(mensaje);
        if (infoTarjeta != null)
            cargarInfo();
    }

    private void leerTarjeta(){
        OperadorActivity activity = (OperadorActivity) getActivity();
        //activity.leerNFC();
        callBackNfc(nfcPrueba);
    }


    private void cargarInfo(){
        Horario hora = calculos.getHorarioCercano(rutaSeleccionada, new Date());

        if (hora != null) {
            infoTarjeta.setUltimoCheck(new Date());
            fechaCheck = new Date();

            tplaca.setText(infoTarjeta.getBus().getPlaca());
            tinterno.setText(infoTarjeta.getBus().getInterno());
            tempresa.setText(infoTarjeta.getEmpresa().getNombre());
            thoraregistro.setText(TimeHelper.getTime(fechaCheck));
            tultimocheck.setText(TimeHelper.getDate(infoTarjeta.getUltimoCheck(), "dd MMM yyyy - HH:mm:ss"));

            if (TimeHelper.esFindeSemana(new Date()) || calculos.esDiaFestivo(new Date()))
                tdespacho.setText(hora.getHoraFestivoHasta());
            else
                tdespacho.setText(hora.getHoraHasta());

            Long difTiempo = TimeHelper.calcularDiferencia(tdespacho.getText().toString(), tfecha.getText().toString());
            tminutos.setText(TimeHelper.segundosahoras(difTiempo));
            if (difTiempo > 0)
                tinfo1.setText("Demorado");
            else
                tinfo1.setText("Adelantado");


            bguardar.setVisibility(View.VISIBLE);
        }
        else
            ToastHelper.error("Error al intentar obtener el horario de despacho cercano.");
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

