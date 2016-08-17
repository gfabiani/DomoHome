package com.fabiani.domohome.app.controller;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fabiani.domohome.app.R;
import com.fabiani.domohome.app.model.Dashboard;
import com.fabiani.domohome.app.model.VideoFetchr;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoFragment extends Fragment {
    private final int NUMBER_OF_JOBS = 10;
    private ImageView mImageView;
    private Toolbar mToolbar;
    private VideoFetchr mVideoFetchr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoFetchr = new VideoFetchr();
        mVideoFetchr.open();
        executeJobs(NUMBER_OF_JOBS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        mImageView = (ImageView) v.findViewById(R.id.imageView);
        mToolbar = (Toolbar) v.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);// TODO:  Implement progress bar
        return v;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            mVideoFetchr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        return fragment;
    }

    class VideoThread implements Runnable {
        private Bitmap mBitmap;

        @Override
        public void run() {
            try {
                mBitmap = mVideoFetchr.getUrlBitmap("https://" + Dashboard.sIp + "/telecamera.php");
            } catch (IOException e) {
                e.printStackTrace();
            }
            mImageView.post(() -> mImageView.setImageBitmap(mBitmap));//TODO: resize image
        }
    }

    private void executeJobs(int n) {
        ExecutorService ex = Executors.newFixedThreadPool(1);
        for (int i = 0; i < n; i++)
            ex.execute(new VideoThread());
        ex.shutdown();
    }
}

