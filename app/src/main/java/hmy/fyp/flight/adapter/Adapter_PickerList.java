package hmy.fyp.flight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import hmy.fyp.flight.R;
import hmy.fyp.flight.entity.Airport;
import java.util.List;

public class Adapter_PickerList extends BaseAdapter {

    private final Context context;
    private final List<Airport> dataList;

    public Adapter_PickerList(Context context, List<Airport> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        if (dataList == null){
            return 0;
        }
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_view_picker_list, parent, false);
        }

        Airport airport = dataList.get(position);

        TextView IATA_Code = convertView.findViewById(R.id.picker_list_IATA_code);
        TextView Airport_Name = convertView.findViewById(R.id.picker_list_airport_name);
        TextView Country = convertView.findViewById(R.id.picker_list_country);

        IATA_Code.setText(airport.getAirportIATACode());
        Airport_Name.setText(airport.getAirportName());
        Country.setText(airport.getCountry());

        return convertView;
    }
}
