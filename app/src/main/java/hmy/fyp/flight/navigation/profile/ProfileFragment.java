package hmy.fyp.flight.navigation.profile;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.Objects;

import hmy.fyp.flight.Login;
import hmy.fyp.flight.R;
import hmy.fyp.flight.dao.UserDao;
import hmy.fyp.flight.databinding.FragmentProfileBinding;
import hmy.fyp.flight.utils.ImageBase64Util;

public class ProfileFragment extends Fragment {
    private SharedPreferences preferences;
    private FragmentProfileBinding binding;
    private ImageView UserPhoto;
    private Button logout_button;
    private TextView UserName;
    private String user_id, username;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        UserPhoto = binding.profileUserPhoto;
        logout_button = binding.profileLogoutButton;
        UserName = binding.profileUserName;

        preferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE);
        user_id = preferences.getString("user_id", "");
        username = preferences.getString("user_name", "");

        initView();

        getPhoto();

        return binding.getRoot();
    }

    /**
     * Function: Handler for User Profile
     */
    private final Handler handler_profile = new Handler(Objects.requireNonNull(Looper.myLooper())) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Toast.makeText(getActivity(), "Failed to get photo!", Toast.LENGTH_SHORT).show();
                UserPhoto.setImageResource(R.drawable.user_icon);   // if no photo, set default photo
            } else if (msg.what == 1) {
                String photo = (String) msg.obj;
                Bitmap bitmap = ImageBase64Util.base64ToImage(photo);
                UserPhoto.setImageBitmap(bitmap);
            } else if (msg.what == 2) {
                Toast.makeText(getActivity(), "Upload photo successfully!", Toast.LENGTH_SHORT).show();
                getPhoto();
            } else if (msg.what == 3) {
                Toast.makeText(getActivity(), "Upload photo failed!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Function: Register ActivityResultLauncher for Album
     * Reference From: <a href="https://www.cnblogs.com/lihuawei/p/16574688.html">...</a>
     * Reference Method: Refer on Sample Code
     */
    private final ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                            String imageEncoded = ImageBase64Util.imageToBase64(bitmap);
                            uploadPhoto(imageEncoded);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Failed to get image!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    /**
     * Function: Initialize the view
     */
    private void initView() {
        //Log Out Button Listener
        logout_button.setOnClickListener(v -> new AlertDialog.Builder(getActivity())
                .setTitle("Message")
                .setMessage("Do you want to LOG-OUT your account?")
                .setIcon(android.R.drawable.ic_menu_help)
                .setPositiveButton("Yes", (dialog, which) -> {
                    preferences.edit().clear().apply();
                    Toast.makeText(getActivity(), "Log out successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton("No", (dialog, which) -> Toast.makeText(getActivity(), "User cancelled log out.", Toast.LENGTH_SHORT).show())
                .show());

        //Upload Photo Listener
        UserPhoto.setOnClickListener(v -> openAlbum());

        //Display user's nickname
        UserName.setText(username);
    }

    /**
     * Function: Open the album
     */
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        ActivityResultLauncher.launch(intent);
    }

    /**
     * Function: Get the user's photo from MySQL Database
     */
    private void getPhoto() {
        new Thread(() -> {
            UserDao userDao = new UserDao();
            Message msg = new Message();
            String photo = userDao.getPhoto(user_id);
            if (photo == null) {
                msg.what = 0;
            } else {
                msg.what = 1;
                msg.obj = photo;
            }
            handler_profile.sendMessage(msg);
        }
        ).start();
    }

    /**
     * Function: Upload or Update Photo to MySQL Database
     */
    public void uploadPhoto(String imageEncoded) {
        new Thread(() -> {
            UserDao userDao = new UserDao();
            Message msg = new Message();
            boolean result = userDao.uploadPhoto(user_id, imageEncoded);
            if (result) {
                msg.what = 2;
            } else {
                msg.what = 3;
            }
            handler_profile.sendMessage(msg);
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}