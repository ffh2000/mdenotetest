package loc.ffh2000.mednotetest;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import java.util.TimerTask;

import loc.ffh2000.mednotetest.models.CurrencyModel;
import loc.ffh2000.mednotetest.models.CurrencyTableModel;
import loc.ffh2000.mednotetest.presenters.MainPresenter;

public class MainActivity extends BaseActivity<MainPresenter> {
    private CurrencyTableModel currencyTable;
    private LinearLayout currencyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currencyLayout = findViewById(R.id.content_main_currency_layout);
        setPresenter(new MainPresenter(this));

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
        Date currentDate = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        for (CurrencyModel currency : currencyTable.getStock()) {
            ViewStub viewStub = new ViewStub(this);// findViewById(R.id.);
            viewStub.setLayoutResource(R.layout.currency_row);
            currencyLayout.addView(viewStub);
            View view = viewStub.inflate();

            TextView tvCurrencyName = view.findViewById(R.id.currency_name);
            tvCurrencyName.setText(currency.getName() + " | " + timeText);

            TextView tvCurrencyVolume = view.findViewById(R.id.currency_volume);
            tvCurrencyVolume.setText(String.format("%d", currency.getVolume()));

            TextView tvCurrencyAmount = view.findViewById(R.id.currency_price_amount);
            tvCurrencyAmount.setText(String.format("%8.2f", currency.getPrice().getAmount()));
        }
    }

}
