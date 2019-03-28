package loc.ffh2000.mednotetest.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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

    /**
     * Частота обновления таймера
     */
    private static int TIMER_INTERVAL = 15000;

    /**
     * Таймер
     */
    private Timer timer;

//    private AtomicBoolean isInTimer = new AtomicBoolean(false);

    /**
     * Поле для хранения загруженной таблицы
     */
    private CurrencyTableModel currencyTable;

    /**
     * Primary constructor
     * @param activity
     * @param savedInstanceState
     */
    public MainPresenter(A activity, Bundle savedInstanceState) {
        super(activity, savedInstanceState);
    }

    public MainPresenter(A activity) {
        this(activity, null);
    }
    /**
     * Подкласс для обслуживания таймера
     */
    class updateCurrencyTableTask extends TimerTask {
        @Override
        public void run() {
            refreshCurrencyTable();
        }
    }

    /**
     * Экземпляр класса на базе интерфейса Api для получения данных
     */
    private Api netApi;

    /**
     * Стандартный класс для обмена http-запросами
     */
    private OkHttpClient okHttpClient;

    /**
     * Аксессор
     * @return
     */
    public Api getNetApi() {
        return netApi;
    }

    /**
     * Метод для инициализации презентера и вида.
     *
     * @param savedInstanceState сохраненное состояние. Имеет значение только при инициализации
     *                           MainActivity. Если задан, то делается попытка сначала из него
     *                           загрузить таблицу курсов (т.к. считаю, что идет режим
     *                           восстановления приложения, а не пуска). Если не получается, тогда
     *                           данные запрашиваются.
     */
    @Override
    public void init(Bundle savedInstanceState) {
        initNetwork();
        if (getActivity() == null)
            return;
        if (getActivity() instanceof SplashActivity) {
            //если стоим на splash, то запрашиваем данные и стартуем главный экран
            loadData((CurrencyTableModel data) -> {
                setCurrencyTable(data);

                try {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra(CurrencyTableModel.CURRENCY_TABLE, getCurrencyTable());
                    getActivity().startActivity(intent);
                    getActivity().finish();
                } catch (Exception e) {
                    Log.e(getClass().getCanonicalName(), "init: " + e.getMessage());
                }
            });
        } else if (getActivity() instanceof MainActivity) {
            if (savedInstanceState == null) {
                //если инициализация на главном экране, то грузим данные из Intent, предварительно перед закрытием splashActivity их туда уже поместили
                setCurrencyTable(getActivity().getIntent().getParcelableExtra(CurrencyTableModel.CURRENCY_TABLE));
                ((MainActivity) getActivity()).setCurrencyTable(getCurrencyTable());
            } else {
                //режим восстановления приложения (после убивания ОС)
                setCurrencyTable(savedInstanceState.getParcelable(CurrencyTableModel.CURRENCY_TABLE));
                ((MainActivity) getActivity()).setCurrencyTable(getCurrencyTable());
            }
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
                .subscribe(new ApiObserver(callback, CurrencyTableModel.class, "loadData observer"));
    }

    /**
     * Аксессор
     * @return
     */
    public CurrencyTableModel getCurrencyTable() {
        return currencyTable;
    }

    /**
     * Мутатор
     * @param currencyTable
     */
    private void setCurrencyTable(CurrencyTableModel currencyTable) {
        this.currencyTable = currencyTable;
    }

    /**
     * Метод для закрытия приложения
     */
    public void exitFromApp() {
        //обойдемся без чистки стека т.к. тут всего одно Activity
        getActivity().finish();
    }

    /**
     * Метод обновляет данные в таблице валют.
     * На сервер отправляется запрос и новая модель данных устанавливается в вид (Activity).
     */
    public void refreshCurrencyTable() {
        //сбрасываю таймер что бы запросы не валились бюстро, синхронизация не нужна т.к. все в главном потоке
        stopTimer();
        loadData((data) -> {
            setCurrencyTable(data);
            ((MainActivity)getActivity()).refreshLastUpdateText();
            ((MainActivity) getActivity()).setCurrencyTable(getCurrencyTable());
            startTimer();
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (getCurrencyTable() != null) {
            outState.putParcelable(CurrencyTableModel.CURRENCY_TABLE, getCurrencyTable());
        }
    }

    @Override
    public void onResume() {
        //state-машину применять не буду т.к. все слишком просто... а может сделать?
        startTimer();
    }

    @Override
    public void onPause() {
        //state-машину применять не буду т.к. все слишком просто
        stopTimer();
    }

    private void stopTimer() {
        if (getActivity() instanceof MainActivity) {
            timer.cancel();
        }
    }

    private void startTimer() {
        if (getActivity() instanceof MainActivity) {
            //запуск таймера для регулярного обновления
            timer = new Timer();
            timer.schedule(new updateCurrencyTableTask(), TIMER_INTERVAL, TIMER_INTERVAL);
        }
    }

}
