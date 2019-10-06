package sitetech.NFCcheckPoint.ui.horarios;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.HorarioDao;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.RutaDao;
import sitetech.NFCcheckPoint.ui.empresas.EmpresaAgregarFragment;
import sitetech.NFCcheckPoint.ui.empresas.EmpresasFragment;
import sitetech.NFCcheckPoint.ui.rutas.RutasFragment;
import sitetech.routecheckapp.R;

public class HorarioAgregarFragment extends Fragment {
    private static final String MAINFRAGMENT_KEY = "mainFragment";
    private HorariosFragment mainFragment;
    private View vista;

    private TextView ttitulo;
    private EditText tmaxMinutos;
    private EditText tmaxMinutosFestivos;
    private Button bhora;
    private Button bhoraFestivo;

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
        tmaxMinutos.setText(horario.getMaxMinutos().toString());
        tmaxMinutosFestivos.setText(horario.getMaxMinutosFestivo().toString());

        bhora.setText(horario.getHora().toString());
        bhoraFestivo.setText(horario.getHoraFestivo().toString());
    }

    private void cargarControles(){
        ttitulo = vista.findViewById(R.id.ttitulo); // Titulo
        tmaxMinutos = vista.findViewById(R.id.tmaxMinutos);
        tmaxMinutosFestivos = vista.findViewById(R.id.tmaxMinutosFestivos);
        bhora = vista.findViewById(R.id.bhora);
        bhoraFestivo = vista.findViewById(R.id.bhoraFestivo);

        bcancelar = vista.findViewById(R.id.bcancelar);
        bguardar = vista.findViewById(R.id.bguardar);
    }

    private void onClick(){
        bhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String horaActual = bhora.getText().toString();
                TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        bhora.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 9, 0, false);
                if (!horaActual.isEmpty()) timePicker.updateTime(Integer.parseInt(horaActual.substring(0, 2)), Integer.parseInt(horaActual.substring(3)));
                timePicker.show();
            }
        });

        bhoraFestivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String horaActual = bhoraFestivo.getText().toString();
                TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        bhoraFestivo.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 9, 0, false);
                if (!horaActual.isEmpty()) timePicker.updateTime(Integer.parseInt(horaActual.substring(0, 2)), Integer.parseInt(horaActual.substring(3)));
                timePicker.show();
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

                //horario.setNombre(tnombre.getText().toString());

                horario.setHora(bhora.getText().toString());
                horario.setMaxMinutos(Integer.parseInt(tmaxMinutos.getText().toString()));
                horario.setHoraFestivo(bhoraFestivo.getText().toString());
                horario.setMaxMinutosFestivo(Integer.parseInt(tmaxMinutosFestivos.getText().toString()));

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
