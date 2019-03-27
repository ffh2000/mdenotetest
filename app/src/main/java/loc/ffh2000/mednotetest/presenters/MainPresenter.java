package loc.ffh2000.mednotetest.presenters;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import loc.ffh2000.mednotetest.models.Api;
import loc.ffh2000.mednotetest.models.CurrencyTableModel;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainPresenter extends Presenter {
    /**
     * Экземпляр класса на базе интерфейса Api для получения данных
     */
    private Api netApi;

    /**
     * Стандартный класс для обмена http-запросами
     */
    private OkHttpClient okHttpClient;

    public MainPresenter() {
        super();
        this.initNetwork();
    }

    public Api getNetApi() {
        return netApi;
    }

    /**
     * Метод инициализирует экземпляр интерфейса Api, что бы в дальнейшем вести обмен по сети.
     */
    private void initNetwork() {
        String URL = "";
        okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new HttpLoggingInterceptor().setLevel((isDebug()) ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .readTimeout(60, TimeUnit.SECONDS)
                .cache(null)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        this.netApi = retrofit.create(Api.class);
    }

    /**
     * Метод загружает данные по сети и запускает callback
     */
    public void loadData(CallbackFunc<CurrencyTableModel> callback) {
//        getNetApi().downloadData()
//                .observeOn(AndroidSchedulers.mainThread()) //возвращаю обработку в главный поток т.к. из других потоков нельзя работать с интерфейсными элементами
//                .subscribe(new ApiObserver(callback, LoginServerData.class, "login"));
    }
}
