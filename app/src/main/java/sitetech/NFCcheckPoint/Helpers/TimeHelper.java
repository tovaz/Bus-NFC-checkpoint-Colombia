package sitetech.NFCcheckPoint.Helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelper {
    public static SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yyyy");
    public static SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");

    public static String getDate(Date d, String format){
        return new SimpleDateFormat(format).format(d);
    }

    public static String getDate(Date d){
        return TimeHelper.formatDate.format(d);
    }

    public static String getTime(Date d){
        return TimeHelper.formatTime.format(d);
    }

    public static String formatTime(int h, int m){
        if (h < 10)
            if (m>9)
                return "0" + h + ":" + m;
            else
                return "0" + h + ":0" + m;
        else if (m>9)
            return h + ":" + m;
        else
            return h + ":0" + m;

    }

    public static Long calcularDiferencia(String time1, String time2){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);
            long difference = date2.getTime() - date1.getTime();

            return difference / 1000;
        }catch (Exception e){
            return null;
        }
    }
}
