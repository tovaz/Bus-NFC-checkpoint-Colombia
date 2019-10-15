package sitetech.NFCcheckPoint.ui.operador;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Date;
import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.DialogHelper;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.myDialogInterface;
import sitetech.NFCcheckPoint.db.Registro_Turno;
import sitetech.NFCcheckPoint.db.Registro_TurnoDao;
import sitetech.NFCcheckPoint.db.Turno;
import sitetech.NFCcheckPoint.db.Usuario;
import sitetech.routecheckapp.R;

public class TurnoFragment extends Fragment {
    private View vista;
    private TextView tusuario;
    private TextView tbuses;
    private TextView tatiempo;
    private TextView tdemorados;
    private TextView tadelantados;
    private Button bcerrarSesion;
    private Button bcerrarTurno;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.operador_turno_fragment, viewGroup, false);

        obtenerDatos();
        cargarTurnoInfo();
        click();
        return vista;
    }

    private void click(){
        bcerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.showAsk2(v, "¿Cerrar Sesion?", "Desea realmente cerrar sesion.", "Cerrar Sesion", "Cancelar", new myDialogInterface() {
                    @Override
                    public View onBuildDialog() {
                        return null;
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onResult(View vista) {
                        Configuraciones.setUsuarioLog(getContext(), null);
                        getActivity().finish();

                    }

                });
            }
        });

        bcerrarTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.showAsk2(v, "¿Cerrar Turno?", "Al cerrar turno, se reiniciaran los registros a 0.", "Cerrar Turno", "Cancelar", new myDialogInterface() {
                    @Override
                    public View onBuildDialog() {
                        return null;
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onResult(View vista) {
                        cerrarTurno();
                    }

                });
            }
        });

    }


    private void cerrarTurno(){
        Turno turnoAbierto = Configuraciones.getTurnoAbierto();
        Usuario ulog = Configuraciones.getUsuarioLog(vista.getContext());

        turnoAbierto.setFechaCierre(new Date());
        turnoAbierto.setOperadorCierre(ulog.getId());

        crearNuevoTurno();
    }

    private void crearNuevoTurno(){
        Turno turno = new Turno();
        turno.setFechaCreacion(new Date());
        turno.setEliminada(false);
        turno.setFechaCierre(null);

        turno.setTotalAtiempo(0);
        turno.setTotalAdelantados(0);
        turno.setTotalDemorados(0);

        turno.setTotalBuses(new Long(0));
        AppController.daoSession.getTurnoDao().insert(turno);
        ToastHelper.exito("Turno cerrado.");
    }

    private void cargarControles(){
        tusuario = vista.findViewById(R.id.tusuario);
        tbuses = vista.findViewById(R.id.tbuses);
        tatiempo = vista.findViewById(R.id.tatiempo);
        tdemorados = vista.findViewById(R.id.tdemorados);
        tadelantados = vista.findViewById(R.id.tadelantados);

        bcerrarSesion = vista.findViewById(R.id.bcerrarSesion);
        bcerrarTurno = vista.findViewById(R.id.bcerrarTurno);
    }

    int totalBuses = 0;
    private void obtenerDatos(){
        cargarControles();
        Turno turnoAbierto = Configuraciones.getTurnoAbierto();

        List<Registro_Turno> rlista = AppController.daoSession.getRegistro_TurnoDao().queryBuilder()
                .where(Registro_TurnoDao.Properties.TurnoId.eq(turnoAbierto.getId())).list();

        totalBuses = rlista.size();
    }

    private void cargarTurnoInfo(){
        Turno turnoAbierto = Configuraciones.getTurnoAbierto();
        Usuario ulog = Configuraciones.getUsuarioLog(vista.getContext());
        tusuario.setText(ulog.getNombre());

        tbuses.setText(String.valueOf(totalBuses));
        tadelantados.setText(String.valueOf(turnoAbierto.getTotalAdelantados()));
        tdemorados.setText(String.valueOf(turnoAbierto.getTotalDemorados()));
        tatiempo.setText(String.valueOf(turnoAbierto.getTotalAtiempo()));
    }
}
