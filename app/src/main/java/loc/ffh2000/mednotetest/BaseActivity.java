package loc.ffh2000.mednotetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import loc.ffh2000.mednotetest.presenters.IPresenter;

/**
 * Базовый класс Activity для всех моих Activity с общими функциями
 * @param <P> параметризуется классом презентера, который будет управлять данным Activity
 */
public class BaseActivity<P extends IPresenter> extends AppCompatActivity {
    /**
     * Ссылка на презентер
     */
    private P presenter;

    /**
     * Аксессор для получения презентера
     * @return
     */
    public P getPresenter() {
        return presenter;
    }

    /**
     * Мутатор для ссылки на презентер<br>
     * Рекомендуется использовать только при инициализации Activity.<br>
     * Со временем можно подумать о переносе в конструктор.<br>
     * @param presenter ссылка на презентер, который надо установить.<br>
     * @return возвращает ссылку для добавленного презентера. Надо для построения цепочек.
     */
    public P setPresenter(P presenter) {
        this.presenter = presenter;
        return this.presenter;
    }

    //сохранение состояния
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (getPresenter() != null) {
            getPresenter().onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getPresenter() != null)
            getPresenter().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getPresenter() != null)
            getPresenter().onPause();
    }
}
