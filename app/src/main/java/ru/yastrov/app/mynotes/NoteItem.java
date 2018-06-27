package ru.yastrov.app.mynotes;

import java.util.Date;

public class NoteItem {
    private String mTitle,
                   mFileName;
    private Date mDate;

    public NoteItem () {
        mTitle = "";
        mFileName = "";
        mDate = null;
    }

    public NoteItem (String fileName, String title, Date date) {
        mTitle = title;
        mDate = date;
        mFileName = fileName;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public boolean isEmpty() {
        return mTitle.isEmpty();
    }
}
