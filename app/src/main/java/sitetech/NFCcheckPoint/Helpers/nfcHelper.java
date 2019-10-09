package sitetech.NFCcheckPoint.Helpers;

import com.google.gson.Gson;

public class nfcHelper {
    public static nfcData getnfcData(String _tag){
        Gson gson = new Gson();
        try {
            return gson.fromJson(_tag, nfcData.class);
        }
        catch (Exception e){
            ToastHelper.error("Error al intentar obtener la informacion de la tarjeta, TAG invalido.");
        }

        return null;
    }

    public static String convertnfcData(nfcData data){
        Gson gson = new Gson();
        try {
            return gson.toJson(data);
        }
        catch (Exception e){
            ToastHelper.error("Error al intentar parsear la informacion, objeto invalido.");
        }
        return null;
    }
}
