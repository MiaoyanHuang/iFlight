package hmy.fyp.flight.bean.airfare;

import com.google.gson.annotations.SerializedName;

public class Context {

    @SerializedName("status")
    private String status;

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("totalResults")
    private String totalResults;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public String toString() {
        return "Context{" +
                "status='" + status + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", totalResults='" + totalResults + '\'' +
                '}';
    }
}
