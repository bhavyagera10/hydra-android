package be.ugent.zeus.hydra.common.database.migrations;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.migration.Migration;
import androidx.annotation.NonNull;
import android.util.Log;

/**
 * An older migration, kept for compatibility reasons.
 *
 * @author Niko Strijbol
 */
public class Migration_7_8 extends Migration {

    public Migration_7_8() {
        super(7, 8);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {

        Log.i("Migrations", "Migrating database from " + this.startVersion + " to " + this.endVersion);

        // Add the column
        supportSQLiteDatabase.execSQL("ALTER TABLE minerva_courses ADD COLUMN ordering INTEGER NOT NULL DEFAULT 0");
    }
}