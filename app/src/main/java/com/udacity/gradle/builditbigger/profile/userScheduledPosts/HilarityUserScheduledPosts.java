package com.udacity.gradle.builditbigger.profile.userScheduledPosts;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.databinding.FragmentJokeslistGenrelistBinding;
import com.udacity.gradle.builditbigger.interfaces.EnableSearch;
import com.udacity.gradle.builditbigger.interfaces.HideFAB;
import com.udacity.gradle.builditbigger.jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.models.Post;
import com.udacity.gradle.builditbigger.models.PostWrapper;
import com.udacity.gradle.builditbigger.profile.FragmentFocusLiveData;
import com.udacity.gradle.builditbigger.search.SearchDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class HilarityUserScheduledPosts extends Fragment implements EnableSearch {

    private HideFAB hideFAB;
    private JokesAdapter postAdapter;
    private List<Post> posts = new ArrayList<>();
    private String uid;
    private boolean init = true;
    private FragmentJokeslistGenrelistBinding binding;
    private UserScheduledPostsViewModel userScheduledPostsViewModel;

    public static HilarityUserScheduledPosts newInstance(HideFAB hideFAB){
        HilarityUserScheduledPosts hilarityUserSP = new HilarityUserScheduledPosts();
        Bundle bundle = new Bundle();
        bundle.putString("uid", Constants.UID);
        hilarityUserSP.setArguments(bundle);
        hilarityUserSP.hideFAB = hideFAB;
        return hilarityUserSP;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) uid = getArguments().getString(getString(R.string.uid));

        if (savedInstanceState != null) {
            posts = savedInstanceState.getParcelableArrayList(getString(R.string.posts));
            init = savedInstanceState.getBoolean(getString(R.string.init));
        }
        postAdapter = new JokesAdapter(getActivity(), posts, true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokeslist_genrelist, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        userScheduledPostsViewModel = ViewModelProviders.of(this, new UserScheduledPostViewModelFactory(uid))
                .get(UserScheduledPostsViewModel.class);
        UserScheduledPostsLiveData userScheduledPostsLiveData = userScheduledPostsViewModel.getUserScheduledPostsLiveData();
        userScheduledPostsLiveData.observe(this, postWrapper -> {
            addPostToList(postWrapper, posts);
            configureUI();
            Log.i("new_query", "new post added to list");
            if (posts.size() % 20 == 0) {
                Log.i("new_query", "startAt set");
                userScheduledPostsLiveData.setStartAt(postWrapper.getPost().getInverseTimeStamp());
            }
        });
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setAdapter(postAdapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0) hideFAB.hideFAB();
                if (llm.findLastVisibleItemPosition() >= (posts.size() - 5)) {
                    userScheduledPostsLiveData.newQuery();
                    Log.i("new_query", "new query called from fragment");
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) hideFAB.showFAB();

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        FragmentFocusLiveData.getFragmentFocusLiveData().observe(this, position -> {
            if (position == 0) hideFAB.getFAB().setOnClickListener(view -> showSearchDialog());
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (init) hideFAB.getFAB().setOnClickListener(view -> showSearchDialog());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.posts), (ArrayList<? extends Parcelable>) posts);
        outState.putBoolean(getString(R.string.init), init);
    }

    public void showSearchDialog() {
        SearchDialogFragment.getInstance(this).show(getFragmentManager(), getString(R.string.search));
        init = false;
    }

    public void configureUI() {
        if (posts.isEmpty()) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noItemImageview.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noItemImageview.setVisibility(View.GONE);
        }
    }

    public void addPostToList(PostWrapper postWrapper, List<Post> jokes) {
        if (!postAdapter.getJokes().equals(jokes)) postAdapter.setJokes(jokes);
        Post post = postWrapper.getPost();
        if (!jokes.contains(post)) {
            jokes.add(post);
            postAdapter.notifyDataSetChanged();
        } else if (postWrapper.getState() == PostWrapper.EDITTED) {
            //if post gets modified
            int index = jokes.indexOf(post);
            jokes.set(index, post);
            postAdapter.notifyDataSetChanged();
        } else if (postWrapper.getState() == PostWrapper.REMOVED) {
            jokes.remove(post);
            postAdapter.notifyDataSetChanged();
        }
    }

    public void configureFAM() {
        hideFAB.getFAM().setVisibility(View.VISIBLE);
        hideFAB.getFAM().setOnMenuButtonClickListener(view -> {
            postAdapter.setJokes(posts);
            hideFAB.getFAM().setVisibility(View.GONE);
        });
    }

    @Override
    public void search(String keyword) {
        List<Post> searchedPosts = new ArrayList<>();
        userScheduledPostsViewModel.getSearchUserScheduledPostsLiveData(keyword).observe(this, postWrapper -> {
            addPostToList(postWrapper, searchedPosts);
            configureFAM();
        });
    }

    public FragmentJokeslistGenrelistBinding getBinding() {
        return binding;
    }
}
