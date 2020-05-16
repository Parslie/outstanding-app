package com.vikho305.isaho220.outstanding.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.User;

public class LockedProfileActivity extends AuthorizedActivity {

    private User user;

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_locked);

        user = getIntent().getParcelableExtra("user");

        if(user != null){
            View root = findViewById(R.id.lockedProfile_root);
            TextView username = findViewById(R.id.lockedProfile_username);
            TextView description = findViewById(R.id.lockedProfile_description);
            ImageView profilePictureView = findViewById(R.id.lockedProfile_picture);

            String u = user.getUsername();
            String d = user.getDescription();

            profilePictureView.setImageBitmap(StringToBitMap(user.getPicture()));

            float[] hsv = new float[3];
            hsv[0] = (float)user.getHue();
            hsv[1] = (float)user.getSaturation();
            hsv[2] = (float)user.getLightness();
            root.setBackgroundColor(Color.HSVToColor(hsv));

            username.setText(u);
            description.setText(d);
        }
    }
}
