package com.fabiani.domohome.app.model;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.*;

/**
 * Created by Giovanni on 12/06/2016.
 */
public class HostnameVerifierSetup implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
            return true;
    }
}