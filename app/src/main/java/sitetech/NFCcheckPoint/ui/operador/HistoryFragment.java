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

    public void actualizarRegistros(List<Registro_Turno> lista){
        final List<Registro_Turno> lnueva = lista;
        //ToastHelper.aviso(String.valueOf(lista.size()));

        dataAdapter = new registroAdapter(lnueva, new onItemClick() {
            @Override
            public void onClickItemList(View v, final int position) {
                ToastHelper.info("Desea Reimprimirlo" + lnueva.get(position).getBus().getPlaca());
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
                        imprimir(lnueva.get(position));
                    }

                });
            }
        });

        rlista.setHasFixedSize(true);
        rlista.setLayoutManager(new LinearLayoutManager(getContext()));
        rlista.setAdapter(dataAdapter);
    }

    private void imprimir(final Registro_Turno registro){
        try {
            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice mBtDevice = btAdapter.getBondedDevices().iterator().next();   // Get first paired device

            final BluetoothPrinter mPrinter = new BluetoothPrinter(mBtDevice);

            mPrinter.connectPrinter(new BluetoothPrinter.PrinterConnectListener() {

                @Override
                public void onConnected() {
                    imprimir(mPrinter, registro);
                    //limpiarInfo();
                    //bimprimir.setVisibility(View.GONE);
                    ToastHelper.exito("Registro impreso correctamente.");

                }

                @Override
                public void onFailed() {
                    ToastHelper.error("Error al enviar la impresion, verifica la impresora.");
                    Log.d("Impresora bluetooth", "Error de conexion");
                }

            });
        }
        catch (Exception e){
            ToastHelper.error("Error al intentar imprimir. " + e.getMessage());
        }
    }

    private void imprimir(BluetoothPrinter impresora, Registro_Turno registro){
        //Bitmap logoEmpresa = BitmapFactory.decodeResource(getResources(), R.drawable.logo_empresa2);
        //impresora.setAlign(BluetoothPrinter.ALIGN_CENTER);
        //impresora.printImage(logoEmpresa);
        //impresora.addNewLine();

        //impresora.setBold(true);
        impresora.printText("PUNTO DE CONTROL"); impresora.addNewLine();
        //impresora.setAlign(BluetoothPrinter.ALIGN_LEFT);
        //impresora.setBold(false);
        impresora.addNewLine();
        impresora.printText(registro.getRuta().getNombre().toUpperCase());

        impresora.addNewLine();
        //impresora.setBold(true);
        impresora.printText("TIEMPO AL PUNTO: " + registro.getDespacho()); impresora.addNewLine();

        impresora.printText("DETALLE DE CONTROL");
        //impresora.setBold(false);
        //impresora.setLineSpacing(1);
        impresora.addNewLine();

        String empresa = "ninguna";
        try {
            empresa = registro.getBus().getEmpresa().getNombre().toUpperCase();
        } catch (Exception e){ empresa = "Error entity detached"; }

        imprimirValor(impresora, "EMPRESA ", empresa);
        impresora.addNewLine();
        imprimirValor(impresora, "PLACA ", registro.getBus().getPlaca().toUpperCase());
        impresora.addNewLine();
        imprimirValor(impresora, "INTERNO ", registro.getBus().getInterno().toUpperCase());
        impresora.addNewLine();
        imprimirValor(impresora, "FECHA ", TimeHelper.getDate(registro.getFecha()));
        impresora.addNewLine();
        imprimirValor(impresora, "HORA DESPACHO ", registro.getDespacho());
        impresora.addNewLine();
        imprimirValor(impresora, "HORA REGISTRO ", TimeHelper.getTime(registro.getFecha()));

        impresora.addNewLine();
        if (registro.getMinAdelantado().equals("00:00:00"))
            imprimirValor(impresora, "Demorado ", registro.getMinAtrazado());
        else
            imprimirValor(impresora, "Adelantado ", registro.getMinAdelantado());

        impresora.addNewLine();
        imprimirValor(impresora, "Dato extra 2: ", "");

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
}
