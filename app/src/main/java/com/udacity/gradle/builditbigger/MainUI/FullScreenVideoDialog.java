package com.udacity.gradle.builditbigger.MainUI;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.VideoLifeCyclerObserver;

public class FullScreenVideoDialog extends DialogFragment {
    private static FullScreenVideoDialog fullScreenVideoDialog;
    private SimpleExoPlayerView simpleExoPlayerView;
    private String url;
    private long position;

    public static FullScreenVideoDialog getInstance(String url, long position){
        if (fullScreenVideoDialog == null) fullScreenVideoDialog = new FullScreenVideoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putLong("position", position);
        fullScreenVideoDialog.setArguments(bundle);
        return fullScreenVideoDialog;
    }

    public static FullScreenVideoDialog getInstance(){
        if (fullScreenVideoDialog == null) fullScreenVideoDialog = new FullScreenVideoDialog();
        return fullScreenVideoDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            url = getArguments().getString("url");
            position = getArguments().getLong("position");
            Log.i("orientation4", "position = " + position + " in onCreate");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_full_screen_video, container, false);
        simpleExoPlayerView = root.findViewById(R.id.video_player);
        getLifecycle().addObserver(new VideoLifeCyclerObserver(getContext(), simpleExoPlayerView));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "Hilarity"), bandwidthMeter);
        SimpleCache cache = new SimpleCache(getActivity().getCacheDir(), new LeastRecentlyUsedCacheEvictor(1024^2*100));
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(cache, dataSourceFactory);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        if (simpleExoPlayerView.getPlayer() != null) {
            simpleExoPlayerView.getPlayer().prepare(new ExtractorMediaSource(Uri.parse(url),
                    dataSourceFactory, extractorsFactory, null, null), false, true);
            simpleExoPlayerView.getPlayer().seekTo(position);
            Log.i("orientation4", "position = " + position + " in onResume");
            simpleExoPlayerView.getPlayer().setPlayWhenReady(true);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_full_screen_video);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getPosition() {
        return position;
    }

    public String getUrl() {
        return url;
    }

    public void pause(){
        simpleExoPlayerView.getPlayer().setPlayWhenReady(false);
    }

    /*@Override
    public void onDestroy() {
        simpleExoPlayerView.getPlayer().release();
        super.onDestroy();
    }*/
}
