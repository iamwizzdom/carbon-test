package com.carbon.test.common;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class ValidatorTest {

    @Test
    public void testNetworkIsConnected() {
        boolean isConnect = Validator.isNetworkConnected(ApplicationProvider.getApplicationContext());
        assertThat(isConnect).isTrue();
    }

    @Test
    public void testNetworkIsNotConnected() {
        boolean isConnect = Validator.isNetworkConnected(ApplicationProvider.getApplicationContext());
        assertThat(isConnect).isFalse();
    }
}