package com.fabiani.domohome.app.model;

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
    private static final int NOT_CONNECTED=0;
    private static final int CONNECTED=3;
    private static GestioneSocketComandi
            sGestioneSocketComandi=new GestioneSocketComandi();
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
        return BitmapFactory.decodeStream(mDataInputStream);//TODO: resize image
    }

    public  void open() {
        switch (GestioneSocketComandi.stato){
            case NOT_CONNECTED:
                if (sGestioneSocketComandi.connect(Dashboard.sIp, Dashboard.PORT, Dashboard.sPasswordOpen)) {
                    sGestioneSocketComandi.invia("*6*9**##");  // Implement other camera commands
                    Log.i(TAG,"Video grabber activated  ");
                    sGestioneSocketComandi.invia("*6*0*4000##");//TODO: make 40  constant  and the rest of the string   variable
                    Log.i(TAG,"Camera sctivated  ");
                }
                break;
            case CONNECTED:
                sGestioneSocketComandi.invia("*6*0*4000##");
                Log.i(TAG,"Camera sctivated  ");
                break;
        }
    }
    public void close() throws IOException {
                mDataInputStream.close();
                Log.i(TAG,"DataInputStream closed  successfully   ");
                Log.i(TAG,"GestioneSocketComandi.stato   "+GestioneSocketComandi.stato);
                GestioneSocketComandi.stato=NOT_CONNECTED;
                Log.i(TAG,"GestioneSocketComandi. stato  has been set to 0   ");
            }
        }


