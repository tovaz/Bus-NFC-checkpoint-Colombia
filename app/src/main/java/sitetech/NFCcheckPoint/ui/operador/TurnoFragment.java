package sitetech.NFCcheckPoint.ui.operador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import sitetech.routecheckapp.R;

public class TurnoFragment extends Fragment {
    private View vista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.operador_turno_fragment, viewGroup, false);

        return vista;
    }
}
