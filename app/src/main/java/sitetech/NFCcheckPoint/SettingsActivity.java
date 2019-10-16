package sitetech.NFCcheckPoint;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.preference.PreferenceFragmentCompat;


import java.io.File;
import java.io.IOException;
import java.util.Date;

import sitetech.NFCcheckPoint.Core.GetPathUtils;
import sitetech.NFCcheckPoint.Core.dbIExport;
import sitetech.NFCcheckPoint.Helpers.Configuraciones;
import sitetech.NFCcheckPoint.Helpers.TimeHelper;
import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.routecheckapp.R;

import static sitetech.NFCcheckPoint.Core.GetPathUtils.getFilePathFromUri;
import static sitetech.NFCcheckPoint.Core.dbIExport.getPath;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public boolean onPreferenceTreeClick(androidx.preference.Preference preference) {
            if (preference.getKey().equals("btn_backup")) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                getActivity().startActivityForResult(intent, 100);
            }


            if (preference.getKey().equals("btn_import")) {
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);
                getActivity().startActivityForResult(Intent.createChooser(intent, "Seleccione el archivo de base de datos."), 200);
            }

            return super.onPreferenceTreeClick(preference);
        }
    }

    public void seleccionarDirectorio() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //ToastHelper.info(String.valueOf(requestCode) + " - " + String.valueOf(resultCode));
        if (requestCode == 100) {                   // DIRECTORIO PARA EXPORTAR
            if (resultCode == -1) {
                File dataDir = Environment.getDataDirectory();
                Uri uri = data.getData();
                Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
                        DocumentsContract.getTreeDocumentId(uri));
                String destino = getPath(this, docUri);
                File destinoF = new File(destino);

                String dbpath = "/data/sitetech.routecheckapp/databases/" + Configuraciones.dbName;
                File dbFile = new File(dataDir, dbpath);


                if (destinoF != null && dbFile != null) {
                    try {
                        dbIExport.copy(dbFile, new File(destino + "/nfc_backup_" + TimeHelper.getDate(new Date(), "dd-MM-yyyy_HH-mm-ss")));
                        ToastHelper.exito("BACKUP REALIZADO CON EXITO: " + destino);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    ToastHelper.error("ERROR AL REALIZAR EL BACKUP");

            } else {
                // Nothing selected
            }
        }

        if (requestCode == 200) {                   // DIRECTORIO PARA EXPORTAR
            if (resultCode == -1) {
                File dataDir = Environment.getDataDirectory();
                Uri uri = data.getData();
                String origenS = GetPathUtils.getFilePathFromUri(this, uri);
                File origenF = new File(origenS);

                String destinoS = "/data/sitetech.routecheckapp/databases/" + Configuraciones.dbName;
                //File dbFile = new File(dataDir, dbpath);


                if (origenF != null) {
                    try {
                        dbIExport.copy(origenF, new File(dataDir, destinoS));
                        ToastHelper.exito("SE IMPORTO LA BASE DE DATOS CON EXITO. " + origenF.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    ToastHelper.error("ERROR AL IMPORTAR.");

            } else {
                // Nothing selected
            }
        }


    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}