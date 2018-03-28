package com.udacity.gradle.builditbigger.NewPost;


import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.NewPost.GifPost.NewGifPost;
import com.udacity.gradle.builditbigger.NewPost.ImagePost.NewImagePost;
import com.udacity.gradle.builditbigger.NewPost.TextPost.NewTextPostEditFragment;
import com.udacity.gradle.builditbigger.NewPost.VideoPost.NewVideoPost;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewPostBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPostFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String number;

    public NewPostFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NewPostFragment.
     */
    public static NewPostFragment newInstance(String number) {
        NewPostFragment newPostFragment = new NewPostFragment();
        newPostFragment.number = number;
        return newPostFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStorageWritePermission();
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentNewPostBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_new_post, container, false);
        bind.viewpager.setAdapter(new NewPostPagerAdapter(getActivity().getSupportFragmentManager()));
        bind.tablayout.setupWithViewPager(bind.viewpager);
        return bind.getRoot();
    }

    private class NewPostPagerAdapter extends FragmentPagerAdapter {
        public NewPostPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        Fragment[] fragmentArray = new Fragment[]{NewTextPostEditFragment.newInstance(number), NewImagePost.newInstance(number),
                NewVideoPost.newInstance(number), NewGifPost.newInstance(number)};

        String[] tabTitles = new String[]{"Text", "Image", "Video", "Gif"};

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private void requestStorageWritePermission() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        }
    }

}
