package loc.ffh2000.mednotetest.presenters;

import android.app.Activity;

import loc.ffh2000.mednotetest.BaseActivity;

/**
 * Base presenter interface
 */
public interface IPresenter<A extends BaseActivity> {
    /**
     * Метод для инициализации приложения.<br>
     * Должен вызываться в самом начале работы приложения.<br>
     */
    void init();

    /**
     * getter for Activity
     * @return
     */
    A getActivity();

    /**
     * setter for Activity
     * @return
     */
//    void setActivity(Activity activity);
}
