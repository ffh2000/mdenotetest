package loc.ffh2000.mednotetest.presenters;

import android.app.Activity;
import android.os.Bundle;

import loc.ffh2000.mednotetest.BaseActivity;

/**
 * Base presenter interface
 */
public interface IPresenter<A extends BaseActivity> {
    /**
     * Метод для инициализации приложения.<br>
     * Должен вызываться в самом начале работы приложения.<br>
     *
     * @param savedInstanceState сохраненное состояние. Если задан, то считаю, что надо
     *                           инициализиаровать презентер из режима восстановления состояния, а
     *                           не запуска с нуля. Тогда и данные беруться от сюда.
     */
    void init(Bundle savedInstanceState);

    /**
     * Метод для сохранения состояния
     *
     * @param outState
     */
    void onSaveInstanceState(Bundle outState);

    /**
     * getter for Activity
     *
     * @return
     */
    A getActivity();

    /**
     * см. Activity onPause()
     */
    void onPause();

    /**
     * см. Activity onResume()
     */
    void onResume();
}


