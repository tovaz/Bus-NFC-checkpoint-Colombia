package sitetech.NFCcheckPoint.Helpers;

import com.google.gson.Gson;

public class nfcHelper {
    public static nfcData getnfcData(String _tag){
        Gson gson = new Gson();
        return gson.fromJson(_tag, nfcData.class);
    }
}
