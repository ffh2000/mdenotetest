package loc.ffh2000.mednotetest.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import loc.ffh2000.mednotetest.BaseActivity;
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

public class MainPresenter<A extends BaseActivity> extends Presenter<A> {
    private static String EXCEPTION_ACTIVITY_NULL = "Activity property cannot be nullable";
    private static String CURRENCY_TABLE = "currency_table";

    private CurrencyTableModel currencyTable;

    /**
     * Primary constructor
     *
     * @param activity
     */
    public MainPresenter(A activity) {
        super(activity);
        init();
    }

    public MainPresenter() {
        this(null);
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
        if (getActivity() == null)
            return;
        if (getActivity() instanceof SplashActivity) {
            //если стоим на splash, то запрашиваем данные и стартуем главный экран
            loadData((CurrencyTableModel data) -> {
                currencyTable = data;
                try {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra(CURRENCY_TABLE, currencyTable);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                } catch (Exception e) {
                    Log.e(getClass().getCanonicalName(), "init: " + e.getMessage());
                }
            });
        } else if (getActivity() instanceof MainActivity) {
            //если инициализация на главном экране, то грузим данные из Intent, предварительно перед закрытием splashActivity их туда уже поместили
            currencyTable = getActivity().getIntent().getParcelableExtra(CURRENCY_TABLE);
            ((MainActivity) getActivity()).setCurrencyTable(currencyTable);
        } else {
//            throw new Exception(getActivity().getString(R.string.exception_unsupported_activity));
            Toast.makeText(getActivity(), getActivity().getString(R.string.exception_unsupported_activity), Toast.LENGTH_SHORT).show();
        }
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
     * Метод загружает данные по сети и запускает callback
     */
    private void loadData(CallbackFunc<CurrencyTableModel> callback) {
        getNetApi().downloadData()
                .observeOn(AndroidSchedulers.mainThread()) //возвращаю обработку в главный поток т.к. из других потоков нельзя работать с интерфейсными элементами
                .subscribe(new ApiObserver(callback, CurrencyTableModel.class, "loadData"));
    }

    public CurrencyTableModel getCurrencyTable() {
        return currencyTable;
    }
}
