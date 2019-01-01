package garye.utils.jhy.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JUtil {

    public static String dateToString(Date date) {
        String fullDate = "";

        Date from = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yy/MM/dd   HH:mm");

        fullDate = transFormat.format(from);

        return fullDate;
    }
}
