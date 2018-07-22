package com.udacity.gradle.builditbigger.NewPost.VisualMediaPost;

import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.Log;

import com.udacity.gradle.builditbigger.Interfaces.IntentCreator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class GifEncoderAsnycTask extends AsyncTask<File, Void, String> {
    private String path;
    private IntentCreator intentCreator;
    private String number;

    public GifEncoderAsnycTask(String path, IntentCreator intentCreator, String number){
        this.path = path;
        this.intentCreator = intentCreator;
        this.number = number;
    }
    @Override
    protected String doInBackground(File[] files) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(files[0].getPath());
        } catch (Exception e){
            Log.i("VMPF", e.toString());
        }
        //int framesRate = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE));
        int duration = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        Log.i("gifduration","" + duration);
        //Log.i("giffps","" + framesRate);
        int millisPerframe = 1000/24;
        int timeAt = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setDelay(0);
        encoder.setRepeat(100);
        encoder.setFrameRate(24);
        encoder.start(bos);
        while (timeAt < duration){
            encoder.addFrame(mmr.getFrameAtTime(timeAt));
            timeAt += millisPerframe;
            Log.i("wenfhluwhru", timeAt+"");
        }
        encoder.finish();
        Log.i("wenfhluwhru", "loop finished");
        FileOutputStream outStream = null;
        File file = new File(path);
        try {
            outStream = new FileOutputStream(file);
            outStream.write(bos.toByteArray());
            outStream.close();
            Log.i("wenfhluwhru", "outstream closed");
            return file.getAbsolutePath();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String file) {
        super.onPostExecute(file);
        intentCreator.createIntent(file,number);
    }
}
