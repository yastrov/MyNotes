package ru.yastrov.app.mynotes;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public final class DateHelper {

    public static String getDateTimeString() {
        final Date now = new Date();
        return DateFormat.getDateInstance().format(now);
    }

    public static Date psrseStringToDate(String str) {
        DateFormat df = DateFormat.getDateInstance();
        try {
            return df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date now = new Date();
        return now;
    }
}
