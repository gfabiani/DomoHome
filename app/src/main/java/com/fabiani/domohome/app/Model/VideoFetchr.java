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
 */
public class VideoFetchr {
    private static final String TAG = "VideoFetchr";
    private static GestioneSocketComandi
            mGestioneSocketComandi=new GestioneSocketComandi();
    DataInputStream dataInputStream;

    public Bitmap getUrlBitmap(String urlSpec) throws IOException {
        URL myUrl = new URL(urlSpec);
        SSLSetup.overrideTrustManager();
        HttpsURLConnection urlc = (HttpsURLConnection) myUrl.openConnection();
        urlc.setRequestMethod("GET");
        urlc.setDoOutput(true);
        urlc.setUseCaches(false);
        urlc.connect();
        dataInputStream = new DataInputStream(urlc.getInputStream());//TODO: Priority high: Close the stream. Command open too
        return BitmapFactory.decodeStream(dataInputStream);//TODO: resize image
    }

    public static void sendVideoOpen() {
        switch (GestioneSocketComandi.stato){
            case 0://non sono ancora connesso
                if (mGestioneSocketComandi.connect(Dashboard.sIp, Dashboard.PORT, Dashboard.sPasswordOpen)) {
                    mGestioneSocketComandi.invia("*6*9**##");
                    Log.i(TAG,"Video grabber activated  ");
                    mGestioneSocketComandi.invia("*6*0*4000##");//TODO: make 40  constant  and the rest of the string   variable
                    Log.i(TAG,"Camera sctivated  ");
                }
                break;
            case 3://sono già connesso
                mGestioneSocketComandi.invia("*6*0*4000##");
                Log.i(TAG,"Camera sctivated  ");
                break;
        }
    }
    public void close(){//TODO: to be integrated
        if(mGestioneSocketComandi != null){
                mGestioneSocketComandi.invia("*6*9**##");
                mGestioneSocketComandi.close();
                mGestioneSocketComandi = null;
                GestioneSocketComandi.stato = 0;
               Log.i(TAG,"MON: Socket monitor closed  successfully-----\n");
            }
        }
    }

