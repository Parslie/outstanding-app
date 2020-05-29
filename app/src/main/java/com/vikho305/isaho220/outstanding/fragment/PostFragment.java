package com.vikho305.isaho220.outstanding.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.vikho305.isaho220.outstanding.OnClickCallback;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.database.User;

public class PostFragment extends Fragment implements View.OnClickListener {

    public static final String AUTHOR_CLICK_KEY = "author";

    private TextView titleView, textView;
    private ImageView imageView;
    private VideoView videoView;
    private ImageView authorPicture;
    private TextView authorUsername;

    private OnClickCallback onClickCallback;

    public void setOnClickCallback(OnClickCallback onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        // Get views
        titleView = view.findViewById(R.id.postFrag_title);
        textView = view.findViewById(R.id.postFrag_text);
        imageView = view.findViewById(R.id.postFrag_image);
        videoView = view.findViewById(R.id.postFrag_video);
        authorPicture = view.findViewById(R.id.postFrag_authorPicture);
        authorUsername = view.findViewById(R.id.postFrag_author);

        // Init listeners
        authorPicture.setOnClickListener(this);
        authorUsername.setOnClickListener(this);

        return view;
    }

    public void updateDetails(Post post) {
        titleView.setText(post.getTitle());

        // Media
        switch (post.getMediaType()) {
            case Post.TEXT_TYPE:
                imageView.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                textView.setText(post.getText());
                break;
            case Post.IMAGE_TYPE:
                // TODO: set videoView to GONE
                System.out.println(post.getMedia());

                /*
                byte[] imageBytes = Base64.decode(post.getMedia(), Base64.DEFAULT);
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imageView.setImageBitmap(imageBitmap);
                */
                textView.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                Uri imageUri = Uri.parse(post.getMedia());
                imageView.setImageURI(imageUri);

                break;
            case Post.VIDEO_TYPE:
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                Uri videoUri = Uri.parse(post.getMedia());
                videoView.setVideoURI(videoUri);
                videoView.start();

                // TODO: add decoding off video
                break;
            default:
                // TODO: add error case
                break;
        }

        // Author
        User author = post.getAuthor();
        authorUsername.setText(author.getUsername());

        byte[] authorBytes = Base64.decode(author.getProfile().getPicture(), Base64.DEFAULT);
        Bitmap authorBitmap = BitmapFactory.decodeByteArray(authorBytes, 0, authorBytes.length);
        RoundedBitmapDrawable authorDrawable = RoundedBitmapDrawableFactory.create(getResources(), authorBitmap);
        authorDrawable.setCircular(true);
        authorPicture.setImageDrawable(authorDrawable);
    }

    @Override
    public void onClick(View v) {
        if (v == authorUsername || v == authorPicture) {
            onClickCallback.onClickCallback(AUTHOR_CLICK_KEY);
        }
    }
}
