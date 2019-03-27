package loc.ffh2000.mednotetest.models;

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
}
