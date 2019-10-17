package sitetech.NFCcheckPoint.ui.historial;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import android.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.greendao.query.WhereCondition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sitetech.NFCcheckPoint.Adapters.customAdapter;
import sitetech.NFCcheckPoint.Adapters.onEditClick;
import sitetech.NFCcheckPoint.Adapters.onItemClick;
import sitetech.NFCcheckPoint.Adapters.registroAdapter;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.DialogHelper;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.Helpers.printHelper;
import sitetech.NFCcheckPoint.OperadorActivity;
import sitetech.NFCcheckPoint.db.BusDao;
import sitetech.NFCcheckPoint.db.Empresa;
import sitetech.NFCcheckPoint.db.EmpresaDao;
import sitetech.NFCcheckPoint.db.Registro_Turno;
import sitetech.NFCcheckPoint.db.Registro_TurnoDao;
import sitetech.NFCcheckPoint.db.Turno;
import sitetech.routecheckapp.R;

public class HistorySearchOP extends Fragment {
    private ImageView batraz;
    private Button bhoy;
    private Button bbuscar;
    private TextView tplaca;
    private TextView tdesde;
    private TextView thasta;
    private TextView tinfo;
    private Spinner spempresa;
    private Spinner spsel;
    private View vista;
    private RecyclerView rlista;

    private Empresa selEmpresa;
    final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    final SimpleDateFormat dbDf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.history_search_operador, container, false);

        batraz = vista.findViewById(R.id.batraz);

        cargarControles();
        Click();

        cargarEmpresas();
        return vista;
    }

    private void cargarControles(){
        bhoy = vista.findViewById(R.id.bhoy);
        bbuscar = vista.findViewById(R.id.bbuscar);
        tplaca = vista.findViewById(R.id.tplaca);
        tdesde = vista.findViewById(R.id.tdesde);
        thasta = vista.findViewById(R.id.thasta);
        tinfo = vista.findViewById(R.id.tinfo);
        spempresa = vista.findViewById(R.id.spempresa);
        spsel = vista.findViewById(R.id.spsel);
        rlista = vista.findViewById(R.id.rlista);

        tdesde.setText(df.format(new Date()));
        thasta.setText(df.format(new Date()));

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.placa_interno_array, R.layout.spinner_item_white);
        spsel.setAdapter(adapter);
    }

    private void Click(){
        batraz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
            }
        });

        tdesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date fecha = new Date();
                    if (!tdesde.getText().toString().isEmpty())
                        fecha = df.parse(tdesde.getText().toString());

                    DatePickerDialog StartTime = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            tdesde.setText(df.format(newDate.getTime()));
                        }

                    }, TimeHelper.getCalendario(fecha).get(Calendar.YEAR), TimeHelper.getCalendario(fecha).get(Calendar.MONTH), TimeHelper.getCalendario(fecha).get(Calendar.DAY_OF_MONTH));
                    StartTime.show();
                }catch (Exception e) {}


        }});

        thasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date fecha = new Date();
                    if (!thasta.getText().toString().isEmpty())
                        fecha = df.parse(thasta.getText().toString());

                    DatePickerDialog StartTime = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            thasta.setText(df.format(newDate.getTime()));
                        }

                    }, TimeHelper.getCalendario(fecha).get(Calendar.YEAR), TimeHelper.getCalendario(fecha).get(Calendar.MONTH), TimeHelper.getCalendario(fecha).get(Calendar.DAY_OF_MONTH));
                    StartTime.show();
                }catch (Exception e) {}

            }});

        bbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });

        bhoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tdesde.setText(df.format(new Date()));
                thasta.setText(df.format(new Date()));
            }
        });
    }

    private void cargarEmpresas(){
        final List<Empresa> lempresas = AppController.daoSession.getEmpresaDao().queryBuilder().where(EmpresaDao.Properties.Eliminado.eq(false)).list();
        selEmpresa = lempresas.get(0);

        spempresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selEmpresa = lempresas.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selEmpresa = null;
            }
        });

        customAdapter CustomAdapter = new customAdapter(AppController.getAppContext(), lempresas, R.layout.spinner_empresa_appbar);
        spempresa.setSelection(0);
        spempresa.setAdapter(CustomAdapter);
    }

    registroAdapter dataAdapter;
    private void buscar(){
        try {

            Date fdesde = dbDf.parse(tdesde.getText().toString() + " 00:00:00");
            Date fhasta = dbDf.parse(thasta.getText().toString()+ " 23:59:59");

            String placa = "", interno = "";
            if (spsel.getSelectedItem().equals("Placa")) placa = tplaca.getText().toString();
            if (spsel.getSelectedItem().equals("Interno")) interno = tplaca.getText().toString();

            if (selEmpresa != null) {
                List<Registro_Turno> lresultado;
                if (!fdesde.equals(fhasta)) {
                    lresultado = AppController.daoSession.getRegistro_TurnoDao().queryBuilder()
                            .where(Registro_TurnoDao.Properties.Fecha.between(fdesde, fhasta)
                            ).orderAsc(Registro_TurnoDao.Properties.Fecha).list();
                }
                else {
                    lresultado = AppController.daoSession.getRegistro_TurnoDao().queryBuilder()
                            .where(Registro_TurnoDao.Properties.Fecha.between(fdesde, fhasta)
                            ).orderAsc(Registro_TurnoDao.Properties.Fecha).list();
                }

                //ToastHelper.info("ELEMENTOS: " + lresultado.size());

                List<Registro_Turno> ltemp = new ArrayList();
                for (Registro_Turno rx : lresultado){
                    if (placa != "") {
                        if (rx.getBus().getPlaca().contains(placa))
                            if (selEmpresa != null) {
                                if (rx.getBus().getEmpresa().getId() == selEmpresa.getId())
                                    ltemp.add(rx);
                            } else ltemp.add(rx);
                    }

                    if (interno != "") {
                        if (rx.getBus().getInterno().contains(interno))
                            if (selEmpresa != null) {
                                if (rx.getBus().getEmpresa().getId() == selEmpresa.getId())
                                    ltemp.add(rx);
                            } else ltemp.add(rx);
                    }

                }

                actualizarLista(ltemp);
            }
        } catch (Exception e) {
            Log.d("ERROR EXC", e.getMessage());
        }
    }

    public void actualizarLista(List<Registro_Turno> lista){
        final List<Registro_Turno> lnueva = lista;

        if (lnueva.size() == 0) tinfo.setVisibility(View.VISIBLE);
        else tinfo.setVisibility(View.GONE);

        dataAdapter = new registroAdapter(lnueva, new onItemClick() {
            @Override
            public void onClickItemList(View v, final int position) {
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

            }
        });

        rlista.setHasFixedSize(true);
        rlista.setLayoutManager(new LinearLayoutManager(getContext()));
        rlista.setAdapter(dataAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (getActivity().getClass().equals(OperadorActivity.class))
            ((OperadorActivity)getActivity()).cerrarHistorial();
    }


}
