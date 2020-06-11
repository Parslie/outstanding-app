package com.vikho305.isaho220.outstanding.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.vikho305.isaho220.outstanding.ClickCallbackListener;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.database.User;

import java.util.Locale;

public class PostFragment extends Fragment implements View.OnClickListener {

    public static final String AUTHOR_CLICK_KEY = "postAuthor", LIKE_CLICK_KEY = "like";
    public static final String DISLIKE_CLICK_KEY = "dislike";

    private TextView titleView, textView;
    private ImageView imageView;

    private ImageView authorPicture;
    private Button likeButton;
    private Button dislikeButton;

    private ClickCallbackListener clickCallbackListener;

    public void setClickCallbackListener(ClickCallbackListener clickCallbackListener) {
        this.clickCallbackListener = clickCallbackListener; // Sets what object to call on when clicking on a view
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
        authorPicture = view.findViewById(R.id.postFrag_authorPicture);

        likeButton = view.findViewById(R.id.postFrag_like);
        dislikeButton = view.findViewById(R.id.postFrag_dislike);

        // Init listeners
        authorPicture.setOnClickListener(this);
        likeButton.setOnClickListener(this);
        dislikeButton.setOnClickListener(this);

        return view;
    }

    public void updateDetails(Post post) {
        titleView.setText(post.getTitle());
        textView.setText(post.getText());

        if (post.getTitle() == null || post.getTitle().length() == 0)
            titleView.setVisibility(View.GONE);
        if (post.getText() == null || post.getText().length() == 0)
            textView.setVisibility(View.GONE);

        // Decode media
        switch (post.getMediaType()) {
            case Post.TEXT_TYPE:
                imageView.setVisibility(View.GONE);
                break;
            case Post.IMAGE_TYPE:
                byte[] decodedPicture = Base64.decode(post.getMedia(), Base64.DEFAULT);
                Bitmap pictureBitmap = BitmapFactory.decodeByteArray(decodedPicture, 0, decodedPicture.length);
                imageView.setImageBitmap(pictureBitmap);
                break;
            default:
                // TODO: add error case
                break;
        }

        // Decode and round author picture
        User author = post.getAuthor();

        Bitmap pictureBitmap;
        if (author.getProfile().getPicture() == null) {
            pictureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_pfp);
        }
        else {
            byte[] decodedPicture = Base64.decode(author.getProfile().getPicture(), Base64.DEFAULT);
            pictureBitmap = BitmapFactory.decodeByteArray(decodedPicture, 0, decodedPicture.length);
        }

        RoundedBitmapDrawable authorDrawable = RoundedBitmapDrawableFactory.create(getResources(), pictureBitmap);
        authorDrawable.setCircular(true);
        authorPicture.setImageDrawable(authorDrawable);

        // Show like and dislike status
        likeButton.setText(String.format(Locale.ENGLISH, " %d", post.getLikeCount()));
        dislikeButton.setText(String.format(Locale.ENGLISH, " %d", post.getDislikeCount()));

        if (post.isLiked())
            likeButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_liked_24dp, null), null, null, null);
        else
            likeButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_unliked_24dp, null), null, null, null);

        if (post.isDisliked())
            dislikeButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_disliked_24dp, null), null, null, null);
        else
            dislikeButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_undisliked_24dp, null), null, null, null);
    }

    @Override
    public void onClick(View v) {
        if (v == authorPicture) {
            clickCallbackListener.onClickCallback(AUTHOR_CLICK_KEY);
        }
        else if (v == likeButton) {
            clickCallbackListener.onClickCallback(LIKE_CLICK_KEY);
        }
        else if (v == dislikeButton) {
            clickCallbackListener.onClickCallback(DISLIKE_CLICK_KEY);
        }
    }

}
