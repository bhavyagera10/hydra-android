package be.ugent.zeus.hydra.resto.menu;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import be.ugent.zeus.hydra.common.network.InstanceProvider;
import be.ugent.zeus.hydra.resto.RestoMenu;
import be.ugent.zeus.hydra.resto.RestoPreferenceFragment;
import be.ugent.zeus.hydra.testing.Utils;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.threeten.bp.*;

import static be.ugent.zeus.hydra.testing.Assert.assertThat;
import static be.ugent.zeus.hydra.testing.Utils.generate;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

/**
 * @author Niko Strijbol
 */
@RunWith(RobolectricTestRunner.class)
public class MenuFilterTest {

    private final Instant cutOff = Instant.parse("2007-12-03T10:15:00.000Z");
    private final Clock clock = Clock.fixed(cutOff, ZoneOffset.UTC);

    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void testDefaults() throws IOException {
        Moshi moshi = InstanceProvider.getMoshi();
        List<RestoMenu> menus = Utils.readJson(moshi, "resto/menu_default.json", Types.newParameterizedType(List.class, RestoMenu.class));
        // Set half of the restos to before, half after.
        for (int i = 0; i < menus.size(); i++) {
            if (i < menus.size() / 2) {
                menus.get(i).setDate(LocalDateTime.ofInstant(cutOff, ZoneId.systemDefault()).toLocalDate().minusDays(menus.size() / 2 - i));
            } else {
                menus.get(i).setDate(LocalDateTime.ofInstant(cutOff, ZoneId.systemDefault()).toLocalDate().plusDays(i - menus.size() / 2));
            }
        }

        MenuFilter filter = new MenuFilter(context, clock);
        List<RestoMenu> result = filter.apply(menus);
        assertThat(result, hasSize(menus.size() / 2));
        assertTrue(result.stream().noneMatch(restoMenu -> restoMenu.getDate().isBefore(cutOff.atZone(ZoneId.systemDefault()).toLocalDate())));
    }

    @Test
    public void testSameDayBefore() {

        RestoMenu restoMenu = generate(RestoMenu.class);
        restoMenu.setDate(LocalDateTime.ofInstant(cutOff, ZoneId.systemDefault()).toLocalDate());

        // If the time in the settings is after the current time, the resto must be allowed to pass.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putString(RestoPreferenceFragment.PREF_RESTO_CLOSING_HOUR, "11:00")
                .apply();

        MenuFilter filter = new MenuFilter(context, clock);
        List<RestoMenu> result = filter.apply(Collections.singletonList(restoMenu));

        assertThat(result, contains(restoMenu));
    }

    @Test
    public void testSameDayAfter() {
        RestoMenu restoMenu = generate(RestoMenu.class);
        restoMenu.setDate(LocalDateTime.ofInstant(cutOff, ZoneId.systemDefault()).toLocalDate());

        // If the time in the settings is after the current time, the resto must be allowed to pass.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putString(RestoPreferenceFragment.PREF_RESTO_CLOSING_HOUR, "09:00")
                .apply();

        MenuFilter filter = new MenuFilter(context, clock);
        List<RestoMenu> result = filter.apply(Collections.singletonList(restoMenu));

        assertThat(result, empty());
    }

    @Test
    public void testSameDayOnMoment() {
        RestoMenu restoMenu = generate(RestoMenu.class);
        restoMenu.setDate(LocalDateTime.ofInstant(cutOff, ZoneId.systemDefault()).toLocalDate());

        // If the time in the settings is after the current time, the resto must be allowed to pass.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putString(RestoPreferenceFragment.PREF_RESTO_CLOSING_HOUR, "10:15")
                .apply();

        // TODO: for some weird reason, if we do not pass the same preference, it doesn't work, even if the debugger
        // says they are the same instance and all that. Perhaps this is a bug in Robolectric?
        MenuFilter filter = new MenuFilter(preferences, clock);
        List<RestoMenu> result = filter.apply(Collections.singletonList(restoMenu));

        assertThat(result, empty());
    }
}
