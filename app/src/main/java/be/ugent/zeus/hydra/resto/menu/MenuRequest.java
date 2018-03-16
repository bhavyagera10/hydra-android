package be.ugent.zeus.hydra.resto.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import be.ugent.zeus.hydra.common.network.Endpoints;
import be.ugent.zeus.hydra.common.network.JsonOkHttpRequest;
import be.ugent.zeus.hydra.resto.RestoMenu;
import be.ugent.zeus.hydra.resto.RestoPreferenceFragment;
import org.threeten.bp.Duration;

import java.util.List;

/**
 * Request for the menu's of the resto's.
 *
 * @author mivdnber
 */
public class MenuRequest extends JsonOkHttpRequest<List<RestoMenu>> {

    @VisibleForTesting
    public static final String OVERVIEW_URL = Endpoints.ZEUS_RESTO_URL + "menu/%s/overview.json";

    private final SharedPreferences preferences;

    public MenuRequest(Context context) {
        super(context,listToken(RestoMenu.class));
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    @NonNull
    @Override
    protected String getAPIUrl() {
        String resto = preferences.getString(RestoPreferenceFragment.PREF_RESTO_KEY, RestoPreferenceFragment.PREF_DEFAULT_RESTO);
        return String.format(OVERVIEW_URL, resto);
    }

    @Override
    public Duration getCacheDuration() {
        return Duration.ofSeconds(10);
    }
}