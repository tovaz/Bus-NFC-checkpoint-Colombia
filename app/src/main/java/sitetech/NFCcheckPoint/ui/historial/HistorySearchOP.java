package sitetech.NFCcheckPoint.ui.historial;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import sitetech.NFCcheckPoint.Adapters.customAdapter;
import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.OperadorActivity;
import sitetech.NFCcheckPoint.db.Empresa;
import sitetech.NFCcheckPoint.db.EmpresaDao;
import sitetech.routecheckapp.R;

public class HistorySearchOP extends Fragment {
    private ImageView batraz;
    private Button bhoy;
    private Button bbuscar;
    private TextView tplaca;
    private TextView tdesde;
    private TextView thasta;
    private Spinner spempresa;
    private Spinner spsel;
    private View vista;

    private Empresa selEmpresa;
    final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

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
        spempresa = vista.findViewById(R.id.spempresa);
        spsel = vista.findViewById(R.id.spsel);

        tdesde.setText(df.format(new Date()));
        thasta.setText(df.format(new Date()));
    }

    private void Click(){
        batraz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHelper.goBackStack(v);
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
        spempresa.setAdapter(CustomAdapter);
    }




    @Override
    public void onStop() {
        super.onStop();
        ((OperadorActivity)getActivity()).cerrarHistorial();
    }


}
