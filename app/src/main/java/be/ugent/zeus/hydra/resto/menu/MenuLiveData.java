package be.ugent.zeus.hydra.resto.menu;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import java.util.List;

import be.ugent.zeus.hydra.R;
import be.ugent.zeus.hydra.common.arch.data.RequestLiveData;
import be.ugent.zeus.hydra.resto.RestoChoice;
import be.ugent.zeus.hydra.resto.RestoMenu;
import be.ugent.zeus.hydra.resto.RestoPreferenceFragment;

/**
 * @author Niko Strijbol
 */
class MenuLiveData extends RequestLiveData<List<RestoMenu>> implements SharedPreferences.OnSharedPreferenceChangeListener {

    private RestoChoice previousChoice;

    MenuLiveData(Context context) {
        super(context, new MenuRequest(context).map(new MenuFilter(context)));
    }

    @Override
    protected void onActive() {
        super.onActive();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String key = RestoPreferenceFragment.getRestoEndpoint(getContext(), preferences);
        String name = preferences.getString(RestoPreferenceFragment.PREF_RESTO_NAME, getContext().getString(R.string.resto_default_name));
        RestoChoice resto = new RestoChoice(name, key);
        // Register the listener for when the settings change while it's active
        preferences.registerOnSharedPreferenceChangeListener(this);
        // Check if the value is equal to the saved value. If not, we need to reload.
        if (previousChoice != null && !resto.equals(previousChoice)) {
            loadData();
        }
        previousChoice = resto;
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (RestoPreferenceFragment.PREF_RESTO_KEY.equals(key) || RestoPreferenceFragment.PREF_RESTO_NAME.equals(key)) {
            loadData();
        }
    }
}
