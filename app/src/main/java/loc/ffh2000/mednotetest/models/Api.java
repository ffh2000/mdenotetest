package loc.ffh2000.mednotetest.models;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface Api {
    String DATA_URL = "http://phisix-api3.appspot.com/stocks.json";

    @GET(DATA_URL)
    Observable<CurrencyTableModel> downloadData();
}
