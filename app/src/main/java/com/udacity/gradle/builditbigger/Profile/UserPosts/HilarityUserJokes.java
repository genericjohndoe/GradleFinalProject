package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.udacity.gradle.builditbigger.Interfaces.HideFAB;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * HilarityUserJokes class shows user generated posts
 */

public class HilarityUserJokes extends Fragment {
    //todo test search and back press
    Profile profile;
    JokesAdapter jokeAdapter;
    List<Post> jokes = new ArrayList<>();
    private FragmentJokeslistGenrelistBinding binding;
    private String uid;
    private boolean searched = false;

    public static HilarityUserJokes newInstance(String uid) {
        HilarityUserJokes hilarityUserJokes = new HilarityUserJokes();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        hilarityUserJokes.setArguments(bundle);
        return hilarityUserJokes;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString("uid");
        }
        if (savedInstanceState != null) jokes = savedInstanceState.getParcelableArrayList("posts");
        jokeAdapter = new JokesAdapter(getActivity(), jokes, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokeslist_genrelist, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        llm.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setAdapter(jokeAdapter);
        profile = (Profile) getActivity().getSupportFragmentManager().findFragmentByTag("profile");
        Log.i("profilefragment", profile.toString());


        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0) profile.hideFAB();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) profile.showFAB();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        //returns full list of posts after search
        binding.recyclerView.setOnKeyListener((v, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_BACK && searched){
                jokeAdapter.setJokes(jokes);
                searched = false;
                configureUI();
                binding.recyclerView.scrollToPosition(jokes.size() - 1);
                return true;
            }
            return false;
        });

        UserPostsViewModel userPostsViewModel = ViewModelProviders.of(this,
                new UserPostViewModelFactory(uid))
                .get(UserPostsViewModel.class);
        userPostsViewModel.getUserPostsLiveData().observe(this, joke -> {

            if (!jokes.contains(joke)){
                jokes.add(joke);
            } else {
                //if post gets modified
                int index = jokes.indexOf(joke);
                jokes.remove(joke);
                jokes.add(index,joke);
                jokeAdapter.notifyDataSetChanged();
            }
            if (!searched) {
                jokeAdapter.notifyDataSetChanged();
                configureUI();
                binding.recyclerView.scrollToPosition(jokes.size() - 1);
            }
        });

        configureUI();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        profile.getFAB().setOnClickListener(view -> showSearchDialog());
        Log.i("profilefragment",profile.getFAB().toString() + " HUJ");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("posts",(ArrayList<? extends Parcelable>) jokes);
    }

    public void showSearchDialog() {
        new MaterialDialog.Builder(getActivity())
                .customView(R.layout.search, true)
                .positiveText("Search")
                .negativeText("Cancel")
                .onPositive((dialog, which) -> {
                            searched = true;
                            View view2 = dialog.getCustomView();
                            String searchKeyword = ((EditText) view2.findViewById(R.id.search)).getText().toString();
                            List<Post> searches = new ArrayList<>();
                            List<String> splitSearchKeyword = Arrays.asList(searchKeyword.split(" "));
                            for (Post post: jokes){
                                Set<String> tags = post.getMetaData().getTags().keySet();
                                if (tags.retainAll(splitSearchKeyword) && tags.size() > 0) searches.add(post);
                            }
                            jokeAdapter.setJokes(searches);
                            binding.recyclerView.scrollToPosition(searches.size() - 1);
                        })
                .onNegative((dialog, which) -> dialog.dismiss())
                .show().setCanceledOnTouchOutside(false);
    }

    public void configureUI() {
        if (jokes.isEmpty()) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noItemImageview.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noItemImageview.setVisibility(View.GONE);
        }
    }
}
