package be.ugent.zeus.hydra.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.ugent.zeus.hydra.R;
import be.ugent.zeus.hydra.fragments.common.RecyclerLoaderFragment;
import be.ugent.zeus.hydra.models.association.News;
import be.ugent.zeus.hydra.models.association.NewsItem;
import be.ugent.zeus.hydra.recyclerview.adapters.NewsAdapter;
import be.ugent.zeus.hydra.requests.NewsRequest;

/**
 * Created by Ellen on 07/04/2016.
 */
public class NewsFragment extends RecyclerLoaderFragment<NewsItem, News, NewsAdapter> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    /**
     * @return The adapter to use.
     */
    @Override
    protected NewsAdapter getAdapter() {
        return new NewsAdapter();
    }

    /**
     * @return The request that will be executed.
     */
    @Override
    protected NewsRequest getRequest() {
        return new NewsRequest();
    }
}
