package com.example.flight.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

/**
 * Reference Function: Convert Image to Base64 Encode
 * Reference From: Forget....
 */
public class ImageBase64Utils {

    /**
     * Function: Bitmap to Base64
     */
    public static String imageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String baseStr = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return baseStr;
    }

    /**
     * Function: Base64 to Bitmap
     */
    public static Bitmap base64ToImage(String base64Str) {
        byte[] byteArray = Base64.decode(base64Str, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }
}
