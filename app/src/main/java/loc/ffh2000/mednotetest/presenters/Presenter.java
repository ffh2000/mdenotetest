package loc.ffh2000.mednotetest.presenters;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Base presenter class
 */
public class Presenter implements IPresenter {
    private Activity activity;

    public Presenter() {
        this(null);
    }

    public Presenter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void init() {

    }

    @Override
    public Activity getActivity() {
        return activity;
    }

//    @Override
//    public void setActivity(Activity activity) {
//        this.activity = activity;
//    }

}
