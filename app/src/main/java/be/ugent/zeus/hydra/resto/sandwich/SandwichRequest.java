package be.ugent.zeus.hydra.resto.sandwich;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import be.ugent.zeus.hydra.common.network.Endpoints;
import be.ugent.zeus.hydra.common.network.JsonArrayRequest;
import be.ugent.zeus.hydra.common.request.Result;
import org.threeten.bp.Duration;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.Collections;
import java.util.List;

/**
 * CacheRequest the list of sandwiches.
 *
 * @author feliciaan
 */
class SandwichRequest extends JsonArrayRequest<Sandwich> {

    SandwichRequest(Context context) {
        super(context, Sandwich.class);
    }

    @NonNull
    @Override
    public Result<List<Sandwich>> execute(@NonNull Bundle args) {
        return super.execute(args).map(sandwiches -> {
            //noinspection Java8ListSort
            Collections.sort(sandwiches, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
            return sandwiches;
        });
    }

    @NonNull
    @Override
    protected String getAPIUrl() {
        return Endpoints.ZEUS_V2 + "resto/sandwiches.json";
    }

    @Override
    public Duration getCacheDuration() {
        return ChronoUnit.WEEKS.getDuration();
    }
}