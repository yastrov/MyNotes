package ru.yastrov.app.mynotes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView notesRecycler;
    private List<NoteItem> notesList = new ArrayList<>();
    private NotesAdapter nAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
                intent.setAction(NoteActivity.NOTE_ACTION_CREATE);
                NoteListActivity.this.startActivity(intent);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.noteListProgressBar);

        notesRecycler = (RecyclerView) findViewById(R.id.notes);
        notesRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext()); /*Or LinearLayoutManager(this)*/
        notesRecycler.setLayoutManager(mLayoutManager);
        notesRecycler.setItemAnimator(new DefaultItemAnimator());
        new ReadNoteListTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean fileExists(String fname) {
        final File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    private class ReadNoteListTask extends AsyncTask<Void, Void, List<NoteItem>> {

        @Override
        protected List<NoteItem> doInBackground(Void... files) {
            return prepareNotes();
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(List<NoteItem> result) {
            notesList = result;
            if(result != null ) {
                nAdapter = new NotesAdapter(result, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View v, int position) {
                        final String fileName = notesList.get(position).getFileName();
                        Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
                        intent.putExtra(NoteActivity.INTENT_EXTRA_FILENAME, fileName);
                        intent.setAction(NoteActivity.NOTE_ACTION_OPEN);
                        startActivity(intent);
                    }
                });
                notesRecycler.setAdapter(nAdapter);
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.error_cant_load_file), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
            }
        }

        private List<NoteItem> prepareNotes() {
            List<NoteItem> notes = new ArrayList<>();
            File directory = getFilesDir();
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; ++i) {
                NoteItem note = openFile(files[i].getPath());
                notes.add(note);
            }
            //notes.sort(Comparator.comparing(NoteItem::getDate));
            notes.sort(new Comparator<NoteItem>() {
                @Override
                public int compare(NoteItem a, NoteItem b) {
                    return a.getDate().compareTo(b.getDate());
                }
            });
            return notes;
        }

        private NoteItem openFile(String fileName) {
            NoteItem result = new NoteItem();
            result.setFileName(fileName);
            if (fileExists(fileName)) {
                try {
                    InputStream in = openFileInput(fileName);
                    if ( in != null) {
                        InputStreamReader tmp = new InputStreamReader(in);
                        BufferedReader reader = new BufferedReader(tmp);
                        String str;
                        str = reader.readLine();
                        if(str != null) {
                            result.setTitle(str);
                        }
                        str = reader.readLine();
                        final Date date = DateHelper.parseStringToDate(str);
                        result.setDate(date);
                    }
                } catch (java.io.FileNotFoundException e) {} catch (Throwable t) {

                }
            }
            return result;
        }
    }
}
