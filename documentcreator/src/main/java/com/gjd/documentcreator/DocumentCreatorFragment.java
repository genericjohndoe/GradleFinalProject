package com.gjd.documentcreator;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gjd.documentcreator.databinding.FragmentDocumentCreatorBinding;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentCreatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentCreatorFragment extends Fragment {

    public DocumentCreatorFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DocumentCreatorFragment.
     */
    public static DocumentCreatorFragment newInstance() {
        DocumentCreatorFragment fragment = new DocumentCreatorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentDocumentCreatorBinding bind = DataBindingUtil.inflate(inflater,
                R.layout.fragment_document_creator, container, false);
        bind.webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("androidwebview", "page finished loading " + url);
            }

        });
        String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, " +
                "testing, testing...</p></body></html>";
        bind.webview.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);
        return bind.getRoot();
    }

}
