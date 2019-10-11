package sitetech.NFCcheckPoint.ui.operador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import sitetech.NFCcheckPoint.Helpers.Configuraciones;
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

        cargarControles();
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

    private void cargarTurnoInfo(){
        Turno turno = Configuraciones.getTurnoAbierto();
        Usuario ulog = Configuraciones.getUsuarioLog(vista.getContext());
        tusuario.setText(ulog.getNombre());

        if (turno.getTotalBuses() != null)
            tbuses.setText(turno.getTotalBuses().toString());
        //tatiempo.setText(turno.get);
    }
}
