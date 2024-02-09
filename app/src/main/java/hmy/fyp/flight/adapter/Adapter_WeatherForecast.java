package hmy.fyp.flight.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import hmy.fyp.flight.R;
import hmy.fyp.flight.bean.weather.Forecast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Adapter_WeatherForecast extends RecyclerView.Adapter<Adapter_WeatherForecast.WeatherViewHolder> {

    private final Context mContext;
    private final List<Forecast> mForecast;

    public Adapter_WeatherForecast(Context context, List<Forecast> forecast) { // constructor
        mContext = context;
        this.mForecast = forecast;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_weather_list, parent, false);
        return new WeatherViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Forecast forecast = mForecast.get(position);
        holder.rv_temperature.setText(forecast.getHigh() + "°C");
        holder.rv_weather.setText(forecast.getText());
        holder.rv_week.setText(getCorrectWeekName(forecast.getDay()));
        holder.rv_date.setText(transformTimeStamp(forecast.getDate()));
        String tem_low_high = forecast.getLow() + "°C" + "~" + forecast.getHigh() + "°C";
        holder.rv_tem_low_high.setText(tem_low_high);
        holder.rv_icon.setImageResource(getWeatherIcon(forecast.getCode()));
    }

    @Override
    public int getItemCount() { // return the number of data
        if (mForecast == null) {
            return 0;
        }
        return mForecast.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder { // ViewHolder
        TextView rv_temperature, rv_weather, rv_date, rv_week, rv_tem_low_high;
        ImageView rv_icon;
        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_temperature = itemView.findViewById(R.id.rv_temperature);
            rv_weather = itemView.findViewById(R.id.rv_weather);
            rv_date = itemView.findViewById(R.id.rv_date);
            rv_week = itemView.findViewById(R.id.rv_week);
            rv_tem_low_high = itemView.findViewById(R.id.rv_tem_low_high);
            rv_icon = itemView.findViewById(R.id.rv_icon);
        }
    }

    public String transformTimeStamp(String time){ // 将时间戳转换为日期
        long timestamp = Long.parseLong(time);

        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd", Locale.US);  // Create a SimpleDateFormat object with the desired date/time format
        Date date = new Date(timestamp * 1000);  // Convert the timestamp to a date
        return formatter.format(date);  // Print the formatted date string
    }

    private String getCorrectWeekName(String day) { // 将星期几转换为full name
        String week ;
        switch (day) {
            case "Mon":
                week = "Monday";
                break;
            case "Tue":
                week = "Tuesday";
                break;
            case "Wed":
                week = "Wednesday";
                break;
            case "Thu":
                week = "Thursday";
                break;
            case "Fri":
                week = "Friday";
                break;
            case "Sat":
                week = "Saturday";
                break;
            case "Sun":
                week = "Sunday";
                break;
            default:
                week = "Unknown";
                break;
        }
        return week;
    }

    private int getWeatherIcon(String weatherCode) { // 根据天气代码返回天气图标
        int result;
        switch (weatherCode) {
            case "0":
                result = android.R.drawable.ic_delete;
                break;
            case "1":
                result = android.R.drawable.ic_delete;
                break;
            case "2":
                result = android.R.drawable.ic_delete;
                break;
            case "3":
                result = android.R.drawable.ic_delete;
                break;
            case "4": //Thunderstorms
                result = R.drawable.weather_icon_thunderstorms;
                break;
            case "5":
                result = android.R.drawable.ic_delete;
                break;
            case "6":
                result = android.R.drawable.ic_delete;
                break;
            case "7":
                result = android.R.drawable.ic_delete;
                break;
            case "8":
                result = android.R.drawable.ic_delete;
                break;
            case "9":
                result = android.R.drawable.ic_delete;
                break;
            case "10":
                result = android.R.drawable.ic_delete;
                break;
            case "11": //Showers
                result = R.drawable.weather_icon_rainy;
                break;
            case "12": //Rainy
                result = R.drawable.weather_icon_rainy;
                break;
            case "13":
                result = android.R.drawable.ic_delete;
                break;
            case "14":
                result = android.R.drawable.ic_delete;
                break;
            case "15":
                result = android.R.drawable.ic_delete;
                break;
            case "16": //Snow
                result = R.drawable.weather_icon_snow;
                break;
            case "17":
                result = android.R.drawable.ic_delete;
                break;
            case "18": //Sleet
                result = android.R.drawable.ic_delete;
                break;
            case "19":
                result = android.R.drawable.ic_delete;
                break;
            case "20":
                result = android.R.drawable.ic_delete;
                break;
            case "21": //Haze
                result = android.R.drawable.ic_delete;
                break;
            case "22":
                result = android.R.drawable.ic_delete;
                break;
            case "23":
                result = android.R.drawable.ic_delete;
                break;
            case "24": //Windy
                result = R.drawable.weather_icon_windy;
                break;
            case "25":
                result = android.R.drawable.ic_delete;
                break;
            case "26": //Cloudy
                result = R.drawable.weather_icon_cloudy;
                break;
            case "27":
                result = android.R.drawable.ic_delete;
                break;
            case "28": //Mostly Cloudy
                result = R.drawable.weather_icon_cloudy;
                break;
            case "29":
                result = android.R.drawable.ic_delete;
                break;
            case "30": //Partly Cloudy
                result = R.drawable.weather_icon_cloudy;
                break;
            case "31": //Clear
                result = R.drawable.weather_icon_sunny;
                break;
            case "32": //Sunny
                result = R.drawable.weather_icon_sunny;
                break;
            case "33": //Mostly Clear
                result = R.drawable.weather_icon_mostly_clear;
                break;
            case "34": //Mostly Sunny
                result = R.drawable.weather_icon_sunny;
                break;
            case "36": //Hot
                result = R.drawable.weather_icon_sunny;
                break;
            case "45": //Scattered Showers
                result = R.drawable.weather_icon_scattered_showers;
                break;
            default:
                result = android.R.drawable.ic_menu_help;
                break;
        }
        return result;
    }
}
