package sitetech.NFCcheckPoint.ui.operador;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.Adapters.rutaSelAdapter;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Core.calculos;
import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.Listener;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.nfcData;
import sitetech.NFCcheckPoint.Helpers.nfcHelper;
import sitetech.NFCcheckPoint.Helpers.printHelper;
import sitetech.NFCcheckPoint.OperadorActivity;
import sitetech.NFCcheckPoint.db.Bus;
import sitetech.NFCcheckPoint.db.BusDao;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.Registro_Turno;
import sitetech.NFCcheckPoint.db.Registro_TurnoDao;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.RutaDao;
import sitetech.NFCcheckPoint.db.Turno;
import sitetech.routecheckapp.R;

import static sitetech.NFCcheckPoint.dbHelpers.RegistrosManager.getRegistroAnterior;

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
    private final String nfcPrueba = "{\"bus\":{\"eliminado\":false,\"empresa\":{\"eliminado\":false,\"id\":2,\"nombre\":\"Empresa ejemplo 2\",\"telefono\":\"0000-9999\"},\"empresaId\":2,\"id\":1,\"interno\":\"0000\",\"placa\":\"000-000\"},\"empresa\":{\"eliminado\":false,\"id\":2,\"nombre\":\"Empresa ejemplo 2\",\"telefono\":\"0000-9999\"},\"ultimoCheck\":\"Oct 11, 2019 11:58:30 PM\"}";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        vista =  inflater.inflate(R.layout.operador_check_fragment, viewGroup, false);

        cargarControles();
        cargarLista();
        showFechayHora();
        Click();
        pruebas();
        leerTarjeta();

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
        bimprimir.setVisibility(View.GONE);
    }

    private Registro_Turno ultimoRegistro;
    private boolean yaGuardo=false;
    private void Click(){
        bguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bimprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!yaGuardo) {
                    final Registro_Turno rx = guardarRegistro();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            printHelper.imprimirRegistro(rx, true, false); //IMPRIME Y GUARDA EL REGISTRO
                            final Registro_Turno registroAnterior = getRegistroAnterior();
                            final Handler handler2 = new Handler();
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    printHelper.imprimirRegistro(registroAnterior, false, false);
                                    limpiarInfo();
                                }
                            }, 1000);
                        }
                    }, 1000);

                }
            }
        });

        tdespacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tdespacho.getText().toString().equals("")) {
                    Date dx = TimeHelper.separarString(tdespacho.getText().toString());

                    Calendar mcurrentTime = Calendar.getInstance();
                    TimePickerDialog picker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    tdespacho.setText(TimeHelper.formatTime(sHour, sMinute, 0));
                                }
                            }, dx.getHours(), dx.getMinutes(), true);
                    picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    picker.show();
                }
            }
        });
    }

    private Registro_Turno guardarRegistro(){
        Turno tn = Configuraciones.getTurnoAbierto();
        Registro_Turno nuevoRegistro = new Registro_Turno();
        nuevoRegistro.setFecha(fechaCheck);
        nuevoRegistro.setBus(infoTarjeta.getBus());
        nuevoRegistro.setRuta(rutaSeleccionada);
        nuevoRegistro.setTurno(tn);
        nuevoRegistro.setUsuario(Configuraciones.getUsuarioLog(getContext()));
        nuevoRegistro.setDespacho(tdespacho.getText().toString());
        nuevoRegistro.setPuntoControl(Configuraciones.getPuntodeControl(getContext()));

        Long difTiempo = TimeHelper.calcularDiferencia(tdespacho.getText().toString(), thoraregistro.getText().toString());

        nuevoRegistro.setMinAtrazado("00:00:00");
        nuevoRegistro.setMinAdelantado("00:00:00");
        if (difTiempo > 0) {
            if (difTiempo > 120)
                tn.setTotalDemorados(tn.getTotalDemorados()+1);
            else
                tn.setTotalAtiempo(tn.getTotalAtiempo() + 1);

            nuevoRegistro.setMinAtrazado(TimeHelper.segundosahoras(difTiempo));
        }
        else {
            if (difTiempo < 120)
                tn.setTotalAdelantados(tn.getTotalAdelantados()+1); // Atrazados = Adelantado --- error de campos
            else
                tn.setTotalAtiempo(tn.getTotalAtiempo() + 1);

            nuevoRegistro.setMinAdelantado(TimeHelper.segundosahoras(difTiempo));
        }

        nuevoRegistro.setEliminado(false);
        registrosManager.insert(nuevoRegistro);

        ToastHelper.exito("Bus registrado con exito.");
        bguardar.setVisibility(View.GONE);

        yaGuardo = true;

        return nuevoRegistro;
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
                //ToastHelper.info("RUTA SELECCIONADA : " + lista.get(position).getNombre().toString());
                Configuraciones.setRutaDefault(getContext(), lista.get(position));
                rutaSeleccionada = lista.get(position);
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
        //ToastHelper.exito("UF LLEGO HASTA AQUI");
        infoTarjeta = nfcHelper.getnfcData(mensaje);
        if (infoTarjeta != null)
            cargarInfo();
    }

    public void callBackNfcUid(String mensaje){
        if (mensaje != null){
            Bus bx = AppController.daoSession.getBusDao().queryBuilder().where(BusDao.Properties.TagNfc.eq(mensaje), BusDao.Properties.Eliminado.eq(false)).unique();
            limpiarInfo();
            if (bx != null) {
                infoTarjeta = new nfcData(bx);
                cargarInfo();
            }
            else
                ToastHelper.error("Tarjeta no asignada a ningun bus.");
        }
        else
            ToastHelper.error("Error al leer la tarjeta");
    }

    private void leerTarjeta(){
        OperadorActivity activity = (OperadorActivity) getActivity();
        activity.leerNFC(this);
        //Log.d("JSON PRUEBA", nfcHelper.convertnfcData(new nfcData(AppController.daoSession.getBusDao().loadAll().get(0))));
        //callBackNfc(nfcPrueba);
    }


    private void cargarInfo(){
        yaGuardo = false;
        Horario hora = calculos.getHorarioCercano(rutaSeleccionada, new Date());

        if (hora != null) {
            //infoTarjeta.setUltimoCheck(new Date());
            fechaCheck = new Date();

            tplaca.setText(infoTarjeta.getBus().getPlaca());
            tinterno.setText(infoTarjeta.getBus().getInterno());
            tempresa.setText(infoTarjeta.getEmpresa().getNombre());
            thoraregistro.setText(TimeHelper.getTime(fechaCheck));
            //tultimocheck.setText(TimeHelper.getDate(infoTarjeta.getUltimoCheck(), "dd MMM yyyy - HH:mm:ss"));

            if (TimeHelper.esFindeSemana(new Date()) || calculos.esDiaFestivo(new Date()))
                tdespacho.setText(hora.getHoraFestivoHasta());
            else
                tdespacho.setText(hora.getHoraHasta());

            Long difTiempo = TimeHelper.calcularDiferencia(tdespacho.getText().toString(), thoraregistro.getText().toString());
            tminutos.setText(TimeHelper.segundosahoras(difTiempo));
            if (difTiempo > 0)
                tinfo1.setText("Demorado");
            else {
                tminutos.setText(TimeHelper.segundosahoras(difTiempo*-1));
                tinfo1.setText("Adelantado");
            }

            bimprimir.setVisibility(View.VISIBLE);
            //bguardar.setVisibility(View.VISIBLE);
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
        tdespacho.setText("");
        tminutos.setText("");
    }

    @Override
    public void onDialogDisplayed() {

    }

    @Override
    public void onDialogDismissed() {

    }

}

