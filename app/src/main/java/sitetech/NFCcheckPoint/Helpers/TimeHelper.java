package sitetech.NFCcheckPoint.Helpers;

import java.sql.Time;
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
}
