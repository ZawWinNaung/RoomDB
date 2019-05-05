package zwn.com.example.roomdb;

import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String DB_NAME = "note_db_room";
    List<Note> noteList;
    NoteAdapter adapter;
    NoteDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView rvNote = findViewById(R.id.rvNote);
        setSupportActionBar(toolbar);
        db = Room.databaseBuilder(this,NoteDataBase.class,DB_NAME).build();

        rvNote.setLayoutManager(new LinearLayoutManager(this));
        rvNote.setHasFixedSize(true);
        rvNote.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        noteList = new ArrayList<>();
        adapter = new NoteAdapter(this,noteList, db);
        rvNote.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNote();
    }

    private void getNote(){
        db.daoAccess().getAllNote().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                noteList = notes;
                adapter.setNoteList(noteList);
            }
        });
    }
}
