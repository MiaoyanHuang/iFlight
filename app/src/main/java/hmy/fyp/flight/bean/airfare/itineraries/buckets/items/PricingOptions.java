package com.example.flight.bean.airfare.itineraries.buckets.items;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PricingOptions {

    @SerializedName("agentIds")
    private List<String> agentIds;

    @SerializedName("amount")
    private String amount;

    @SerializedName("bookingProposition")
    private String bookingProposition;

    public List<String> getAgentIds() {
        return agentIds;
    }

    public void setAgentIds(List<String> agentIds) {
        this.agentIds = agentIds;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBookingProposition() {
        return bookingProposition;
    }

    public void setBookingProposition(String bookingProposition) {
        this.bookingProposition = bookingProposition;
    }

    @Override
    public String toString() {
        return "PricingOptions{" +
                "agentIds=" + agentIds +
                ", amount='" + amount + '\'' +
                ", bookingProposition='" + bookingProposition + '\'' +
                '}';
    }
}
