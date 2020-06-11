package com.vikho305.isaho220.outstanding.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Comment;
import com.vikho305.isaho220.outstanding.database.User;

public class CommentFragment extends Fragment {
    private static final String COMMENT_PARAM = "comment";
    private Comment comment;

    public CommentFragment() {
        // Required empty public constructor
    }

    // Creates a new comment fragment and sets a comment to show
    public static CommentFragment newInstance(Comment comment) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putParcelable(COMMENT_PARAM, comment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comment = getArguments().getParcelable(COMMENT_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        // Get layout views
        ImageView pictureView = view.findViewById(R.id.commentFrag_picture);
        TextView usernameView = view.findViewById(R.id.commentFrag_username);
        TextView textView = view.findViewById(R.id.commentFrag_text);

        // Init fragment
        usernameView.setText(comment.getAuthor().getUsername());
        textView.setText(comment.getText());

        // Decode and round author picture
        User author = comment.getAuthor();

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
        pictureView.setImageDrawable(authorDrawable);

        return view;
    }
}
