package loc.ffh2000.mednotetest;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import loc.ffh2000.mednotetest.models.CurrencyModel;
import loc.ffh2000.mednotetest.models.CurrencyTableModel;
import loc.ffh2000.mednotetest.presenters.MainPresenter;

public class MainActivity extends BaseActivity<MainPresenter> {
    private CurrencyTableModel currencyTable;
    private LinearLayout currencyLayout;
    private static String LAST_UPDATE_TAG = "last_update";

    /**
     * время последней сетевой загрузки. Поскольку момент обновления не имеет отношения к общей
     * логике приложения и нужен только текущему виду для показа пользователю, сделан тут.
     * Как вариант можно добавить в модель CurrencyTableModel и обновлять из презентера. Надо определиться
     * с логикой поведения, где и как должны работать данные.
     */
    private String lastUpdateText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currencyLayout = findViewById(R.id.content_main_currency_layout);
        Log.d("MainActivity", "MainActivityonCreate: " + ((savedInstanceState == null) ? "null" : savedInstanceState.toString()));
        if (savedInstanceState != null) {
            setLastUpdateText(savedInstanceState.getString(LAST_UPDATE_TAG));
        } else {
            refreshLastUpdateText();
        }
        setPresenter(new MainPresenter(this, savedInstanceState));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_exit:
                getPresenter().exitFromApp();
                break;
            case R.id.action_refresh_currency_table:
                getPresenter().refreshCurrencyTable();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Мутатор для установки данных.
     * @param currencyTable
     */
    public void setCurrencyTable(CurrencyTableModel currencyTable) {
        this.currencyTable = currencyTable;
        currencyLayout.removeAllViews();

        for (CurrencyModel currency : currencyTable.getStock()) {
            ViewStub viewStub = new ViewStub(this);// findViewById(R.id.);
            viewStub.setLayoutResource(R.layout.currency_row);
            currencyLayout.addView(viewStub);
            View view = viewStub.inflate();

            //last_update. Один на всех, мы за ценой не постоим
            TextView tvLastUpdate = view.findViewById(R.id.last_update);
            tvLastUpdate.setText(getLastUpdateText());

            //currency_name
            TextView tvCurrencyName = view.findViewById(R.id.currency_name);
            tvCurrencyName.setText(currency.getName());

            //currency_volume
            TextView tvCurrencyVolume = view.findViewById(R.id.currency_volume);
            tvCurrencyVolume.setText(String.format("%d", currency.getVolume()));

            //currency_price_amount
            TextView tvCurrencyAmount = view.findViewById(R.id.currency_price_amount);
            tvCurrencyAmount.setText(String.format("%8.2f", currency.getPrice().getAmount()));
        }
    }

    public String getLastUpdateText() {
        return lastUpdateText;
    }

    public void setLastUpdateText(String lastUpdateText) {
        this.lastUpdateText = lastUpdateText;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(LAST_UPDATE_TAG, getLastUpdateText());
        super.onSaveInstanceState(outState);
    }

    /**
     * Метод для записи последнего времени обновления данных по сети
     */
    public void refreshLastUpdateText() {
        Date currentDate = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        setLastUpdateText(timeFormat.format(currentDate));
    }

}
