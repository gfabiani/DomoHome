package com.fabiani.domohome.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Giovanni on 23/06/2016.
 *
 * Model for VideoDoorPhone implementation
 * This class contains two methods to open and close connection with the server in order to grab video
 * A  third method is responsible of receiving  bitmaps from the server
 */

public class VideoFetchr {
    private static final String TAG = "VideoFetchr";
    private GestioneSocketComandi mGestioneSocketComandi;
    private DataInputStream mDataInputStream;

    public Bitmap getUrlBitmap(String urlSpec) throws IOException {
        URL myUrl = new URL(urlSpec);
        SSLSetup.overrideTrustManager();
        HttpsURLConnection urlc = (HttpsURLConnection) myUrl.openConnection();
        urlc.setRequestMethod("GET");
        urlc.setDoOutput(true);
        urlc.setUseCaches(false);
        urlc.connect();
        mDataInputStream = new DataInputStream(urlc.getInputStream());
        return BitmapFactory.decodeStream(mDataInputStream);
    }

    public void open() {// TODO: Implement further commands, e.g. brightness contrast, etc...
        mGestioneSocketComandi = new GestioneSocketComandi();
        if (mGestioneSocketComandi.connect(Dashboard.sIp, Dashboard.PORT, Dashboard.sPasswordOpen)) {
            mGestioneSocketComandi.invia("*6*9**##");
            Log.i(TAG, "Video grabber activated");
            mGestioneSocketComandi.invia("*6*0*4000##");
            Log.i(TAG, "Camera activated"); //TODO: make 40  constant  and the rest of the string   variable
        }
    }

    public void close() throws IOException {
        mDataInputStream.close();
        mGestioneSocketComandi.close();
    }

    public String getVideoWhereValue(int n) {
        return (n<10?"0":"")+n;
    }
}


