package zwn.com.example.roomdb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private Context context;
    private List<Note> noteList;
    private static final String DB_NAME = "room_note_db";
    private NoteDataBase db;

    public NoteAdapter(Context context, List<Note> noteList, NoteDataBase db) {
        this.context = context;
        this.noteList = noteList;
        this.db = db;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item,viewGroup,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, final int i) {
        final Note note = noteList.get(i);
        noteViewHolder.txtTitle.setText(note.getNoteTitle());
        noteViewHolder.txtDesc.setText(note.getNoteDescription());

        noteViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String[] items = {"Edit" ,"Delete"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();
                                Intent intent = new Intent(context, EditNoteActivity.class);
                                intent.putExtra("Note", note);
                                context.startActivity(intent);
                                break;
                            case 1:
                                dialog.dismiss();
                                new DeleteTask(db).execute(note);
                                noteList.remove(i);
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    void setNoteList(List<Note> noteList){
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle,txtDesc;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDesc = itemView.findViewById(R.id.txtDesc);
        }
    }

    private static class DeleteTask extends AsyncTask<Note, Void, Void> {

        private NoteDataBase db;

        public DeleteTask(NoteDataBase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            db.daoAccess().deleteNote(notes[0]);
            return null;
        }
    }
}
