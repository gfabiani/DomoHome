package com.fabiani.domohome.app;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.fabiani.domohome.app.model.Dashboard;
import com.fabiani.domohome.app.model.VideoFetchr;

import org.junit.Test;

import java.io.IOException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Test()
     public void videoFetchrTest() {
        VideoFetchr videoFetchr = new VideoFetchr();
        Dashboard.sIp = "10.0.0.36";
        Dashboard.sPasswordOpen = 22071975;
        byte[] b=null;
        try {
            b = videoFetchr.getUrlBytes("https://"+Dashboard.sIp+"/tele.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(null, b);

    }
}