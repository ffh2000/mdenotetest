package loc.ffh2000.mednotetest.presenters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import loc.ffh2000.mednotetest.MainActivity;
import loc.ffh2000.mednotetest.SplashActivity;
import loc.ffh2000.mednotetest.models.Api;
import loc.ffh2000.mednotetest.models.ApiObserver;
import loc.ffh2000.mednotetest.models.CurrencyModel;
import loc.ffh2000.mednotetest.models.CurrencyTableModel;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import loc.ffh2000.mednotetest.R;

public class MainPresenter extends Presenter {
    private static String EXCEPTION_ACTIVITY_NULL = "Activity property cannot be nullable";

    public MainPresenter() {
        this(null);
    }

    /**
     * Primary constructor
     *
     * @param activity
     */
    public MainPresenter(Activity activity) {
        super(activity);
    }

    /**
     * Экземпляр класса на базе интерфейса Api для получения данных
     */
    private Api netApi;

    /**
     * Стандартный класс для обмена http-запросами
     */
    private OkHttpClient okHttpClient;

    public Api getNetApi() {
        return netApi;
    }

    @Override
    public void init() {
        initNetwork();
        loadData((CurrencyTableModel data) -> {
            try {
                startMainActivity();
            } catch (Exception e) {
                Log.e(getClass().getCanonicalName(), "init: " + e.getMessage());
            }
        });
    }

    /**
     * Метод инициализирует экземпляр интерфейса Api, что бы в дальнейшем вести обмен по сети.
     */
    private void initNetwork() {
        okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new HttpLoggingInterceptor().setLevel((isDebug()) ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .readTimeout(Api.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .cache(null)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        this.netApi = retrofit.create(Api.class);
    }

    /**
     * Метод создает и запускает главный MainActivity.
     * Данный метод предназначен только для запуски из splashActivity для инициализации главного
     * экрана.
     * @throws Exception
     */
    private void startMainActivity() throws Exception {
        if (getActivity() == null)
            throw new Exception(EXCEPTION_ACTIVITY_NULL);
        if (!(getActivity() instanceof SplashActivity))
            throw new Exception(getActivity().getString(R.string.exception_init_only_from_splash));
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    /**
     * Метод загружает данные по сети и запускает callback
     */
    public void loadData(CallbackFunc<CurrencyTableModel> callback) {
        getNetApi().downloadData()
                .observeOn(AndroidSchedulers.mainThread()) //возвращаю обработку в главный поток т.к. из других потоков нельзя работать с интерфейсными элементами
                .subscribe(new ApiObserver(callback, CurrencyTableModel.class, "loadData"));
    }

}
