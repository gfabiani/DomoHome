package com.fabiani.domohome.controller;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.fabiani.domohome.R;
import com.fabiani.domohome.model.Dashboard;
import com.fabiani.domohome.model.VideoFetchr;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Giovanni on 27/08/2016.
 */
public class VideoFragment extends Fragment {
    private ImageView mImageView;
    private VideoFetchr mVideoFetchr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int NUMBER_OF_ITERATIONS = 10;
        mVideoFetchr = new VideoFetchr();
        mVideoFetchr.open();
        executeTask(NUMBER_OF_ITERATIONS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video, container,false);
        mImageView = (ImageView) v.findViewById(R.id.imageView);
        Toolbar mToolbar = (Toolbar) v.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.fragment_video_menu, menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mVideoFetchr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void executeTask(int n) {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        for (int i = 0; i < n; i++)
            ex.execute(new VideoThread());
        ex.shutdown();
    }


    static VideoFragment newInstance() {
        return new VideoFragment();
    }

    private class VideoThread implements Runnable {
        private Bitmap mBitmap;

        @Override
        public void run() {
            try {
                mBitmap = mVideoFetchr.getUrlBitmap("https://" + Dashboard.sIp + "/telecamera.php");//TODO: progress bar
            } catch (IOException e) {
                e.printStackTrace();
            }
            mImageView.post(() -> mImageView.setImageBitmap(mBitmap));//TODO: resize image to fit bitmap
        }
    }
}
