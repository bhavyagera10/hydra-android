package be.ugent.zeus.hydra.cache.file;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import be.ugent.zeus.hydra.cache.exceptions.CacheException;

import java.io.*;

/**
 * File cache that serializes the data.
 *
 * This class uses default serialization to save the objects. On Android, the default serializer is not fast. However,
 * for the current use in the application (save some 'smaller' data), it is sufficient. It is also executed in a
 * background thread, so worst case scenario, the user has to wait a little longer for the data (ns or ms). The
 * alternative would be to use an external serializer library (such as fst[1]). This makes the app take up a lot more
 * space, so we do not do that currently. If profiling suggests the serialisation here is really the bottleneck, which
 * is unlikely since it is about network requests, we can easily switch to fst.-
 *
 * @author Niko Strijbol
 * @see [1] <a href="https://github.com/RuedigerMoeller/fast-serialization">fst</a>
 */
public class SerializeCache extends FileCache {

    public SerializeCache(Context context) {
        super(context);
    }

    @Override
    protected <T extends Serializable> void write(String name, CacheObject<T> data) throws CacheException {
        ObjectOutputStream stream = null;
        try {
            stream = new ObjectOutputStream(new FileOutputStream(new File(directory, name)));
            stream.writeObject(data);
        } catch (IOException e) {
            Log.e(TAG, "Error while writing.", e);
            throw new CacheException(e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error while closing stream", e);
            }
        }
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Serializable> CacheObject<T> read(String name) throws CacheException {
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(new FileInputStream(new File(directory, name)));
            return (CacheObject<T>) stream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            Log.w(TAG, "Error while reading.", e);
            throw new CacheException(e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error while closing stream", e);
            }
        }
    }
}