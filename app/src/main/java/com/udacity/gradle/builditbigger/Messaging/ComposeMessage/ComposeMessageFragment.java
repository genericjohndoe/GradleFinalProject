package com.udacity.gradle.builditbigger.Messaging.ComposeMessage;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import com.example.chipslibrary.models.ChipInterface;
import com.example.chipslibrary.views.ChipsInputEditText;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Database.SearchViewModel;
import com.udacity.gradle.builditbigger.Interfaces.CreateChip;
import com.udacity.gradle.builditbigger.Messaging.SentMessages.SentMessagesFragment;
import com.udacity.gradle.builditbigger.Messaging.Transcripts.TranscriptFragment;
import com.udacity.gradle.builditbigger.Models.Message;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.databinding.FragmentComposeMessageBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * to be shown when user is picking people to message
 */
public class ComposeMessageFragment extends Fragment implements CreateChip {

    //todo connect edit text and other recycler view show that before typing user sees
    //todo chron list of people messaged, then people in network, then queries master list of user
    //todo once user is picked from bottom recyclerview, add user chips to top recycler view

    private List<HilarityUser> networkChipList;
    private String uid;
    private FragmentComposeMessageBinding bind;
    private UsersToMessageAdapter usersToMessageAdapter;

    public static ComposeMessageFragment newInstance(String uid){
        ComposeMessageFragment composeMessageFragment = new ComposeMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid",uid);
        composeMessageFragment.setArguments(bundle);
        return composeMessageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkChipList = new ArrayList<>();
        usersToMessageAdapter = new UsersToMessageAdapter(networkChipList,this, getActivity());
        uid = getArguments().getString("uid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater,R.layout.fragment_compose_message, container, false);
        ChipsInputEditText chipsInputEditText = bind.chipsInput.getAccessToEditText();
        chipsInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //todo if user isn't in network query inverse user list path
                //todo when cell is clicked in recyclerview, add chip
                ArrayList<HilarityUser> newList = filter(""+s, networkChipList);
                if (newList.size() > 0) {
                    //todo add a set list method
                    usersToMessageAdapter = new UsersToMessageAdapter(newList, ComposeMessageFragment.this, getActivity());
                    usersToMessageAdapter.notifyDataSetChanged();
                }else{
                    usersToMessageAdapter = new UsersToMessageAdapter(newList, ComposeMessageFragment.this, getActivity());
                    SearchViewModel searchViewModel = ViewModelProviders.of(ComposeMessageFragment.this).get(SearchViewModel.class);
                    searchViewModel.getUsersFromName("%"+s+"%").observe(ComposeMessageFragment.this, user -> {
                        newList.add(user);
                        usersToMessageAdapter.notifyDataSetChanged();
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        bind.userMessageRecyclerview.setAdapter(usersToMessageAdapter);
        bind.incomingMessageEdittext.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
                // If the event is a key-down event on the "enter" button
            //todo add to make time stamp, create constant method for timestamp
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    List<String> sendTo = new ArrayList<>();
                    List<HilarityUserChip> list = (List<HilarityUserChip>) bind.chipsInput.getSelectedChipList();
                    for (HilarityUserChip chip: list){
                        sendTo.add((String) chip.getId());
                    }
                    Collections.sort(sendTo);
                    String text = bind.incomingMessageEdittext.getText().toString();
                    String path = sendTo.toString().substring(1, sendTo.toString().length()-1);
                    Constants.DATABASE.child("messages/"+Constants.UID+"/"+path+"/messagelist").push()
                            .setValue(new Message(Constants.USER,text,"02/01/2018"));
                    InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(bind.incomingMessageEdittext.getWindowToken(), 0);
                    Constants.changeFragment(R.id.hilarity_content_frame, TranscriptFragment.newInstance(Constants.UID,path));
                    return true;
                }
                return false;
        });
        bind.incomingMessageEdittext.setOnFocusChangeListener((view, hasFocus) -> {
            usersToMessageAdapter = new UsersToMessageAdapter(new ArrayList<>(), ComposeMessageFragment.this, getActivity());
            usersToMessageAdapter.notifyDataSetChanged();
        });
        NetworkViewModel networkViewModel = ViewModelProviders.of(this, new NetworkViewModelFactory(uid)).get(NetworkViewModel.class);
        networkViewModel.getNetworkLiveData().observe(this, user -> {
            networkChipList.add(user);
            usersToMessageAdapter.notifyDataSetChanged();
        });
        return bind.getRoot();
    }


    class HilarityUserChip implements ChipInterface {
        HilarityUser hilarityUser;

        HilarityUserChip(HilarityUser hilarityUser){
            this.hilarityUser = hilarityUser;
        }

        @Override
        public Drawable getAvatarDrawable() {
            return null;
        }

        @Override
        public Object getId() {
            return hilarityUser.getUID();
        }

        @Override
        public String getInfo() {
            return null;
        }

        @Override
        public String getLabel() {
            return hilarityUser.getUserName();
        }

        @Override
        public Uri getAvatarUri() {
            return Uri.parse(hilarityUser.getUrlString());
        }
    }

    @Override
    public void addChipView(HilarityUser hu) {
        bind.chipsInput.addChip(new HilarityUserChip(hu));
    }

    private ArrayList<HilarityUser> filter(String str, List<HilarityUser> list){
        ArrayList<HilarityUser> newList = new ArrayList<>();
        for (HilarityUser user: list){
            if (user.getUserName().contains(str)) newList.add(user);
        }
        return newList;
    }
}
