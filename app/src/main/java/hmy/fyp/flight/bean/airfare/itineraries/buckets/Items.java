package hmy.fyp.flight.bean.airfare.itineraries.buckets;

import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.FarePolicy;
import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.Legs;
import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.Price;
import hmy.fyp.flight.bean.airfare.itineraries.buckets.items.PricingOptions;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Items {

    @SerializedName("id")
    private String id;

    @SerializedName("price")
    private Price price;

    @SerializedName("legs")
    private List<Legs> legs;

    @SerializedName("isSelfTransfer")
    private boolean isSelfTransfer;

    @SerializedName("isProtectedSelfTransfer")
    private boolean isProtectedSelfTransfer;

    @SerializedName("farePolicy")
    private FarePolicy farePolicy;

    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("isMashUp")
    private boolean isMashUp;

    @SerializedName("hasFlexibleOptions")
    private boolean hasFlexibleOptions;

    @SerializedName("score")
    private String score;

    @SerializedName("pricingOptions")
    private List<PricingOptions> pricingOptions;

    @SerializedName("deeplink")
    private String deeplink;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public List<Legs> getLegs() {
        return legs;
    }

    public void setLegs(List<Legs> legs) {
        this.legs = legs;
    }

    public boolean isSelfTransfer() {
        return isSelfTransfer;
    }

    public void setSelfTransfer(boolean selfTransfer) {
        isSelfTransfer = selfTransfer;
    }

    public boolean isProtectedSelfTransfer() {
        return isProtectedSelfTransfer;
    }

    public void setProtectedSelfTransfer(boolean protectedSelfTransfer) {
        isProtectedSelfTransfer = protectedSelfTransfer;
    }

    public FarePolicy getFarePolicy() {
        return farePolicy;
    }

    public void setFarePolicy(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isMashUp() {
        return isMashUp;
    }

    public void setMashUp(boolean mashUp) {
        isMashUp = mashUp;
    }

    public boolean isHasFlexibleOptions() {
        return hasFlexibleOptions;
    }

    public void setHasFlexibleOptions(boolean hasFlexibleOptions) {
        this.hasFlexibleOptions = hasFlexibleOptions;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<PricingOptions> getPricingOptions() {
        return pricingOptions;
    }

    public void setPricingOptions(List<PricingOptions> pricingOptions) {
        this.pricingOptions = pricingOptions;
    }

    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }

    @Override
    public String toString() {
        return "Items{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", legs=" + legs +
                ", isSelfTransfer=" + isSelfTransfer +
                ", isProtectedSelfTransfer=" + isProtectedSelfTransfer +
                ", farePolicy=" + farePolicy +
                ", tags=" + tags +
                ", isMashUp=" + isMashUp +
                ", hasFlexibleOptions=" + hasFlexibleOptions +
                ", score='" + score + '\'' +
                ", pricingOptions=" + pricingOptions +
                ", deeplink='" + deeplink + '\'' +
                '}';
    }
}
