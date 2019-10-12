package sitetech.NFCcheckPoint.ui.operador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Date;
import java.util.List;

import sitetech.NFCcheckPoint.AppController;
import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.operador_turno_fragment, viewGroup, false);

        obtenerDatos();
        cargarTurnoInfo();
        return vista;
    }

    private void cargarControles(){
        tusuario = vista.findViewById(R.id.tusuario);
        tbuses = vista.findViewById(R.id.tbuses);
        tatiempo = vista.findViewById(R.id.tatiempo);
        tdemorados = vista.findViewById(R.id.tdemorados);
        tadelantados = vista.findViewById(R.id.tadelantados);
    }

    int demorados, adelantados, atiempo, totalBuses = 0;
    private void obtenerDatos(){
        cargarControles();
        Turno turnoAbierto = Configuraciones.getTurnoAbierto();

        List<Registro_Turno> rlista = AppController.daoSession.getRegistro_TurnoDao().queryBuilder()
                .where(Registro_TurnoDao.Properties.TurnoId.eq(turnoAbierto.getId())).list();

        totalBuses = rlista.size();
        for (Registro_Turno rx : rlista){
            Date tadelantado = TimeHelper.separarString(rx.getMinAdelantado()) ;
            Date tatrazado = TimeHelper.separarString(rx.getMinAdelantado());
            Date tdespacho = TimeHelper.separarString(rx.getDespacho());

            if (tadelantado != null && tatrazado != null) {
                if (tadelantado.getTime() > tatrazado.getTime())
                    if (tadelantado.getTime() < 180)
                        adelantados++;
                    else
                        atiempo++;
                else if (tatrazado.getTime() > 180)
                    demorados++;
                else
                    atiempo++;
            }
        }

    }

    private void cargarTurnoInfo(){
        Usuario ulog = Configuraciones.getUsuarioLog(vista.getContext());
        tusuario.setText(ulog.getNombre());

        tbuses.setText(String.valueOf(totalBuses));
        tadelantados.setText(String.valueOf(adelantados));
        tdemorados.setText(String.valueOf(demorados));
        tatiempo.setText(String.valueOf(atiempo));
        //tatiempo.setText(turno.get);
    }
}
