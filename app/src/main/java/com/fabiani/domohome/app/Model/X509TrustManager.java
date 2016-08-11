package com.fabiani.domohome.app.model;

import java.security.cert.X509Certificate;


/**
 * Created by Giovanni on 11/06/2016.
 */
public class X509TrustManager implements javax.net.ssl.X509TrustManager {
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
}
