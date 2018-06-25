package ru.yastrov.app.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;


public class NoteActivity extends AppCompatActivity {

    private EditText editTitle,
                     editContent;
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
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTitle = (EditText) findViewById(R.id.editNoteTitle);
        editContent = (EditText) findViewById(R.id.editNoteContent);

        final Intent intent = getIntent();
        if(intent.getAction() == NOTE_ACTION_OPEN) {
            fileName = intent.getStringExtra(INTENT_EXTRA_FILENAME);
        }
        if (intent.getAction() == NOTE_ACTION_CREATE) {
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

}
