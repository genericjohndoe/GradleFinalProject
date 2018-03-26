package com.udacity.gradle.builditbigger.Dialog;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.udacity.gradle.builditbigger.NewPost.MediaAdapter;
import com.udacity.gradle.builditbigger.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewGifPost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewGifPost extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    MediaAdapter mediaAdapter;
    public NewGifPost() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NewGifPost.
     */
    public static NewGifPost newInstance() {
       return new NewGifPost();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_gif_post, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "= ?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif");
        String[] selectionArgsPdf = new String[]{ mimeType };
        return new CursorLoader(getActivity(), MediaStore.Files.getContentUri("external"), null,
                selectionMimeType, selectionArgsPdf, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.setNotificationUri(getActivity().getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        mediaAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mediaAdapter.swapCursor(null);
    }

}
