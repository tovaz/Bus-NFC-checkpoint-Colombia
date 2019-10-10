package sitetech.NFCcheckPoint.Helpers;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class stringHelper {

    public static void copiarClipboard(Context context, String texto){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text",  texto);
        clipboard.setPrimaryClip(clip);

    }
}
