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

import com.codetroopers.betterpickers.hmspicker.HmsPickerBuilder;
import com.codetroopers.betterpickers.timepicker.TimePickerBuilder;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;

import java.io.Serializable;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.db.Horario;
import sitetech.NFCcheckPoint.db.HorarioDao;
import sitetech.NFCcheckPoint.db.Ruta;
import sitetech.NFCcheckPoint.db.RutaDao;
import sitetech.NFCcheckPoint.ui.empresas.EmpresaAgregarFragment;
import sitetech.NFCcheckPoint.ui.empresas.EmpresasFragment;
import sitetech.NFCcheckPoint.ui.rutas.RutasFragment;
import sitetech.routecheckapp.R;

public class HorarioAgregarFragment extends Fragment implements Serializable {
    private static final String MAINFRAGMENT_KEY = "mainFragment";
    private HorariosFragment mainFragment;
    private View vista;

    private TextView ttitulo;
    private EditText tnombre;
    private EditText tmaxMinutos;
    private EditText tmaxMinutosFestivos;

    private Button bhora;
    private Button bhoraFestivo;
    private Button bhora_hasta;
    private Button bhoraFestivo_hasta;

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
        tmaxMinutos.setText(horario.getMaxMinutos().toString());
        tmaxMinutosFestivos.setText(horario.getMaxMinutosFestivo().toString());

        tnombre.setText(horario.getNombre());
        thora.setText(horario.getHoraDesde() == null ? "" : horario.getHoraDesde());
        thoraHasta.setText(horario.getHoraHasta() == null ? "" : horario.getHoraHasta());
        thoraFestivo.setText(horario.getHoraFestivoDesde() == null ? "" : horario.getHoraFestivoDesde());
        thoraFestivoHasta.setText(horario.getHoraFestivoHasta() == null ? "" : horario.getHoraFestivoHasta());
    }

    private void cargarControles(){
        ttitulo = vista.findViewById(R.id.ttitulo); // Titulo
        tmaxMinutos = vista.findViewById(R.id.tmaxMinutos);
        tnombre = vista.findViewById(R.id.tnombre);
        tmaxMinutosFestivos = vista.findViewById(R.id.tmaxMinutosFestivos);
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
                final String horaActual = thora.getText().toString();
                /*TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        thora.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 9, 0, false);
                if (!horaActual.isEmpty()) timePicker.updateTime(Integer.parseInt(horaActual.substring(0, 2)), Integer.parseInt(horaActual.substring(3)));
                timePicker.show();*/

                TimePickerBuilder tpb = new TimePickerBuilder()
                        .setFragmentManager(getFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                tpb.show();
                tpb.addTimePickerDialogHandler(new TimePickerDialogFragment.TimePickerDialogHandler() {
                    @Override
                    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
                        thora.setText(TimeHelper.formatTime(hourOfDay, minute));
                    }
                });

            }
        });

        bhoraFestivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String horaActual = thoraFestivo.getText().toString();
                /*TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        thoraFestivo.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 9, 0, false);
                if (!horaActual.isEmpty()) timePicker.updateTime(Integer.parseInt(horaActual.substring(0, 2)), Integer.parseInt(horaActual.substring(3)));
                timePicker.show();*/

                TimePickerBuilder tpb = new TimePickerBuilder().setFragmentManager(getFragmentManager()).setStyleResId(R.style.BetterPickersDialogFragment);
                tpb.show();
                tpb.addTimePickerDialogHandler(new TimePickerDialogFragment.TimePickerDialogHandler() {
                        @Override
                        public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
                            thoraFestivo.setText(TimeHelper.formatTime(hourOfDay, minute));
                        }
                    });
            }
        });

        bhora_hasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String horaActual = thoraHasta.getText().toString();
                TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        thoraHasta.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 9, 0, false);
                if (!horaActual.isEmpty()) timePicker.updateTime(Integer.parseInt(horaActual.substring(0, 2)), Integer.parseInt(horaActual.substring(3)));
                timePicker.show();
                 */
                TimePickerBuilder tpb = new TimePickerBuilder().setFragmentManager(getFragmentManager()).setStyleResId(R.style.BetterPickersDialogFragment);
                tpb.show();
                tpb.addTimePickerDialogHandler(new TimePickerDialogFragment.TimePickerDialogHandler() {
                    @Override
                    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
                        thoraHasta.setText(TimeHelper.formatTime(hourOfDay, minute));
                    }
                });
            }
        });

        bhoraFestivo_hasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String horaActual = thoraFestivoHasta.getText().toString();
                TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        thoraFestivoHasta.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 9, 0, false);
                if (!horaActual.isEmpty()) timePicker.updateTime(Integer.parseInt(horaActual.substring(0, 2)), Integer.parseInt(horaActual.substring(3)));

                timePicker.show();
                 */
                TimePickerBuilder tpb = new TimePickerBuilder().setFragmentManager(getFragmentManager()).setStyleResId(R.style.BetterPickersDialogFragment);
                tpb.show();
                tpb.addTimePickerDialogHandler(new TimePickerDialogFragment.TimePickerDialogHandler() {
                    @Override
                    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
                        thoraFestivoHasta.setText(TimeHelper.formatTime(hourOfDay, minute));
                    }
                });
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
                horario.setMaxMinutos(Integer.parseInt(tmaxMinutos.getText().toString()));

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
