package sitetech.NFCcheckPoint.Helpers;

import android.view.View;

public interface myDialogInterface {
    // onBuildDialog() is called when the dialog builder is ready to accept a view to insert
    // into the dialog
    View onBuildDialog();
    // onCancel() is called when the user clicks on 'Cancel'
    void onCancel();
    // onResult(View v) is called when the user clicks on 'Ok'
    void onResult(View vista);
}
