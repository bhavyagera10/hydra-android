package be.ugent.zeus.hydra.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import be.ugent.zeus.hydra.feed.cards.dismissal.DismissalDaoTest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifierApi;
import nl.jqno.equalsverifier.Warning;
import okio.BufferedSource;
import okio.Okio;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZonedDateTime;

import static org.jeasy.random.FieldPredicates.named;

/**
 * General utilities and helper methods for use within the tests.
 *
 * @author Niko Strijbol
 */
public class Utils {

    public static <T> T generate(Class<T> clazz, String... exclude) {
        EasyRandomParameters params = new EasyRandomParameters()
                .scanClasspathForConcreteTypes(true)
                .randomize(ZonedDateTime.class, ZonedDateTime::now)
                .randomize(LocalDate.class, LocalDate::now)
                .randomize(OffsetDateTime.class, OffsetDateTime::now)
                .randomize(Instant.class, Instant::now);
        for (String excluded : exclude) {
            params.excludeField(named(excluded));
        }
        return new EasyRandom(params).nextObject(clazz);
    }

    public static <T> Stream<T> generate(Class<T> clazz, int amount, String... exclude) {
        EasyRandomParameters params = new EasyRandomParameters()
                .scanClasspathForConcreteTypes(true)
                .randomize(ZonedDateTime.class, ZonedDateTime::now)
                .randomize(LocalDate.class, LocalDate::now)
                .randomize(OffsetDateTime.class, OffsetDateTime::now)
                .randomize(Instant.class, Instant::now);
        for (String excluded : exclude) {
            params.excludeField(named(excluded));
        }
        return new EasyRandom(params).objects(clazz, amount);
    }

    /**
     * Default verifier with support for {@link ZonedDateTime}.
     *
     * @param clazz Class to verify.
     * @param <T>   Type of class.
     *
     * @return The verifier.
     */
    public static <T> EqualsVerifierApi<T> defaultVerifier(Class<T> clazz) {
        return EqualsVerifier.forClass(clazz)
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(ZonedDateTime.class, ZonedDateTime.now(), ZonedDateTime.now().minusDays(2));
    }

    public static <T> T readJson(Moshi moshi, String file, Type type) throws IOException {
        BufferedSource source = Okio.buffer(Okio.source(new FileInputStream(getResourceFile(file))));
        JsonAdapter<T> adapter = moshi.adapter(type);
        return adapter.fromJson(source);
    }

    public static File getResourceFile(String resourcePath) {
        return new File(DismissalDaoTest.class.getClassLoader().getResource(resourcePath).getFile());
    }

    private static final Random random = new Random();

    public static <T> T getRandom(List<T> collection) {
        return collection.get(random.nextInt(collection.size()));
    }

    public static <T> List<T> getRandom(final List<T> collection, int amount) {
        if (amount > collection.size()) {
            throw new IllegalArgumentException("The number of requested items cannot be larger than the number of total items.");
        }

        List<T> copy = new ArrayList<>(collection);
        Collections.shuffle(copy);
        return copy.subList(0, amount);
    }

    /**
     * Set a field to a value using reflection.
     *
     * Note: while useful when testing json classes for example, try to minimize usage.
     *
     * @param instance The instance to set the field on.
     * @param name The name of the field to set.
     * @param value The value to set the field to.
     */
    public static void setField(Object instance, String name, Object value) {
        try {

            Field declaredField = instance.getClass().getDeclaredField(name);
            boolean accessible = declaredField.isAccessible();
            declaredField.setAccessible(true);
            declaredField.set(instance, value);
            declaredField.setAccessible(accessible);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
