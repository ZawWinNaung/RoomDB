package zwn.com.example.roomdb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDataBase extends RoomDatabase {

    private static NoteDataBase INSTANCE;

    public abstract NoteDao daoAccess();

    public static NoteDataBase getNoteDataBase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), NoteDataBase.class, MainActivity.DB_NAME)
// allow queries on the main thread.
// Don’t do this on a real app! See PersistenceBasicSample for an example.
// .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
