package be.ugent.zeus.hydra.theme;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

import be.ugent.zeus.hydra.R;
import be.ugent.zeus.hydra.common.analytics.Analytics;
import be.ugent.zeus.hydra.common.analytics.Event;

/**
 * Show preferences related to the news-section.
 *
 * @author Niko Strijbol
 */
public class ThemePreferenceFragment extends PreferenceFragment {

    public static final String PREF_THEME_NIGHT_MODE = "pref_theme_night_mode";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_theme);
        ListPreference listPreference = (ListPreference) findPreference(PREF_THEME_NIGHT_MODE);
        listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            Analytics.getTracker(getActivity())
                    .log(new ThemeChanged((String) newValue));
            Toast.makeText(getActivity().getApplicationContext(), R.string.pref_theme_night_mode_restart, Toast.LENGTH_LONG).show();
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Analytics.getTracker(getActivity())
                .setCurrentScreen(getActivity(), "Settings > Theme", getClass().getSimpleName());
    }

    @AppCompatDelegate.NightMode
    public static int getNightMode(Context context) {
        String value = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_THEME_NIGHT_MODE, "3");
        switch (value) {
            case "0":
                return AppCompatDelegate.MODE_NIGHT_AUTO;
            case "1":
                return AppCompatDelegate.MODE_NIGHT_NO;
            case "2":
                return AppCompatDelegate.MODE_NIGHT_YES;
            default:
            case "3":
                return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
    }

    private static class ThemeChanged implements Event {
        private final String newValue;

        private ThemeChanged(String newValue) {
            this.newValue = newValue;
        }

        @Nullable
        @Override
        public Bundle getParams() {
            Bundle bundle = new Bundle();
            bundle.putString("theme", newValue);
            return bundle;
        }

        @Nullable
        @Override
        public String getEventName() {
            return "be.ugent.zeus.hydra.theme";
        }
    }
}