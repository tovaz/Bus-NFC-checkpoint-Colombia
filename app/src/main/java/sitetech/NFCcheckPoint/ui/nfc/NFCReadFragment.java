package sitetech.NFCcheckPoint.ui.nfc;

import android.app.DialogFragment;
import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.IOException;

import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.Listener;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.NFCcheckPoint.MainActivity;
import sitetech.NFCcheckPoint.OperadorActivity;
import sitetech.routecheckapp.R;

public class NFCReadFragment extends DialogFragment {
    public static final String TAG = NFCReadFragment.class.getSimpleName();
    private View vista;

    public static NFCReadFragment newInstance() {

        return new NFCReadFragment();
    }

    private TextView mTvMessage;
    private Listener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.nfc_read_fragment,container,false);
        cargarControles(vista);
        return vista;
    }

    private void cargarControles(View _vista) {
        mTvMessage = (TextView) _vista.findViewById(R.id.tv_message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OperadorActivity) context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef){

        readFromNFC(ndef);
    }

    private void readFromNFC(Ndef ndef) {
        //((OperadorActivity)  getActivity()).updateCheckFragment("PRUEBA"); //ACTUALIZAR MENSAJE EN FRAGMENTO
        try {
            if (ndef != null) {
                ndef.connect();
                NdefMessage ndefMessage = ndef.getNdefMessage();
                String message = new String(ndefMessage.getRecords()[0].getPayload());

                ToastHelper.info("NDEF : " + message);
                Configuraciones.setUltimoTag(getActivity().getBaseContext(), message.toString());
                ((OperadorActivity)  getActivity()).updateCheckFragment(message); //ACTUALIZAR MENSAJE EN FRAGMENTO

                Log.d(TAG, "Read From NFC: " + message);
                mTvMessage.setText(message);


                ndef.close();
            }
        } catch (IOException | FormatException e) {
            e.printStackTrace();

        }
    }
}
