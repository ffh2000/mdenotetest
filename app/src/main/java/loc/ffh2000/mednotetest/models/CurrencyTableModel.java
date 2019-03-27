package loc.ffh2000.mednotetest.models;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

public class CurrencyTableModel extends BaseModel {
    @SerializedName("stock")
    List<CurrencyModel> stock;

    @SerializedName("as_of")
    String asOf;

    public List<CurrencyModel> getStock() {
        return stock;
    }

    public void setStock(List<CurrencyModel> stock) {
        this.stock = stock;
    }

    public String getAsOf() {
        return asOf;
    }

    public void setAsOf(String asOf) {
        this.asOf = asOf;
    }
}
