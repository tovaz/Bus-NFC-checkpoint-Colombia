package sitetech.NFCcheckPoint.ui.historial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import sitetech.routecheckapp.R;

public class HistorialFragment extends Fragment {;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.historial_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_share);

        textView.setText("HISTORIAL DE REGISTROS");
        return root;
    }
}