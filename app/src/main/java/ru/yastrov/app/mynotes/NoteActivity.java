package ru.yastrov.app.mynotes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class NoteActivity extends AppCompatActivity {

    private EditText editTitle,
                     editContent;
    private ProgressBar progressBar;
    private String fileName;
    public static final String NOTE_ACTION_OPEN = "ru.yastrov.app.mynotes.action.note_open";
    public static final String NOTE_ACTION_CREATE = "ru.yastrov.app.mynotes.action.note_create";
    public static final String INTENT_EXTRA_FILENAME = "fname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                new SaveFileTask().execute(fileName);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTitle = (EditText) findViewById(R.id.editNoteTitle);
        editContent = (EditText) findViewById(R.id.editNoteContent);
        progressBar = (ProgressBar) findViewById(R.id.noteProgressBar);

        final Intent intent = getIntent();
        if(intent.getAction() == NOTE_ACTION_OPEN) {
            fileName = intent.getStringExtra(INTENT_EXTRA_FILENAME);
            new OpenFileTask().execute(fileName);
        }
        if (intent.getAction() == NOTE_ACTION_CREATE) {
            fileName = FileHelper.createFileName();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString(INTENT_EXTRA_FILENAME, fileName);
        outState.putString("title", editTitle.getText().toString());
        outState.putString("content", editContent.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        fileName = savedInstanceState.getString(INTENT_EXTRA_FILENAME);
        String str = savedInstanceState.getString("title");
        editTitle.setText(str);
        str = savedInstanceState.getString("content");
        editContent.setText(str);
    }

    private void showTaskWorked() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        editTitle.setEnabled(false);
        editContent.setEnabled(false);
    }

    private void showTaskStoped() {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        editTitle.setEnabled(true);
        editContent.setEnabled(true);
    }

    /* Work with files */
    public static final String ENDLINE = "\n";

    public boolean fileExists(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    private class OpenFileTask extends AsyncTask<String, Void, NoteData> {

        @Override
        protected NoteData doInBackground(String... files) {
            return openFile(files[0]);
        }

        @Override
        protected void onPreExecute() {
            showTaskWorked();
        }

        @Override
        protected void onPostExecute(NoteData result) {
            showTaskStoped();
            if(result != null && !result.isEmpty()) {
                editTitle.setText(result.title);
                editContent.setText(result.content);
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.error_cant_load_file), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }

        }

        private NoteData openFile(String fileName) {
            NoteData result = new NoteData();
            if (fileExists(fileName)) {
                try {
                    InputStream in = openFileInput(fileName);
                    if ( in != null) {
                        InputStreamReader tmp = new InputStreamReader(in);
                        BufferedReader reader = new BufferedReader(tmp);
                        String str;
                        /* Read title */
                        str = reader.readLine();
                        if(str != null) {
                            result.title = str;
                        }
                        /* Read date */
                        // DateFormat df = DateFormat.getDateInstance();
                        // df.parse(str);
                        str = reader.readLine();
                        str = "";

                        StringBuilder buf = new StringBuilder();
                        while ((str = reader.readLine()) != null) {
                            buf.append(str);
                            buf.append(str + ENDLINE);
                        } in.close();
                        result.content = buf.toString();
                    }
                } catch (java.io.FileNotFoundException e) {} catch (Throwable t) {

                }
            }
            return result;
        }
    }

    private class SaveFileTask extends AsyncTask<String, Void, Boolean> {
        private String title, content;

        @Override
        protected Boolean doInBackground(String... files) {
            return saveFile(files[0]);
        }

        @Override
        protected void onPreExecute() {
            showTaskWorked();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showTaskStoped();
            if(result == Boolean.TRUE) {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.note_saved), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.error_cant_load_file), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }

        }
        private Boolean saveFile(String fileName) {
            try {
                OutputStreamWriter out =
                        new OutputStreamWriter(openFileOutput(fileName, 0));
                out.write(this.title);
                out.write(ENDLINE);
                out.write(DateHelper.getDateTimeString());
                out.write(ENDLINE);
                out.write(this.content);
                out.write(ENDLINE);
                out.close();
                return Boolean.TRUE;
            } catch (Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), "Exception: " + t.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            return Boolean.FALSE;
        }

    }

    private class NoteData {
        public String title, content;

        public NoteData () {
            title = "";
            content = "";
        }

        public NoteData(String mTitle, String mContent) {
            title = mTitle;
            content = mContent;
        }

        public boolean isEmpty() {
            return title.isEmpty() || content.isEmpty();
        }
    }

}
