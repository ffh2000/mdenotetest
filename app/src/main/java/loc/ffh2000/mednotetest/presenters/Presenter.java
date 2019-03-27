package loc.ffh2000.mednotetest.presenters;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import loc.ffh2000.mednotetest.BaseActivity;

/**
 * Base presenter class
 */
public class Presenter<A extends BaseActivity> implements IPresenter<A> {
    private A activity;

    public Presenter() {
        this(null);
    }

    public Presenter(A activity) {
        this.activity = activity;
    }

    @Override
    public void init() {

    }

    @Override
    public A getActivity() {
        return activity;
    }

//    @Override
//    public void setActivity(Activity activity) {
//        this.activity = activity;
//    }

}
