package com.vikho305.isaho220.outstanding.data.media;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageMedia {
    private static final int COMPRESSION_AMOUNT = 20;

    public String uriToBase64(ContentResolver contentResolver, Uri uri) {
        try {
            android.graphics.ImageDecoder.Source imageSource = android.graphics.ImageDecoder.createSource(contentResolver, uri);
            Bitmap imageBitmap = android.graphics.ImageDecoder.decodeBitmap(imageSource);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_AMOUNT, byteStream);
            return Base64.encodeToString(byteStream.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public Bitmap base64ToBitmap(String base64) {
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
