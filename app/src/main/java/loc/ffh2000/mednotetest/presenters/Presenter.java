package loc.ffh2000.mednotetest.presenters;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import loc.ffh2000.mednotetest.BaseActivity;

/**
 * Base presenter class
 */
public abstract class Presenter<A extends BaseActivity> implements IPresenter<A> {
    private A activity;

    /**
     * Primary constructor
     * @param activity
     * @param savedInstanceState
     */
    public Presenter(A activity, Bundle savedInstanceState) {
        this.activity = activity;
        init(savedInstanceState);
    }

    public Presenter() {
        this(null, null);
    }

    public Presenter(A activity) {
        this(activity, null);
    }

    @Override
    public void init(Bundle savedInstanceState) {

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
