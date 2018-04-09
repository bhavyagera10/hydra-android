package be.ugent.zeus.hydra.common.database.migrations;

import android.app.Instrumentation;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.testing.LocalMigrationTestHelper;
import android.os.Build;

import be.ugent.zeus.hydra.common.database.Database;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Niko Strijbol
 */
@RunWith(RobolectricTestRunner.class)
public class TestMigration_12_13 {

    @Rule
    public LocalMigrationTestHelper testHelper;

    {
        Instrumentation mockInstrumentation = mock(Instrumentation.class);
        when(mockInstrumentation.getTargetContext()).thenReturn(RuntimeEnvironment.application);
        when(mockInstrumentation.getContext()).thenReturn(RuntimeEnvironment.application);
        testHelper = new LocalMigrationTestHelper(mockInstrumentation, Database.class.getCanonicalName());
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.KITKAT) // TODO: Sqlite version is too old in robolectric!
    public void testMigration() throws IOException {
        // There is no data to test, since we just add a new column.
        SupportSQLiteDatabase version12 = testHelper.createDatabase("test-db", 12);
        version12.close();
        testHelper.runMigrationsAndValidate("test-db", 13, true, new Migration_12_13());
    }

}