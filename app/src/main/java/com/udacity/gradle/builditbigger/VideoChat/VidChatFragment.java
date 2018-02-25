package com.udacity.gradle.builditbigger.VideoChat;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentVidChatBinding;

import org.webrtc.IceCandidate;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;

import co.netguru.videochatguru.PeerConnectionListener;
import co.netguru.videochatguru.WebRtcAnsweringPartyListener;
import co.netguru.videochatguru.WebRtcClient;
import co.netguru.videochatguru.WebRtcOfferingActionListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VidChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VidChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public VidChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VidChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VidChatFragment newInstance(String param1, String param2) {
        VidChatFragment fragment = new VidChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentVidChatBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_vid_chat,container,false);
        WebRtcClient client = new WebRtcClient(getActivity(),1280,720,24,
                true,null,null,null,null);
        client.attachLocalView(bind.localVideoView);
        client.attachRemoteView(bind.remoteVideoView);
        //todo use live data to return ice service object data from real time database
        //todo may want to not use vid guru library and use base webRTC classes and do the entire signaling process in firebase
        PeerConnectionListener peerConnectionListener = new PeerConnectionListener() {
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {

            }

            @Override
            public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

            }

            @Override
            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

            }
        };
        WebRtcOfferingActionListener webRtcOfferingActionListener = new WebRtcOfferingActionListener() {
            @Override
            public void onError(String s) {

            }

            @Override
            public void onOfferRemoteDescription(SessionDescription sessionDescription) {
                //Called when local session description from offering party is created.
                //[localSessionDescription] object should be sent to the other party through established connection channel.
            }
        };
        WebRtcAnsweringPartyListener webRtcAnsweringPartyListener = new WebRtcAnsweringPartyListener() {
            @Override
            public void onError(String s) {

            }

            @Override
            public void onSuccess(SessionDescription sessionDescription) {
                //Triggered when local session description from answering party is created.
               //[localSessionDescription] object should be sent to the other party through established connection channel.
            }
        };
        client.initializePeerConnection(null, peerConnectionListener, webRtcOfferingActionListener, webRtcAnsweringPartyListener);
        return bind.getRoot();
    }

}
