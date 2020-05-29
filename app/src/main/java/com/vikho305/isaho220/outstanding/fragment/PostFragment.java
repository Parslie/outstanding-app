package com.vikho305.isaho220.outstanding.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.CustomJsonObjectRequest;
import com.vikho305.isaho220.outstanding.OnClickCallback;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.activity.PostActivity;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.database.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostFragment extends Fragment implements View.OnClickListener {

    public static final String AUTHOR_CLICK_KEY = "author";

    private TextView titleView, textView;
    private TextView likeCountView, dislikeCountView;

    private ImageView imageView;
    private VideoView videoView;
    private ImageView authorPicture;
    private TextView authorUsername;

    private Button likeButton;
    private Button dislikeButton;

    private OnClickCallback onClickCallback;

    private Post activePost;

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

        likeCountView = view.findViewById(R.id.postFrag_likeCount);
        dislikeCountView = view.findViewById(R.id.postFrag_dislikeCount);

        imageView = view.findViewById(R.id.postFrag_image);
        videoView = view.findViewById(R.id.postFrag_video);
        authorPicture = view.findViewById(R.id.postFrag_authorPicture);
        authorUsername = view.findViewById(R.id.postFrag_author);

        likeButton = view.findViewById(R.id.postFrag_like);
        dislikeButton = view.findViewById(R.id.postFrag_dislike);

        // Init listeners
        authorPicture.setOnClickListener(this);
        authorUsername.setOnClickListener(this);

        likeButton.setOnClickListener(this);
        dislikeButton.setOnClickListener(this);

        return view;
    }

    public void updateDetails(Post post) {
        titleView.setText(post.getTitle());
        activePost = post;
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
        else if (v == likeButton) {
            ((PostActivity) Objects.requireNonNull(getActivity())).likePost(activePost.getId());
            likeButton.setEnabled(false);
            if(!dislikeButton.isEnabled()){
                ((PostActivity) Objects.requireNonNull(getActivity())).unDislikePost(activePost.getId());
                dislikeButton.setEnabled(true);
            }
            //likeCountView.setText(activePost.getLikeCount());
            //dislikeCountView.setText(activePost.getDislikeCount());
        }
        else if (v == dislikeButton) {
            ((PostActivity) Objects.requireNonNull(getActivity())).dislikePost(activePost.getId());
            dislikeButton.setEnabled(false);
            if(!likeButton.isEnabled()){
                ((PostActivity) Objects.requireNonNull(getActivity())).unLikePost(activePost.getId());
                likeButton.setEnabled(true);
            }
            //likeCountView.setText(activePost.getLikeCount());
            //dislikeCountView.setText(activePost.getDislikeCount());
        }
    }

}
