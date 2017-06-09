package be.ugent.zeus.hydra.repository.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import be.ugent.zeus.hydra.data.network.Request;
import be.ugent.zeus.hydra.repository.Result;

/**
 * Live data for a {@link Request}.
 *
 * @author Niko Strijbol
 */
public class ModelLiveData<M> extends LiveData<Result<M>> {

    private final Request<M> request;
    private final Context applicationContext;

    public ModelLiveData(Context context, Request<M> model) {
        this.applicationContext = context.getApplicationContext();
        this.request = model;
        init();
    }

    protected void init() {
        loadData(Bundle.EMPTY);
    }

    @Nullable
    @Override
    public Result<M> getValue() {
        return super.getValue();
    }

    /**
     * Load the actual data.
     *
     * @param bundle The arguments for the request.
     */
    protected void loadData(@Nullable Bundle bundle) {
        new AsyncTask<Void, Void, Result<M>>() {

            @Override
            protected Result<M> doInBackground(Void... voids) {
                return request.performRequest(bundle);
            }

            @Override
            protected void onPostExecute(Result<M> m) {
                super.onPostExecute(m);
                setValue(m);
            }
        }
        .execute();
    }

    protected Context getContext() {
        return applicationContext;
    }
}