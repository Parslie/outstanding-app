package com.vikho305.isaho220.outstanding.ui.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.CornerTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.data.Post;
import com.vikho305.isaho220.outstanding.data.User;
import com.vikho305.isaho220.outstanding.ui.adapter.PostAdapter;
import com.vikho305.isaho220.outstanding.ui.viewmodel.ContextualViewModelFactory;
import com.vikho305.isaho220.outstanding.ui.viewmodel.LoginViewModel;
import com.vikho305.isaho220.outstanding.ui.viewmodel.ProfileViewModel;
import com.vikho305.isaho220.outstanding.util.Resource;
import com.vikho305.isaho220.outstanding.util.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener  {
    private static final String USER_ID_ARG = "user_id";

    private ShapeableImageView profilePicture;
    private TextView username, description;
    private TextView followerCount, followingCount;
    private Button followButton;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView postRecyclerView;
    private PostAdapter postAdapter;

    private String userId;
    private ProfileViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(USER_ID_ARG, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            userId = getArguments().getString(USER_ID_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setRetainInstance(true); // TODO: fix retaining data, and not fetching every time, resetting current page counter

        profilePicture = view.findViewById(R.id.profilePicture);
        username = view.findViewById(R.id.profileName);
        description = view.findViewById(R.id.profileDescription);
        followerCount = view.findViewById(R.id.profileFollowerCount);
        followingCount = view.findViewById(R.id.profileFollowingCount);
        followButton = view.findViewById(R.id.profileFollowBtn);

        swipeRefreshLayout = view.findViewById(R.id.profileSwipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        postRecyclerView = view.findViewById(R.id.profilePosts);
        postRecyclerView.addItemDecoration(new DividerItemDecoration(postRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(getContext());
        postRecyclerView.setAdapter(postAdapter);

        initViewModel();

        return view;
    }

    private void initViewModel() {
        ContextualViewModelFactory contextualViewModelFactory = new ContextualViewModelFactory(requireContext());
        viewModel = new ViewModelProvider(requireActivity(), contextualViewModelFactory).get(ProfileViewModel.class);

        viewModel.getUser().observe(requireActivity(), new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
                if (userResource.getStatus() == Status.SUCCESS) {
                    User user = userResource.getData();
                    username.setText(user.getUsername());
                    description.setText(user.getDescription());
                    followerCount.setText(getString(R.string.follower_count, user.getFollowerCount()));
                    followingCount.setText(getString(R.string.following_count, user.getFollowingCount()));
                }
            }
        });
        viewModel.getPosts().observe(requireActivity(), new Observer<Resource<List<Post>>>() {
            @Override
            public void onChanged(Resource<List<Post>> listResource) {
                switch (listResource.getStatus()) {
                    case SUCCESS:
                        postAdapter.addPosts(listResource.getData());
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                    case LOADING:
                        break;
                }
            }
        });

        viewModel.fetchUser(userId);
        viewModel.fetchPosts(userId);
    }

    @Override
    public void onRefresh() {
        postAdapter.setPosts(new ArrayList<Post>());
        viewModel.resetPosts();
        viewModel.fetchPosts(userId);
    }
}