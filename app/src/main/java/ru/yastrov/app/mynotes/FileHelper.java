package ru.yastrov.app.mynotes;


public final class FileHelper {
    public static final String ENDLINE = "\n";

    public static String createFileName() {
        return DateHelper.getDateTimeString() + ".txt";
    }
}
