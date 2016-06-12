package com.fabiani.domohome.app.model;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Giovanni on 12/06/2016.
 */
public class SSLSetup {
   /* public static void initializeForSSL() {
        Security.addProvider(new );
        System.setProperty(
                "java.protocol.handler.pkgs",
                "com.sun.net.ssl.internal.www.protocol");
    }*/
    public static void overrideTrustManager() {
//use our own trust manager so we can always trust
//the URL entered in the configuration.
        X509TrustManager tm = new X509TrustManager();
        KeyManager[] km = null;
        X509TrustManager[] tma = new X509TrustManager[] { tm };
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
//SSLv3");
            sslContext.init(km, tma, new java.security.SecureRandom());
            SSLSocketFactory sf1 = sslContext.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(sf1);

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.out);
        } catch (KeyManagementException e) {
            e.printStackTrace(System.out);
        }
    }
    public static void setDebug() {
        System.setProperty("javax.net.debug",
                "ssl,handshake,data,trustmanager");
    }
}