package hmy.fyp.flight.bean.flight;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Flight_Bean_Error {
    @SerializedName("error_code")
    private String error_code;
    @SerializedName("error")
    private String error;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @NonNull
    @Override
    public String toString() {
        return "Flight_Bean_Error{" +
                "error_code='" + error_code + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
