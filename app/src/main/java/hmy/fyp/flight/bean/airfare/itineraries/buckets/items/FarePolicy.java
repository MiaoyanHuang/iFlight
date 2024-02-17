package hmy.fyp.flight.bean.airfare.itineraries.buckets.items;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class FarePolicy {

    @SerializedName("isChangeAllowed")
    private String isChangeAllowed;

    @SerializedName("isPartiallyChangeable")
    private String isPartiallyChangeable;

    @SerializedName("isCancellationAllowed")
    private String isCancellationAllowed;

    @SerializedName("isPartiallyRefundable")
    private String isPartiallyRefundable;

    public String getIsChangeAllowed() {
        return isChangeAllowed;
    }

    public void setIsChangeAllowed(String isChangeAllowed) {
        this.isChangeAllowed = isChangeAllowed;
    }

    public String getIsPartiallyChangeable() {
        return isPartiallyChangeable;
    }

    public void setIsPartiallyChangeable(String isPartiallyChangeable) {
        this.isPartiallyChangeable = isPartiallyChangeable;
    }

    public String getIsCancellationAllowed() {
        return isCancellationAllowed;
    }

    public void setIsCancellationAllowed(String isCancellationAllowed) {
        this.isCancellationAllowed = isCancellationAllowed;
    }

    public String getIsPartiallyRefundable() {
        return isPartiallyRefundable;
    }

    public void setIsPartiallyRefundable(String isPartiallyRefundable) {
        this.isPartiallyRefundable = isPartiallyRefundable;
    }

    @NonNull
    @Override
    public String toString() {
        return "FarePolicy{" +
                "isChangeAllowed=" + isChangeAllowed +
                ", isPartiallyChangeable=" + isPartiallyChangeable +
                ", isCancellationAllowed=" + isCancellationAllowed +
                ", isPartiallyRefundable=" + isPartiallyRefundable +
                '}';
    }
}
