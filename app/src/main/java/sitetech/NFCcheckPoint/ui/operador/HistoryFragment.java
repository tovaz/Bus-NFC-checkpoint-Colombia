package sitetech.NFCcheckPoint.ui.operador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import sitetech.routecheckapp.R;

public class HistoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        return inflater.inflate(R.layout.operador_history_fragment, viewGroup, false);
    }

}
