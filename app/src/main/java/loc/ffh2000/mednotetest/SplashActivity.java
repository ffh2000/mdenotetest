package loc.ffh2000.mednotetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import loc.ffh2000.mednotetest.presenters.IPresenter;
import loc.ffh2000.mednotetest.presenters.MainPresenter;

/**
 * Activity для стартового экрана.
 */
public class SplashActivity extends BaseActivity<MainPresenter> {
    private IPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new MainPresenter(this));
    }
}
