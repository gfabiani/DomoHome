package com.fabiani.domohome.app.controller;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

public class VideoFragment extends Fragment {
    private ImageView mImageView;
    private Toolbar mToolbar;
    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new VideoFetchTask().execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        mImageView = (ImageView) v.findViewById(R.id.imageView);
        mToolbar = (Toolbar) v.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return v;
    }

    public class VideoFetchTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            byte[] b = new byte[0];
            try {
                b = new VideoFetchr().getUrlBytes("https://" + Dashboard.sIp + "/telecamera.php");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            mImageView.setImageBitmap(result);

        }
    }

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        return fragment;
    }
}

