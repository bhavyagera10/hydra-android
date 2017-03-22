package be.ugent.zeus.hydra.data.network;

import android.support.annotation.NonNull;

import be.ugent.zeus.hydra.data.gson.ModelFactory;
import be.ugent.zeus.hydra.data.network.exceptions.IOFailureException;
import be.ugent.zeus.hydra.data.network.exceptions.RequestFailureException;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Request that uses Spring and GSON to get json data from a remote location.
 *
 * @param <R> The type of the result of the request.
 *
 * @author feliciaan
 * @author Niko Strijbol
 */
public abstract class JsonSpringRequest<R> implements Request<R> {

    private Class<R> clazz;

    /**
     * @param clazz The class type of the result data.
     */
    public JsonSpringRequest(Class<R> clazz) {
        this.clazz = clazz;
    }

    /**
     * This implementation retrieves the data from the remote location using Spring and parses the result using GSON.
     *
     * @return The data.
     *
     * @throws IOFailureException If the data could not be obtained due to IO errors (i.e. network).
     * @throws RequestFailureException If something else went wrong.
     */
    @NonNull
    @Override
    public R performRequest() throws RequestFailureException {
        try {
            return createRestTemplate().getForEntity(getAPIUrl(), clazz).getBody();
        } catch (ResourceAccessException e) {
            throw new IOFailureException(e);
        } catch (RestClientException e) {
            throw new RequestFailureException(e);
        } catch (HttpMessageConversionException e) {
            // We log the wrapping exception in Firebase to be able to view the URL of the failing request.
            RequestFailureException wrapping = new RequestFailureException("Could not read JSON for " + getAPIUrl(), e);
            FirebaseCrash.report(wrapping);
            throw wrapping;
        }
    }

    @NonNull
    protected abstract String getAPIUrl();

    /**
     * @return The rest template used by Spring to perform the request.
     *
     * @throws be.ugent.zeus.hydra.data.network.exceptions.RestTemplateException If something went wrong while creating the rest template.
     */
    protected RestTemplate createRestTemplate() throws RequestFailureException {
        RestTemplate restTemplate = new RestTemplate();
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(ModelFactory.create())
                .create();
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter(gson);
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter(gson));
        return restTemplate;
    }
}