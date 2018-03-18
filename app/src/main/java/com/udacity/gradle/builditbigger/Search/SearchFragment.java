package com.udacity.gradle.builditbigger.Search;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentSearchBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    //todo stuff
    public SearchFragment() {}

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSearchBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_search, container, false);
        bind.viewPager.setAdapter(new SearchPagerAdapter(getActivity().getSupportFragmentManager()));
        bind.tabLayout.setupWithViewPager(bind.viewPager);
        SearchHilarityViewModel searchHilarityViewModel = ViewModelProviders.of(this, new SearchHilarityViewModelProvider()).get(SearchHilarityViewModel.class);
        Log.i("HilarityQuery4", searchHilarityViewModel.toString());
        bind.searcheditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchHilarityViewModel.getSearchQuery().setValue(""+s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        return bind.getRoot();
    }

    private class SearchPagerAdapter extends FragmentPagerAdapter {
        public SearchPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        Fragment[] fragmentArray = new Fragment[]{SearchUserFragment.newInstance(), SearchCollectionsFragment.newInstance(),
                SearchTagsFragment.newInstance(), SearchTextPostsFragment.newInstance(), SearchImagePostsFragment.newInstance(),
                SearchVideoPostsFragment.newInstance(), SearchGifPostsFragment.newInstance()};

        String[] tabTitles = new String[]{"Users", "Collections", "Tags", "Text", "Image", "Video", "Gif"};

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return fragmentArray.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArray[position];
        }
    }

}
