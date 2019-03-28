package loc.ffh2000.mednotetest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

public class CurrencyTableModel extends BaseModel {
    /**
     * Тег для сохранения данных в Parcel
     */
    public static String CURRENCY_TABLE = "currency_table";

    @SerializedName("stock")
    CurrencyModel[] stock;

    @SerializedName("as_of")
    String asOf;

    protected CurrencyTableModel(Parcel in) {
        stock = in.createTypedArray(CurrencyModel.CREATOR);
        asOf = in.readString();
    }

    public static final Creator<CurrencyTableModel> CREATOR = new Creator<CurrencyTableModel>() {
        @Override
        public CurrencyTableModel createFromParcel(Parcel in) {
            return new CurrencyTableModel(in);
        }

        @Override
        public CurrencyTableModel[] newArray(int size) {
            return new CurrencyTableModel[size];
        }
    };

    public CurrencyModel[] getStock() {
        return stock;
    }

    public void setStock(CurrencyModel[] stock) {
        this.stock = stock;
    }

    public String getAsOf() {
        return asOf;
    }

    public void setAsOf(String asOf) {
        this.asOf = asOf;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(stock, flags);
        dest.writeString(asOf);
    }
}
