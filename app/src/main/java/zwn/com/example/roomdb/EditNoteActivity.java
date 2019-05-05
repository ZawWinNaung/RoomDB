package zwn.com.example.roomdb;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class EditNoteActivity extends AppCompatActivity {
    EditText editTitle, editDesc;
    NoteDataBase db;
    private String title = "";
    private String des = "";
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        editTitle = findViewById(R.id.editTitle);
        editDesc = findViewById(R.id.editDescription);
        db = Room.databaseBuilder(this,NoteDataBase.class,MainActivity.DB_NAME).build();

        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("Note");
        editTitle.setText(note.getNoteTitle());
        editDesc.setText(note.getNoteDescription());
    }

    boolean backPress = false;
    @Override
    public void onBackPressed() {
        if (backPress) {
            super.onBackPressed();
        } else if (isValidFormData()) {
            try {
                Boolean success = new EditNoteActivity.EditTask(db).execute(note).get();
                if (success) {
                    finish();
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this,"Empty Note Discarded",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean isValidFormData(){
        boolean isValid = true;
        title = editTitle.getText().toString();
        des = editDesc.getText().toString();
        if (title.isEmpty() || des.isEmpty()) {
            isValid = false;
        }
        note.setNoteTitle(title);
        note.setNoteDescription(des);
        return isValid;
    }

    private static class EditTask extends AsyncTask<Note, Void, Boolean> {
        private NoteDataBase db;

        public EditTask(NoteDataBase db) {
            this.db = db;
        }

        @Override
        protected Boolean doInBackground(Note... notes) {
            db.daoAccess().updateNote(notes[0]);
            return true;
        }
    }
}
