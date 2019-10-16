package sitetech.NFCcheckPoint.ui.operador;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import sitetech.NFCcheckPoint.Adapters.rutaSpinnerAdapter;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.OperadorActivity;
import sitetech.NFCcheckPoint.db.Registro_Turno;
import sitetech.NFCcheckPoint.db.Registro_TurnoDao;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.RutaDao;
import sitetech.routecheckapp.R;

public class RegistroEditarFragment extends Fragment {
    private View vista;
    private Button bguardar;
    private Button bcancelar;
    private ImageView batraz;

    private Spinner spruta;
    private TextView tplaca;
    private TextView tinterno;
    private TextView tempresa;
    private TextView tregistro;
    private TextView tdespacho;
    private TextView tdemorado;

    private Registro_Turno registro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            registro = buscarRegistro(getArguments().getInt("registro"));
            if (registro == null) {
                ToastHelper.error("Error al obtener el registro seleccionado");
                activityHelper.goBackStack(vista);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.historial_editar_registro, viewGroup, false);

        cargarControles();
        Click();

        if (registro != null)
            cargarInfo();
        return vista;
    }

    private void cargarControles(){
        bguardar = vista.findViewById(R.id.bguardar);
        bcancelar = vista.findViewById(R.id.bcancelar);
        batraz = vista.findViewById(R.id.batraz);

        spruta = (vista.findViewById(R.id.spruta));
        tplaca = (vista.findViewById(R.id.tplaca));
        tinterno = (vista.findViewById(R.id.tinterno));
        tempresa = (vista.findViewById(R.id.tempresa));
        tregistro = (vista.findViewById(R.id.tregistro));
        tdespacho = (vista.findViewById(R.id.tdespacho));
        tdemorado = (vista.findViewById(R.id.tdemorado));
    }

    private void cargarInfo(){
        tplaca.setText(registro.getBus().getPlaca());
        tinterno.setText(registro.getBus().getInterno());
        tempresa.setText(registro.getBus().getEmpresa().getNombre());
        tregistro.setText(TimeHelper.getDate(registro.getFecha(), "dd MMM yyyy - HH:mm:ss"));
        tdespacho.setText(registro.getDespacho());

        if (!registro.getMinAtrazado().equals("00:00:00"))
            tdemorado.setText(registro.getMinAtrazado());
        else
            tdemorado.setText(registro.getMinAdelantado());

        cargarRutas();
    }

    private void cargarRutas(){
        List<Ruta> listaRutas = AppController.daoSession.getRutaDao().queryBuilder().where(RutaDao.Properties.Eliminada.eq(false)).list();

        rutaSpinnerAdapter spinnerAdapter=new rutaSpinnerAdapter(AppController.getAppContext(), listaRutas, R.layout.ruta_sel_spinner);
        spruta.setAdapter(spinnerAdapter);
        int i=0;
        spruta.setSelection(listaRutas.indexOf(registro.getRuta()));

    }

    private void Click(){
        bguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro.setRuta((Ruta)spruta.getSelectedItem());
                AppController.daoSession.getRegistro_TurnoDao().update(registro);
                if (getActivity().getClass().equals(OperadorActivity.class))
                    ((OperadorActivity)getActivity()).cerrarEditarHistorial(registro);
            }
        });

        batraz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getClass().equals(OperadorActivity.class))
                    ((OperadorActivity)getActivity()).cerrarEditarHistorial(registro);
            }
        });

        bcancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getClass().equals(OperadorActivity.class))
                    ((OperadorActivity)getActivity()).cerrarEditarHistorial(registro);
            }
        });
    }

    private Registro_Turno buscarRegistro(int id){
        Registro_Turno rx = AppController.daoSession.getRegistro_TurnoDao().queryBuilder()
                .where(Registro_TurnoDao.Properties.Id.eq(id)).unique();
        return rx;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity().getClass().equals(OperadorActivity.class))
            ((OperadorActivity)getActivity()).cerrarEditarHistorial(registro);
    }
}

