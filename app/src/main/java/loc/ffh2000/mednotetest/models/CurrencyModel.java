package loc.ffh2000.mednotetest.models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Модель для записи данных о валюте
 */
public class CurrencyModel extends BaseModel {

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private PriceModel price;

    @SerializedName("percent_change")
    private float percentChange;

    @SerializedName("volume")
    private int volume;

    @SerializedName("symbol")
    private String symbol;

    protected CurrencyModel(Parcel in) {
        name = in.readString();
        price = in.readParcelable(PriceModel.class.getClassLoader());
        percentChange = in.readFloat();
        volume = in.readInt();
        symbol = in.readString();
    }

    public static final Creator<CurrencyModel> CREATOR = new Creator<CurrencyModel>() {
        @Override
        public CurrencyModel createFromParcel(Parcel in) {
            return new CurrencyModel(in);
        }

        @Override
        public CurrencyModel[] newArray(int size) {
            return new CurrencyModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PriceModel getPrice() {
        return price;
    }

    public void setPrice(PriceModel price) {
        this.price = price;
    }

    public float getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(float percentChange) {
        this.percentChange = percentChange;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(price, flags);
        dest.writeFloat(percentChange);
        dest.writeInt(volume);
        dest.writeString(symbol);
    }
}
