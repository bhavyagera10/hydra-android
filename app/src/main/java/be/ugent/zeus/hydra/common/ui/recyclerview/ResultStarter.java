package be.ugent.zeus.hydra.common.ui.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * Enables other components to call {@link android.app.Activity#startActivityForResult(Intent, int)} or
 * {@link androidx.fragment.app.Fragment#startActivity(Intent, Bundle)}.
 *
 * @author Niko Strijbol
 */
@SuppressWarnings("unused")
public interface ResultStarter {

    Context getContext();

    void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options);

    void startActivityForResult(Intent intent, int requestCode);

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    int getRequestCode();
}
