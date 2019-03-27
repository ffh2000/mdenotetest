package loc.ffh2000.mednotetest.presenters;

import android.app.Activity;

/**
 * Base presenter interface
 */
public interface IPresenter {
    /**
     * Метод для инициализации приложения.<br>
     * Должен вызываться в самом начале работы приложения.<br>
     */
    void init();

    /**
     * getter for Activity
     * @return
     */
    Activity getActivity();

    /**
     * setter for Activity
     * @return
     */
//    void setActivity(Activity activity);
}
