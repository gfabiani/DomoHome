package com.fabiani.domohome;

import android.graphics.Bitmap;
import android.support.test.runner.AndroidJUnit4;

import com.fabiani.domohome.model.Dashboard;
import com.fabiani.domohome.model.VideoFetchr;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Test
    public void getUrlBitmapEqualToNull() throws IOException {
        VideoFetchr videoFetchr = new VideoFetchr();
        Dashboard.sIp = "10.0.0.36";
        Dashboard.sPasswordOpen = 22071975;
        Bitmap b = videoFetchr.getUrlBitmap("https://" + Dashboard.sIp + "/telecamera.php");
        assertThat("Not null value is good! ", b, equalTo(null));
    }

}
