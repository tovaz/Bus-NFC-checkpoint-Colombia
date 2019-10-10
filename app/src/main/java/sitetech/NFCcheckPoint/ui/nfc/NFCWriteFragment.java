package sitetech.NFCcheckPoint.ui.nfc;

import android.app.DialogFragment;
import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;

import sitetech.NFCcheckPoint.Helpers.Dialog;
import sitetech.NFCcheckPoint.Helpers.Listener;
import sitetech.NFCcheckPoint.MainActivity;
import sitetech.routecheckapp.R;

public class NFCWriteFragment extends DialogFragment {

    public static final String TAG = NFCWriteFragment.class.getSimpleName();

    public static NFCWriteFragment newInstance() {

        return new NFCWriteFragment();
    }

    private TextView mTvMessage;
    private ProgressBar mProgress;
    private Listener mListener;

    private View vista;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.nfc_write_fragment,container,false);
        cargarControles();
        return vista;
    }

    private void cargarControles() {
        mTvMessage = (TextView) vista.findViewById(R.id.tv_message);
        mProgress = (ProgressBar) vista.findViewById(R.id.progress);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (MainActivity)context;
        mListener.onDialogDisplayed();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDialogDismissed();
    }

    public void onNfcDetected(Ndef ndef, String messageToWrite){
        mProgress.setVisibility(View.VISIBLE);
        writeToNfc(ndef,messageToWrite);
    }

    private void writeToNfc(Ndef ndef, String message){
        Gson g = new Gson();
        if (ndef != null)
            Dialog.showAlert(vista, "writeToNfc", g.toJson(ndef));
        else
            Dialog.showAlert(vista, "writeToNfc", "NULL");

        mTvMessage.setText("Escribiendo en la tarjeta nfc.");
        if (ndef != null) {
            try {
                Dialog.showAlert(vista, "writeToNfc - TRY -l83", g.toJson(ndef));
                ndef.connect();
                NdefRecord mimeRecord = NdefRecord.createMime("text/plain", message.getBytes(Charset.forName("US-ASCII")));
                ndef.writeNdefMessage(new NdefMessage(mimeRecord));
                Dialog.showAlert(vista, "writeToNfc - TRY -l87", g.toJson(ndef));
                ndef.close();
                //Write Successful
                mTvMessage.setText("Tarjeta escrita con exito.");

            } catch (IOException | FormatException e) {
                Dialog.showAlert(vista, "writeToNfc - TRY -l93", e.getMessage());
                e.printStackTrace();
                mTvMessage.setText("Error al escribir la tarjeta");

            } finally {
                mProgress.setVisibility(View.GONE);
            }

        }
    }
}