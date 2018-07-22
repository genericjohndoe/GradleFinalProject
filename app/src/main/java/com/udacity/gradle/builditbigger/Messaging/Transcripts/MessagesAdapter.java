package com.udacity.gradle.builditbigger.Messaging.Transcripts;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Message;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.VideoLifeCyclerObserver;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * MessagesAdapter class used to format Message objects for UI
 */

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messages;
    private Context context;
    private static final int TYPE_SENT = 0;
    private static final int TYPE_RECEIVED = 1;
    private static final int TYPE_SENT_IMAGE = 2;
    private static final int TYPE_RECEIVED_IMAGE = 3;
    private static final int TYPE_SENT_VIDEO = 4;
    private static final int TYPE_RECEIVED_VIDEO = 5;


    public MessagesAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_SENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message_layout, parent, false);
                return new SentMessagesViewHolder(view);
            case TYPE_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recieved_message_layout, parent, false);
                return new RecievedMessagesViewHolder(view);
            case TYPE_SENT_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_image_messge_layout, parent, false);
                return new SentImageMessagesViewholder(view);
            case TYPE_RECEIVED_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_image_message_layout, parent, false);
                return new RecievedImageMessagesViewHolder(view);
            case TYPE_SENT_VIDEO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_video_message_layout, parent, false);
                return new SentVideoMessagesViewHolder(view);
            case TYPE_RECEIVED_VIDEO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_video_message_layout, parent, false);
                return new RecievedVideoMessagesViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder instanceof SentMessagesViewHolder) {
            if (message.getContents().size() == 1)
                ((SentMessagesViewHolder) holder).content.setText(message.getContents().get(0));
            ((SentMessagesViewHolder) holder).timeSent.setText(Constants.formattedTimeString(context, message.getTimeStamp()));
            if (holder instanceof RecievedMessagesViewHolder) {
                ((RecievedMessagesViewHolder) holder).user.setText(message.getHilarityUser().getUserName());
                Glide.with(context).load(message.getHilarityUser().getUrlString())
                        .into(((RecievedMessagesViewHolder) holder).profileImg);
            }
        } else if (holder instanceof SentImageMessagesViewholder) {
            ((SentImageMessagesViewholder) holder).timeSent.setText(Constants.formattedTimeString(context, message.getTimeStamp()));
            Constants.STORAGE.child(message.getContents().get(0)).getDownloadUrl().addOnSuccessListener(uri ->{
                Glide.with(context).load(uri).into(((SentImageMessagesViewholder) holder).imageView);
            });
            /*Glide.with(context).load(message.getContents().get(1))
                    .into(((SentImageMessagesViewholder) holder).imageView);*/
            if (holder instanceof  RecievedImageMessagesViewHolder){
                ((RecievedImageMessagesViewHolder) holder).user.setText(message.getHilarityUser().getUserName());
                Glide.with(context).load(message.getHilarityUser().getUrlString())
                        .into(((RecievedImageMessagesViewHolder) holder).profileImg);
            }
        } else {
            ((SentVideoMessagesViewHolder) holder).timeSent.setText(Constants.formattedTimeString(context, message.getTimeStamp()));
            ((SentVideoMessagesViewHolder) holder).getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_START);
            Constants.STORAGE.child(message.getContents().get(0)).getDownloadUrl().addOnSuccessListener(uri ->{
                ((SentVideoMessagesViewHolder) holder).path = uri;
                prepareVideoPlayback(((SentVideoMessagesViewHolder) holder));
            });
            //todo add code to set up video player and actually choose video
            if (holder instanceof  RecievedImageMessagesViewHolder){
                ((RecievedImageMessagesViewHolder) holder).user.setText(message.getHilarityUser().getUserName());
                Glide.with(context).load(message.getHilarityUser().getUrlString())
                        .into(((RecievedImageMessagesViewHolder) holder).profileImg);
            }
            ((SentVideoMessagesViewHolder) holder).getLifecycle().addObserver(new VideoLifeCyclerObserver(context, holder, this));

        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getContents().size() == 1) {
            if (messages.get(position).getHilarityUser().getUid().equals(Constants.UID))
                return TYPE_SENT;
            return TYPE_RECEIVED;
        } else if (Constants.isImage(messages.get(position).getContents().get(0))) {
            if (messages.get(position).getHilarityUser().getUid().equals(Constants.UID))
                return TYPE_SENT_IMAGE;
            return TYPE_RECEIVED_IMAGE;
        } else {
            if (messages.get(position).getHilarityUser().getUid().equals(Constants.UID))
                return TYPE_SENT_VIDEO;
            return TYPE_RECEIVED_VIDEO;
        }
    }


    public class SentMessagesViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView timeSent;

        public SentMessagesViewHolder(View view) {
            super(view);
            content = view.findViewById(R.id.tmessage_imageView);
            timeSent = view.findViewById(R.id.text_message_time);
        }
    }

    public class RecievedMessagesViewHolder extends SentMessagesViewHolder {
        TextView user;
        CircleImageView profileImg;

        public RecievedMessagesViewHolder(View view) {
            super(view);
            profileImg = view.findViewById(R.id.image_message_profile);
            user = view.findViewById(R.id.text_message_name);
        }
    }

    public class SentImageMessagesViewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView timeSent;

        public SentImageMessagesViewholder(View view) {
            super(view);
            imageView = view.findViewById(R.id.message_imageView);
            timeSent = view.findViewById(R.id.text_message_time);
        }
    }

    public class RecievedImageMessagesViewHolder extends SentImageMessagesViewholder {
        TextView user;
        CircleImageView profileImg;

        public RecievedImageMessagesViewHolder(View view) {
            super(view);
            profileImg = view.findViewById(R.id.image_message_profile);
            user = view.findViewById(R.id.text_message_name);
        }
    }

    public class SentVideoMessagesViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner {
        SimpleExoPlayerView exoPlayerView;
        TextView timeSent;
        private LifecycleRegistry mLifecycleRegistry;
        private Uri path;

        public SentVideoMessagesViewHolder(View view) {
            super(view);
            mLifecycleRegistry = new LifecycleRegistry(this);
            exoPlayerView = view.findViewById(R.id.message_videoView);
            timeSent = view.findViewById(R.id.text_message_time);
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return mLifecycleRegistry;
        }

        public LifecycleRegistry getmLifecycleRegistry() {
            return mLifecycleRegistry;
        }

        public SimpleExoPlayerView getExoPlayerView() {
            return exoPlayerView;
        }
    }

    public class RecievedVideoMessagesViewHolder extends SentVideoMessagesViewHolder {
        TextView user;
        CircleImageView profileImg;

        public RecievedVideoMessagesViewHolder(View view) {
            super(view);
            profileImg = view.findViewById(R.id.image_message_profile);
            user = view.findViewById(R.id.text_message_name);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof SentVideoMessagesViewHolder){
            ((SentVideoMessagesViewHolder) holder).getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            prepareVideoPlayback(((SentVideoMessagesViewHolder) holder));
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof SentVideoMessagesViewHolder)
        ((SentVideoMessagesViewHolder) holder).getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof SentVideoMessagesViewHolder)
            ((SentVideoMessagesViewHolder) holder).getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onViewRecycled(holder);
    }

    private void prepareVideoPlayback(SentVideoMessagesViewHolder holder) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "Hilarity"), bandwidthMeter);
        SimpleCache cache = new SimpleCache(context.getCacheDir(), new LeastRecentlyUsedCacheEvictor(1024^2*100));
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(cache, dataSourceFactory);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        if (holder.exoPlayerView.getPlayer() != null && holder.path != null) {
            holder.exoPlayerView.getPlayer().prepare(new ExtractorMediaSource(Uri.parse(holder.path.toString()),
                    dataSourceFactory, extractorsFactory, null, null), false, false);
        }
    }

}
