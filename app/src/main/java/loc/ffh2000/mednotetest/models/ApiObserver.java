package loc.ffh2000.mednotetest.models;

import android.graphics.ColorSpace;
import android.util.Log;

import com.fasterxml.jackson.databind.ser.Serializers;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import loc.ffh2000.mednotetest.presenters.CallbackFunc;

/**
 * Класс, унифицирующий обработку сетевых ответов. Это надо что бы сетевые ошибки обрабатывались
 * однообразно, не делать постоянно обработчики на каждый вызов.
 * В новой версии RxJava есть возможность использовать лямбды, но я хочу так.
 */
public class ApiObserver implements Observer<BaseModel> {
    /**
     * Список текущих выполняемых сетевых запросов.<br>
     * Здесь произвольного доступа не надо, поэтому спокойно можно ставить LinkedList. ...и памяти
     * на 2 коп. меньше будет использоваться.<br>
     * Сохранять список сетевых запросов надо что бы не оказалось каких-то "подвисших" запросов.
     * Если не удастся их дождаться, лучше оборвать, чем потом ловить всякие
     * {@link NullPointerException} т.к. к моменту получения ответа от сервера, экземпляр обработчик
     * может оказаться убитым.
     */
    private List<Disposable> disposables = new LinkedList<>();


    /**
     * callback-функция для обработки полученных данных
     */
    private CallbackFunc callback;

    /**
     * Класс, которым параметризована модель
     */
    private Class<? extends BaseModel> modelClass;

    /**
     * Имя метода из которого вызван обработчик. Чисто для текста ошибок.
     */
    private String methodName;

    /**
     * Конструктор
     * @param callback   функция обратного вызова, обрабатывающая сетевой ответ
     * @param modelClass класс, которым параметризован callback.
     *                   Нужен для коректного формирования экземпляра-заглушки
     *                   при сетевых ошибках
     * @param methodName имя метода из которого вызван сетевой обработчик. Нужен для
     *                   вывода имя метода в котором произошла сетевая ошибка
     */
    public ApiObserver(@NonNull CallbackFunc callback, Class<? extends BaseModel> modelClass, String methodName) {
        this.callback = callback;
        this.modelClass = modelClass;
        this.methodName = methodName;
    }

    @Override
    public void onSubscribe(Disposable d) {
        addDisposable(d);
    }

    @Override
    public void onNext(BaseModel model) {
        if (callback != null)
            callback.execute(model);
    }

    @Override
    public void onError(Throwable e) {
        Log.e("ApiObserver", "onError: " + e.getClass().getName() + "\n\t\tmessage: " + e.getMessage());
        e.printStackTrace();
        //яндекс-метрики нет, поэтому закомментировано
//        YandexMetrica.reportUnhandledException(new TnsMetrikaException("LOG ошибки API в исключении:\nClass:"+ modelClass +"\nCallback: " + callback + "\nMethod Name: " + methodName + "\n" , e));
    }

    @Override
    public void onComplete() {

    }

    /**
     * Метод добавляет в список сетевых запросов один сетевой запрос.
     *
     * @param disposable экземпляр исполнителя запроса, созданный при вызове subscribe()
     * @return в случае успеха true, иначе false
     */
    private boolean addDisposable(Disposable disposable) {
        synchronized (disposables) {
            try {
                disposables.add(disposable);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * Метод удаляет заданный сетевой запрос из списка выполняемых запросов.
     *
     * @param disposable сетевой запрос для удаления из списка
     * @return в случае если запрос был удален успешно возвращает true, иначе false
     */
    private boolean disposeNetQuery(Disposable disposable) {
        if (disposable == null)
            return false;
        synchronized (disposables) {
            try {
                disposables.remove(disposable);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * Метод удаляет все текущие сетевые запросы презентера.<br><br>
     * Для коректного удаления, накладывается блокировка
     * на список запросов (он же монитор). После этого каждый запрос останавливается.<br><br>
     * ЗЫЖ С одной стороны такая работа в асинхроне избыточна т.к.
     * все сетевые ответы я обрабатываю в главном потоке, но
     * с другой стороны <br>
     * 1. натянем презерватив на морковку,<br>
     * 2. в будущем можно будет безопасно вывести обработку из главного
     * потока приложения, особенно когда данные надо будет сохранять в таблицу, а не просто
     * отображать.
     */
    public void disposeAllNetQueries() {
        synchronized (disposables) {
            //на 6 Android стали приходить crashes при использовании итератора
//            for (Disposable disposable : disposables) {
//                try {
//                    disposable.dispose();
//                } finally {
//                    disposables.remove(disposable);
//                }

            for(int i = disposables.size() - 1; i >= 0; i--) {
                Disposable disposable = disposables.get(i);
                disposable.dispose();
                disposables.remove(i);
            }
        }
    }

}
