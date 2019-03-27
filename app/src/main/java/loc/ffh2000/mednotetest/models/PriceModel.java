package loc.ffh2000.mednotetest.models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model for price field
 */
public class PriceModel extends BaseModel {

    @SerializedName("currency")
    private String currency;

    @SerializedName("amount")
    private float amount;

    protected PriceModel(Parcel in) {
        currency = in.readString();
        amount = in.readFloat();
    }

    public static final Creator<PriceModel> CREATOR = new Creator<PriceModel>() {
        @Override
        public PriceModel createFromParcel(Parcel in) {
            return new PriceModel(in);
        }

        @Override
        public PriceModel[] newArray(int size) {
            return new PriceModel[size];
        }
    };

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.currency);
        dest.writeFloat(this.amount);
    }
}
