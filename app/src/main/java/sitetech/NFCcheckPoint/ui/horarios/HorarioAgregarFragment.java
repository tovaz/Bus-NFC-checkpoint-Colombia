package sitetech.NFCcheckPoint.ui.horarios;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.HorarioDao;
import sitetech.NFCcheckPoint.ui.empresas.EmpresaAgregarFragment;
import sitetech.NFCcheckPoint.ui.empresas.EmpresasFragment;
import sitetech.routecheckapp.R;

public class HorarioAgregarFragment extends Fragment implements Serializable {
    private static final String MAINFRAGMENT_KEY = "mainFragment";
    private HorariosFragment mainFragment;
    private View vista;

    private TextView ttitulo;
    private EditText tnombre;
    private TextView talpunto;
    private TextView talpuntoFestivo;

    private ImageView bhora;
    private ImageView bhoraFestivo;
    private ImageView bhora_hasta;
    private ImageView bhoraFestivo_hasta;

    private EditText thora;
    private EditText thoraFestivo;
    private EditText thoraHasta;
    private EditText thoraFestivoHasta;

    private Button bcancelar;
    private Button bguardar;

    public HorarioAgregarFragment() { }

    public static EmpresaAgregarFragment newInstance(EmpresasFragment _main) {
        EmpresaAgregarFragment fragment = new EmpresaAgregarFragment();
        Bundle args = new Bundle();
        args.putSerializable(MAINFRAGMENT_KEY, _main);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mainFragment = (HorariosFragment) getArguments().getSerializable(MAINFRAGMENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista =  inflater.inflate(R.layout.horario_agregar_fragment, container, false);
        cargarControles();
        onClick();

        if (mainFragment.Itemseleccionado != null)
            cargarInfo();
        return vista;
    }

    private void cargarInfo(){
        Horario horario = mainFragment.Itemseleccionado;
        ttitulo.setText("Modificar Horario");
        talpunto.setText(horario.getTiempoNormal().toString());
        talpuntoFestivo.setText(horario.getTiempoDiaFestivo().toString());

        tnombre.setText(horario.getNombre());
        thora.setText(horario.getHoraDesde() == null ? "" : horario.getHoraDesde());
        thoraHasta.setText(horario.getHoraHasta() == null ? "" : horario.getHoraHasta());
        thoraFestivo.setText(horario.getHoraFestivoDesde() == null ? "" : horario.getHoraFestivoDesde());
        thoraFestivoHasta.setText(horario.getHoraFestivoHasta() == null ? "" : horario.getHoraFestivoHasta());
    }

    private void cargarControles(){
        ttitulo = vista.findViewById(R.id.ttitulo); // Titulo
        talpunto = vista.findViewById(R.id.talpunto);
        tnombre = vista.findViewById(R.id.tnombre);
        talpuntoFestivo = vista.findViewById(R.id.talpuntoFestivo);
        bhora = vista.findViewById(R.id.bhora);
        bhoraFestivo = vista.findViewById(R.id.bhoraFestivo);
        bhora_hasta = vista.findViewById(R.id.bhora_hasta);
        bhoraFestivo_hasta = vista.findViewById(R.id.bhoraFestivoHasta);

        thora = vista.findViewById(R.id.thora);
        thoraFestivo = vista.findViewById(R.id.thoraFestivo);
        thoraHasta = vista.findViewById(R.id.thoraHasta);
        thoraFestivoHasta = vista.findViewById(R.id.thoraFestivoHasta);

        bcancelar = vista.findViewById(R.id.bcancelar);
        bguardar = vista.findViewById(R.id.bguardar);
    }

    private void onClick(){
        bhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dx = TimeHelper.separarString(thora.getText().toString());
                Calendar mcurrentTime = Calendar.getInstance();
                TimePickerDialog picker = new TimePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                thora.setText(TimeHelper.formatTime(sHour, sMinute, 0));
                            }
                        }, dx.getHours(), dx.getMinutes(), true);
                picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                picker .show();
            }
        });

        bhoraFestivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dx = TimeHelper.separarString(thoraFestivo.getText().toString());
                Calendar mcurrentTime = Calendar.getInstance();
                TimePickerDialog picker = new TimePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                thoraFestivo.setText(TimeHelper.formatTime(sHour, sMinute, 0));
                            }
                        }, dx.getHours(), dx.getMinutes(), true);
                picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                picker .show();

            }
        });

        bhora_hasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dx = TimeHelper.separarString(thoraHasta.getText().toString());
                Calendar mcurrentTime = Calendar.getInstance();
                TimePickerDialog picker = new TimePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                thoraHasta.setText(TimeHelper.formatTime(sHour, sMinute, 59));
                                Long diferencia = TimeHelper.calcularDiferencia(thora.getText().toString(), thoraHasta.getText().toString());
                                talpunto.setText(TimeHelper.segundosahoras(diferencia));
                            }
                        }, dx.getHours(), dx.getMinutes(), true);
                picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                picker .show();
            }
        });

        bhoraFestivo_hasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dx = TimeHelper.separarString(thoraFestivoHasta.getText().toString());
                Calendar mcurrentTime = Calendar.getInstance();
                TimePickerDialog picker = new TimePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                thoraFestivoHasta.setText(TimeHelper.formatTime(sHour, sMinute, 59));
                                Long diferencia = TimeHelper.calcularDiferencia(thoraFestivo.getText().toString(), thoraFestivoHasta.getText().toString());
                                talpuntoFestivo.setText(TimeHelper.segundosahoras(diferencia));
                            }
                        }, dx.getHours(), dx.getMinutes(), true);
                picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                picker .show();
            }
        });

        bcancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHelper.goBackStack(vista);
            }
        });

        bguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HorarioDao horarioManager = AppController.daoSession.getHorarioDao();
                long inserted = -1;

                Horario horario = new Horario();
                if (mainFragment.Itemseleccionado != null)
                    horario = mainFragment.Itemseleccionado;

                horario.setNombre(tnombre.getText().toString());
                horario.setHoraDesde(thora.getText().toString());
                horario.setHoraFestivoDesde(thoraFestivo.getText().toString());
                horario.setHoraHasta(thoraHasta.getText().toString());
                horario.setHoraFestivoHasta(thoraFestivoHasta.getText().toString());
                horario.setTiempoNormal(talpunto.getText().toString());

                horario.setTiempoDiaFestivo(talpuntoFestivo.getText().toString());

                if (mainFragment.Itemseleccionado == null) {
                    horario.setEliminado(false);
                    inserted = horarioManager.insert(horario);
                }
                else {
                    horarioManager.update(horario);
                    inserted = horario.getId();
                }

                if (inserted > -1) {
                    mainFragment.updateList(horario);
                    activityHelper.goBackStack(vista);
                }
            }
        });

    }
}
