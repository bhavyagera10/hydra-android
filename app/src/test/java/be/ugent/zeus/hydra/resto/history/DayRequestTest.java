package be.ugent.zeus.hydra.resto.history;

import be.ugent.zeus.hydra.common.network.AbstractJsonRequestTest;
import be.ugent.zeus.hydra.common.network.JsonOkHttpRequest;
import be.ugent.zeus.hydra.common.request.Request;
import be.ugent.zeus.hydra.resto.RestoChoice;
import be.ugent.zeus.hydra.resto.RestoMenu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.threeten.bp.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;

/**
 * @author Niko Strijbol
 */
@RunWith(RobolectricTestRunner.class)
public class DayRequestTest extends AbstractJsonRequestTest<RestoMenu> {

    @Override
    protected String getRelativePath() {
        return "resto/singleday.json";
    }

    @Override
    protected JsonOkHttpRequest<RestoMenu> getRequest() {
        DayRequest request = new DayRequest(context);
        request.setChoice(new RestoChoice("test", "test"));
        request.setDate(LocalDate.now());
        return request;
    }

    @Test
    public void testUrlFormat() {
        LocalDate now = LocalDate.now();
        DayRequest request = new DayRequest(context);
        request.setChoice(new RestoChoice("test", "test"));
        request.setDate(now);
        assertThat(request.getAPIUrl(), endsWith("test/" + now.getYear() + "/" + now.getMonthValue() + "/" + now.getDayOfMonth() + ".json"));
    }

    @Test(expected = IllegalStateException.class)
    public void testNonInitialised() {
        Request<RestoMenu> request = new DayRequest(context);
        request.execute();
    }
}