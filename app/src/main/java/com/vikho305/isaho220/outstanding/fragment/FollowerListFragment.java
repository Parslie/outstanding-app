package com.vikho305.isaho220.outstanding.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;

import com.vikho305.isaho220.outstanding.R;

import java.util.List;

public class FollowerListFragment extends ListFragment {
    private InteractionListener parentListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        parentListener.getListItems(this);

        return view;
    }

    // Makes sure that the fragment has an interaction listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            parentListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement InteractionListener");
        }
    }

    // Detaches the interaction listener
    @Override
    public void onDetach() {
        super.onDetach();
        parentListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        parentListener.onListItemClick(item);
    }

    public void onCallback(List<String> items) {
        if (getContext() != null) {
            ListAdapter listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
            setListAdapter(listAdapter);
        }
    }

    public interface InteractionListener {
        void onListItemClick(String item);
        void getListItems(final FollowerListFragment listener);
    }

}
