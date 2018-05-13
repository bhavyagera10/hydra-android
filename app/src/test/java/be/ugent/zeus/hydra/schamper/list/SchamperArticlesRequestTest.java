package be.ugent.zeus.hydra.schamper.list;

import be.ugent.zeus.hydra.common.network.AbstractJsonRequestTest;
import be.ugent.zeus.hydra.common.network.JsonOkHttpRequest;
import be.ugent.zeus.hydra.schamper.Article;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

/**
 * @author Niko Strijbol
 */
@RunWith(RobolectricTestRunner.class)
public class SchamperArticlesRequestTest extends AbstractJsonRequestTest<List<Article>> {

    @Override
    protected String getRelativePath() {
        return "daily_android.json";
    }

    @Override
    protected JsonOkHttpRequest<List<Article>> getRequest() {
        return new SchamperArticlesRequest(RuntimeEnvironment.application);
    }
}