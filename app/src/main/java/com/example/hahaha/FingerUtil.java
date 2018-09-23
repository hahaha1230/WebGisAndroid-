package com.example.hahaha;

import android.app.Activity;
import android.content.Context;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

/**
 * Created by 佳佳 on 9/22/2018.
 */

public class FingerUtil {
    private CancellationSignal signal;
    private FingerprintManagerCompat fingerprintManager;

    public FingerUtil(Activity activity) {
        signal=new CancellationSignal();
        fingerprintManager=FingerprintManagerCompat.from(activity);
    }
    public void startFingerListener(FingerprintManagerCompat.AuthenticationCallback callback) {
        fingerprintManager.authenticate(null,0,signal,callback,null);
    }


    public void stopFingerListener() {
        signal.cancel();
        signal=null;
    }
}
