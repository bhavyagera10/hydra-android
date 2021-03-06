package be.ugent.zeus.hydra.feed;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import be.ugent.zeus.hydra.common.arch.data.BaseLiveData;
import be.ugent.zeus.hydra.common.arch.data.Event;
import be.ugent.zeus.hydra.common.request.Result;
import be.ugent.zeus.hydra.common.ui.RefreshViewModel;
import be.ugent.zeus.hydra.feed.cards.Card;
import be.ugent.zeus.hydra.feed.commands.CommandResult;
import be.ugent.zeus.hydra.feed.commands.FeedCommand;

/**
 * @author Niko Strijbol
 */
public class FeedViewModel extends RefreshViewModel<List<Card>> {

    private MutableLiveData<Event<CommandResult>> commandLiveData;

    public FeedViewModel(Application application) {
        super(application);
    }

    @Override
    protected BaseLiveData<Result<List<Card>>> constructDataInstance() {
        return new FeedLiveData(getApplication());
    }

    LiveData<Event<CommandResult>> getCommandLiveData() {
        if (commandLiveData == null) {
            commandLiveData = new MutableLiveData<>();
        }
        return commandLiveData;
    }

    void execute(FeedCommand command) {
        AsyncTask.execute(() -> {
            int result = command.execute(getApplication());
            commandLiveData.postValue(new Event<>(CommandResult.forExecute(command, result)));
        });
    }

    void undo(FeedCommand command) {
        AsyncTask.execute(() -> {
            int result = command.undo(getApplication());
            commandLiveData.postValue(new Event<>(CommandResult.forUndo(result)));
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        commandLiveData = null;
    }
}
