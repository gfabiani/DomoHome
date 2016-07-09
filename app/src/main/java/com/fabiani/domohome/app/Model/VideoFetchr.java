package com.fabiani.domohome.app.model;

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
    private GestioneSocketComandi socketCom;
    byte[] bytes1 = new byte[100000];
    byte[] bytes2; // contiene l'immagine jpeg
    DataInputStream br;

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        socketCom = new GestioneSocketComandi();
        if (GestioneSocketComandi.stato == 0) { //non sono ancora connesso
            if (socketCom.connect(Dashboard.sIp, Dashboard.PORT, Dashboard.sPasswordOpen)) {
                socketCom.invia("*7*9**##");
            }

        } else if (GestioneSocketComandi.stato == 3) {
            socketCom.invia("*7*9**##");
        } else
            Log.i(TAG, "Connection failed");

        if (GestioneSocketComandi.stato == 0) { //non sono ancora connesso
            if (socketCom.connect(Dashboard.sIp, Dashboard.PORT, Dashboard.sPasswordOpen)) {
                socketCom.invia("*6*0*4000##");
            }
        } else if (GestioneSocketComandi.stato == 3) {
            socketCom.invia("*6*0*4000##");
        }
        URL myUrl = new URL(urlSpec);
        SSLSetup.overrideTrustManager();
        HttpsURLConnection urlc = (HttpsURLConnection) myUrl.openConnection();
        urlc.setRequestMethod("GET");
        urlc.setDoOutput(true);
        urlc.setUseCaches(false);
        urlc.connect();

        //bytes0 = new byte[100000];
        bytes1 = new byte[100000];

        br = new DataInputStream(urlc.getInputStream());

        int length = 0;
        int temp;

        while(true){
            try{
                temp = br.read();
                if(temp == -1) break;
                bytes1[length] = (byte)temp;
                length++;
            } catch (IOException e1) {

                Log.i(TAG,"IOException reading datastream ");
                e1.printStackTrace();

            }
        }

        bytes2 = new byte[length];

        for(int z = 0; z < length; z++){
            bytes2[z] = (byte)bytes1[z];
        }
        br.close();
        return bytes2;
    }
}
