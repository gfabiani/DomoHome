package com.fabiani.domohome.app;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;
import com.fabiani.domohome.app.controller.SettingsFragment;
import com.fabiani.domohome.app.model.Dashboard;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertThat;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        //validateIpTest();
        //startMonitoringTest();
    }

   /* @Test
    public static void validateIpTest() {
        String mAddressInput = "pippo";
        assertTrue(SettingsFragment.validateIp(mAddressInput));
    }

    /*@Test
    public static void startMonitoringTest() {
        SettingsFragment.sAddressInput = "10.0.0.36";//Arrange


        try {
            Dashboard.startMonitoring();//Act
            fail("Expected an IOException to be thrown");
        } catch (Exception anException) {
            assertEquals(anException.getMessage(), D);//assert
        }
    }*/
}