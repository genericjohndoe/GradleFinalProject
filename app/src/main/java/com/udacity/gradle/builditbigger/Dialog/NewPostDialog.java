package com.udacity.gradle.builditbigger.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.udacity.gradle.builditbigger.R;

/**
 * Created by joeljohnson on 11/3/17.
 */

public class NewPostDialog extends DialogFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_post, container, false);
//        viewPager = root.findViewById(R.id.viewpager);
//        viewPager.setAdapter(new PostPagerAdapter(getChildFragmentManager()));
        changeFragment(new NewTextPost());
        tabLayout = root.findViewById(R.id.tablayout);
        //tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) changeFragment(new NewTextPost());
                if (tab.getPosition() == 1) changeFragment(new NewImagePost());
                if (tab.getPosition() == 2) changeFragment(new NewVideoPost());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        //TODO SET TABS TO IMAGES
        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(R.layout.dialog_new_post)
//                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //TODO take into account viewpager
//                        //TODO retrieve info from fragment
//                        //TODO create path on database
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        NewPostDialog.this.getDialog().cancel();
//                    }
//                });

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
        //return builder.create();
    }

    private void changeFragment(Fragment fragment){
        getChildFragmentManager().beginTransaction()
                .replace(R.id.new_post_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

//    private class PostPagerAdapter extends FragmentPagerAdapter {
//        public PostPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        Fragment[] fragmentArray = new Fragment[]{new NewTextPost(), new NewImagePost(), new NewVideoPost()};
//
//        @Override
//        public int getCount() {
//            return fragmentArray.length;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return fragmentArray[position];
//        }
//    }
}
