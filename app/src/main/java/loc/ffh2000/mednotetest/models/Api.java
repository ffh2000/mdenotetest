package loc.ffh2000.mednotetest.models;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL = "http://phisix-api3.appspot.com/";
    String DATA_SCRIPT = "stocks.json";
    int DEFAULT_TIMEOUT = 60;

    @GET(BASE_URL + DATA_SCRIPT)
    Observable<CurrencyTableModel> downloadData();
}
