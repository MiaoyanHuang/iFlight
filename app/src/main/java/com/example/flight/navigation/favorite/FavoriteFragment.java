package com.example.flight.navigation.favorite;

import static android.content.Context.MODE_PRIVATE;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flight.adapter.Adapter_FavoriteList;
import com.example.flight.dao.FlightDao;
import com.example.flight.databinding.FragmentFavoriteBinding;
import com.example.flight.entity.Flight;
import com.example.flight.Flight_Tracking;
import java.util.List;
/**
 * Created by: Huang Miaoyan
 * Create Date: 2022-12-19
 */
public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;
    private RecyclerView favorite_list;
    private ImageView favorite_empty_icon;
    private TextView favorite_empty_text;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        favorite_list = binding.favoriteList;
        favorite_empty_icon = binding.favoriteEmptyIcon;
        favorite_empty_text = binding.favoriteEmptyText;

        searchFavorite();

        return root;
    }

    /**
     * Function: Handler for Favorite Fragment
     */
    private final Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) { // Empty Favorite List
                favorite_list.setVisibility(View.GONE);
                favorite_empty_icon.setVisibility(View.VISIBLE);
                favorite_empty_text.setVisibility(View.VISIBLE);
            } else if (msg.what == 2) { // Display Favorite List
                List<Flight> flights = (List<Flight>) msg.obj;
                displayFavorite(flights);
            }
        }
    };

    /**
     * Function: Display Favorite Flight List
     */
    private void displayFavorite(List<Flight> flights) {

        Adapter_FavoriteList adapter_favoriteList = new Adapter_FavoriteList(getContext(), flights);
        favorite_list.setAdapter(adapter_favoriteList);

        //设置LinearLayoutManager的参数
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        favorite_list.setLayoutManager(linearLayoutManager);

        //监听FavoriteList中的点击事件
        adapter_favoriteList.setOnItemClickListener(position -> {
            Flight flight = flights.get(position);
            String FlightNumber = flight.getFlight_No().replace(" ",""); // Remove Space
            String Date = flight.getFlight_Date();

            //传递参数
            Intent intent = new Intent(getActivity(), Flight_Tracking.class);
            intent.putExtra("Favorite_SearchFavoriteFlight","Yes");
            intent.putExtra("Favorite_FlightNumber",FlightNumber);
            intent.putExtra("Favorite_Date", Date);
            startActivity(intent);
        });
    }

    /**
     * Function: Search Favorite Flight From MySQL Database
     */
    private void searchFavorite(){
        new Thread(){
            @Override
            public void run() {
                FlightDao flightDao = new FlightDao();
                Message message = new Message();
                SharedPreferences preferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE);
                int user_id = Integer.parseInt(preferences.getString("user_id",""));
                List<Flight> flights = flightDao.searchFavoriteFlight(user_id);
                if(flights.size() == 0){ // if size == 0, which mean no favorite flight in MySQL
                    message.what = 1;
                } else {  // if size != 0, which mean there are favorite flight information in MySQL
                    message.what = 2;
                    message.obj = flights;
                }
                handler.sendMessage(message);
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() { //当用户从其他页面返回时，刷新页面并重新搜索收藏航班
        super.onResume();
        searchFavorite();
    }
}