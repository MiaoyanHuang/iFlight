package hmy.fyp.flight.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Reference Function: Convert Image to Base64 Encode
 * Reference From: Forget....
 */
public class ImageBase64Util {

    /**
     * Function: Bitmap to Base64
     */
    public static String imageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Function: Base64 to Bitmap
     */
    public static Bitmap base64ToImage(String base64Str) {
        byte[] byteArray = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}
