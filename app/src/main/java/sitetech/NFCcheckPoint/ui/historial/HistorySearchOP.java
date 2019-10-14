package sitetech.NFCcheckPoint.ui.historial;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.Helpers.activityHelper;
import sitetech.NFCcheckPoint.OperadorActivity;
import sitetech.routecheckapp.R;

public class HistorySearchOP extends Fragment {
    private ImageView batraz;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.history_search_operador, container, false);
        //root = inflater.inflate(R.layout.activity_operador, container, false);


        batraz = root.findViewById(R.id.batraz);

        Click();

        return root;
    }

    private void Click(){
        batraz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHelper.goBackStack(v);
                ToastHelper.normal("ATRAS PRECIONADO");
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        ((OperadorActivity)getActivity()).cerrarHistorial();
    }


}
