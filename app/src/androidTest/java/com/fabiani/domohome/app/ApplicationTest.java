package com.fabiani.domohome.app;

import android.app.Application;
import android.graphics.Bitmap;
import android.test.ApplicationTestCase;

import com.fabiani.domohome.app.model.Dashboard;
import com.fabiani.domohome.app.model.VideoFetchr;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    // probably this test is not isolated because it depends on server status. Maybe mock is needed
    // FIRST acronym violated


    @Test
    public void getUrlBitmapEqualToNull() throws IOException {
        VideoFetchr videoFetchr = new VideoFetchr();
        Dashboard.sIp = "10.0.0.36";
        Dashboard.sPasswordOpen = 22071975;
        Bitmap b = videoFetchr.getUrlBitmap("https://" + Dashboard.sIp + "/telecamera.php");
        assertThat("Not null value is good! ", b, equalTo(null));
    }
}