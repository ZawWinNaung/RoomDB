package zwn.com.example.roomdb;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class AddNoteActivity extends AppCompatActivity {
    private Note note;
    EditText editTitle,editDescription;
    NoteDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        db = Room.databaseBuilder(this,NoteDataBase.class,MainActivity.DB_NAME).build();
    }

    private boolean isValidFormData(){
        boolean isValid = true;
        String title = editTitle.getText().toString();
        String des = editDescription.getText().toString();
        if (title.isEmpty() || des.isEmpty()){
            isValid = false;
        }
        note = new Note();
        note.setNoteTitle(title);
        note.setNoteDescription(des);
        return isValid;
    }

    boolean backPress = false;
    @Override
    public void onBackPressed() {
        if (backPress) {
            super.onBackPressed();
        } else if (isValidFormData()) {
            try {
                Boolean success = new InsertTask(db).execute(note).get();
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

    private static class InsertTask extends AsyncTask<Note, Void, Boolean> {
        private NoteDataBase db;

        InsertTask(NoteDataBase db) {
            this.db = db;
        }

        @Override
        protected Boolean doInBackground(Note... notes) {
            db.daoAccess().insertNote(notes[0]);
            return true;
        }
    }
}
