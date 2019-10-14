package sitetech.NFCcheckPoint.ui.historial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import sitetech.routecheckapp.R;

public class HistorySearchOP extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.history_search_operador, container, false);
        //final TextView textView = root.findViewById(R.id.text_share);

        return root;
    }
}
