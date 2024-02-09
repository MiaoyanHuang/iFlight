package com.example.flight.navigation.home;

import static android.content.Context.MODE_PRIVATE;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.flight.Airfare;
import com.example.flight.databinding.FragmentHomeBinding;
import com.example.flight.Flight_Tracking;
import com.example.flight.Login;
/**
 * Created by: Huang Miaoyan
 * Create Date: 2022-12-19
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        Button airfare = binding.homeAirfareComparisonButton;
        Button flightTracking = binding.homeFlightTrackingButton;

        //Flight Tracking Button Listener
        airfare.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Airfare.class);
            startActivity(intent);
        });

        //Airfare Comparison Button Listener
        flightTracking.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Flight_Tracking.class);
            startActivity(intent);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Message")
                        .setMessage("Do You Want to LOG-OUT Your Account?")
                        .setIcon(android.R.drawable.ic_menu_help)
                        .setPositiveButton("Yes", (dialog, which) -> {
                            SharedPreferences preferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE);
                            preferences.edit().clear().apply();
                            Toast.makeText(getActivity(), "Log Out Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);
                            requireActivity().finish();
                        })
                        .setNegativeButton("No", (dialog, which) -> Toast.makeText(getActivity(), "User Cancelled Log-Out.", Toast.LENGTH_SHORT).show())
                        .show();
            }
        });

        return binding.getRoot();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}