package be.ugent.zeus.hydra.common.network;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.*;
import android.util.Log;

import be.ugent.zeus.hydra.common.arch.data.BaseLiveData;
import be.ugent.zeus.hydra.common.request.Request;
import be.ugent.zeus.hydra.common.request.Result;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.threeten.bp.Duration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * Common implementation base for requests that are network requests. This request provides built-in caching on the
 * networking level.
 *
 * <h2>Caching</h2>
 * The caching implementation uses OkHttp's cache implementation. How long a response is cached is determined by
 * {@link #getCacheDuration()}. By default, requests are not cached.
 *
 * Additionally, stale data is always used by default if needed.
 *
 * To disable the cache, pass {@link BaseLiveData#REFRESH_COLD} as an argument to the request.
 *
 * <h2>Decode</h2>
 * The request uses Moshi to decode the json response into Java objects.
 *
 * @author Niko Strijbol
 */
public abstract class JsonOkHttpRequest<D> implements Request<D> {

    private static final String TAG = "JsonOkHttpRequest";

    private static final String ALLOW_STALENESS = "be.ugent.zeus.hydra.data.staleness";

    private final Moshi moshi;
    private final OkHttpClient client;
    private final Type typeToken;

    /**
     * Construct a new request. As this constructor is not type-safe, it must only be used internally.
     *
     * @param context The context.
     * @param token The type token of the return type.
     */
    JsonOkHttpRequest(Context context, Type token) {
        this.moshi = InstanceProvider.getMoshi();
        this.client = InstanceProvider.getClient(context);
        this.typeToken = token;
    }

    /**
     * Construct a new request.
     *
     * @param context The context.
     * @param token   The class of the return type. If you need a generic list, use {@link JsonArrayRequest} instead.
     */
    public JsonOkHttpRequest(Context context, Class<D> token) {
        this(context, (Type) token);
    }

    @NonNull
    @Override
    @WorkerThread
    public Result<D> performRequest(@Nullable Bundle args) {

        JsonAdapter<D> adapter = moshi.adapter(typeToken);

        if (args == null) {
            args = Bundle.EMPTY;
        }

        try {
            return executeRequest(adapter, args);
        } catch (IOException e) {
            Log.d(TAG, "Error while getting data, try to get stale data.");
            // We try to get stale data at this point.
            args = new Bundle(args);
            args.putBoolean(ALLOW_STALENESS, true);

            Result<D> result = new Result.Builder<D>().withError(new IOFailureException(e)).build();

            try {
                Result<D> staleResult = executeRequest(adapter, args);
                Log.d(TAG, "Stale data was found and used.");
                // Add the result.
                return result.updateWith(staleResult);
            } catch (IOException e2) {
                Log.d(TAG, "Stale data was not found.");
                // Just give up at this point.
                return result;
            }
        }
    }

    private Result<D> executeRequest(JsonAdapter<D> adapter, @NonNull Bundle args) throws IOException {
        okhttp3.Request request = constructRequest(args);

        Response response = client.newCall(request).execute();

        Log.d(TAG, "executeRequest: response is from cache? " + String.valueOf(response.cacheResponse() != null));
        Log.d(TAG, "executeRequest: response is from network? " + String.valueOf(response.networkResponse() != null));

        assert response.body() != null;

        try {
            D result = adapter.fromJson(response.body().source());
            if (result == null) {
                throw new NullPointerException("Null is not a valid value.");
            }
            return new Result.Builder<D>()
                    .withData(result)
                    .build();
        } catch (JsonDataException | NullPointerException e) {
            // Create, log and throw exception, since this is not normal.
            String message = "The server did not respond with the expected format for URL: " + getAPIUrl();
            InvalidFormatException exception = new InvalidFormatException(message, e);
            FirebaseCrash.report(exception);
            return new Result.Builder<D>()
                    .withError(exception)
                    .build();
        }
    }

    @NonNull
    protected abstract String getAPIUrl();

    protected okhttp3.Request constructRequest(@NonNull Bundle arguments) {
        return new okhttp3.Request.Builder()
                .url(getAPIUrl())
                .cacheControl(constructCacheControl(arguments))
                .build();
    }

    protected CacheControl constructCacheControl(@NonNull Bundle arguments) {
        CacheControl.Builder cacheControl = new CacheControl.Builder();

        if (arguments.getBoolean(ALLOW_STALENESS, false)) {
            Log.d(TAG, "constructCacheControl: stale data is allowed!");
            cacheControl.maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS);
        }

        // If the REFRESH_COLD argument is set, ignore the cache duration and set it to zero, otherwise use the
        // duration provided by the request. By using this method, in contrast to noCache(), allows the stale data to
        // be used if the network failed for some reason.
        if (arguments.getBoolean(BaseLiveData.REFRESH_COLD, false)) {
            cacheControl.maxAge(0, TimeUnit.SECONDS);
        } else {
            Duration cacheDuration = getCacheDuration();
            cacheControl.maxAge((int) cacheDuration.getSeconds(), TimeUnit.SECONDS);
        }
        return cacheControl.build();
    }

    /**
     * How long the result of this request should be cached. By default, things are not cached.
     *
     * @return The duration, 0 by default.
     */
    protected Duration getCacheDuration() {
        return Duration.ZERO;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    Type getTypeToken() {
        return typeToken;
    }
}