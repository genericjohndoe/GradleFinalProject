package com.udacity.gradle.builditbigger.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FeedExplorePageBinding;
import com.udacity.gradle.builditbigger.jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.models.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * CLASS SHOWS posts of followed users
 */

public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private JokesAdapter jokeAdapter;
    private List<Post> jokes;
    private FeedExplorePageBinding bind;
    private String uid;
    private boolean enableSwipeToRefresh = false;
    private boolean searched = false;

    public static FeedFragment newInstance(String uid){
        FeedFragment feedFragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        feedFragment.setArguments(bundle);
        return feedFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getString("uid");
        jokes = new ArrayList<>();
        jokeAdapter = new JokesAdapter(getActivity(), jokes,false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater,R.layout.feed_explore_page, container, false);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, true);
        llm.setStackFromEnd(true);
        bind.recyclerView.setLayoutManager(llm);
        bind.recyclerView.setAdapter(jokeAdapter);
        bind.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0){
                    bind.fab.hide(true);
                    enableSwipeToRefresh = true;
                    //bind.refreshButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) bind.fab.show(true);
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        bind.fab.setOnClickListener(view -> showSearchDialog());
        FeedViewModel feedViewModel = ViewModelProviders.of(this, new FeedViewModelProvider(uid)).get(FeedViewModel.class);
        feedViewModel.getFeedLiveData().observe(this, jokeWrapper -> {
            switch (jokeWrapper.getState()) {
                case 1:
                    if (!jokes.contains(jokeWrapper.getPost())){
                        jokes.add(jokeWrapper.getPost());
                        if (!enableSwipeToRefresh){
                            refreshLayout();
                        } else {
                            //bind.refreshButton.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case 2:
                    int index = jokes.indexOf(jokeWrapper.getPost());
                    jokes.set(index, jokeWrapper.getPost());
                    jokeAdapter.notifyDataSetChanged();
                    break;
                default:
                    jokes.remove(jokeWrapper.getPost());
                    jokeAdapter.notifyDataSetChanged();
                    break;
            }
            if (jokes.size() == 0 || jokes.size() == 1) configureUI();
        });
        //bind.refreshButton.setOnClickListener(view -> refreshLayout());
        bind.swipe.setOnRefreshListener(this);
        configureUI();
        return bind.getRoot();
    }

    public void configureUI() {
        if (jokes.isEmpty()) {
            bind.recyclerView.setVisibility(View.GONE);
            bind.noItemImageview.setVisibility(View.VISIBLE);
        } else {
            bind.recyclerView.setVisibility(View.VISIBLE);
            bind.noItemImageview.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        refreshLayout();
    }

    public void refreshLayout(){
        jokeAdapter.notifyDataSetChanged();
        bind.recyclerView.scrollToPosition(jokes.size() - 1);
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
                        Set<String> tags = post.getMetaData().keySet();
                        if (tags.retainAll(splitSearchKeyword) && tags.size() > 0) searches.add(post);
                    }
                    jokeAdapter.setJokes(searches);
                    bind.recyclerView.scrollToPosition(searches.size() - 1);
                })
                .onNegative((dialog, which) -> dialog.dismiss())
                .show().setCanceledOnTouchOutside(false);
    }
}
