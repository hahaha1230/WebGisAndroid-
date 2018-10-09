package com.example.hahaha;

import android.animation.ObjectAnimator;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private FingerprintManager fingerManager;
    private KeyguardManager keyManager;
    //指纹识别
    private FingerUtil fingerUtil;
    //指纹监听
    private FingerprintManagerCompat.AuthenticationCallback mFingerListener;
    //开始检测按钮
    private Button btnStart;
    //显示提示信息
    private TextView tvLog;
    //显示提示性动画的imageview
    private FingerImageView ivFinger;

    private Button passwordLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
    }

    private void initview() {
        btnStart=(Button)findViewById(R.id.btn_start);
        passwordLogin=(Button)findViewById(R.id.password_login);
        tvLog=(TextView)findViewById(R.id.tv_log);
        ivFinger=(FingerImageView)findViewById(R.id.iv_finger);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //初始化指纹按钮点击事件
                    initFingerBtnClick();
                }catch (Exception e) {
                    tvLog.setText("当前设备没有指纹识别模块");
                    //指纹识别失败，显示失败图标
                    ivFinger.end(false);
                }
                //显示提示性文字
                ObjectAnimator.ofFloat(tvLog,"alpha",0,0.7f).setDuration(150).start();

            }


        });
        passwordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("/");
                ComponentName cm = new ComponentName(
                        "com.android.settings",
                        "com.android.settings.ChooseLockGeneric");
                intent.setComponent(cm);
                startActivityForResult(intent, 2);
            }
        });
        fingerManager=(FingerprintManager)getSystemService(Context.FINGERPRINT_SERVICE);
        keyManager=(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mFingerListener!=null&&fingerUtil!=null) {
            fingerUtil.startFingerListener(mFingerListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fingerUtil!=null) {
            fingerUtil.stopFingerListener();
        }
    }

    private void initFingerBtnClick() {
        //检测设备是否具有指纹识别模块
        if (false==isFingerRecognition()) {
            tvLog.setText("当前设备没有指纹识别模块");
            //显示失败图标
            ivFinger.end(false);
            return;
        }


        //检测是否需要初始化
        if (mFingerListener==null||fingerUtil==null) {
            //初始化指纹识别
            initFinger();
            //指纹开启成功，显示成功图标
            ivFinger.startGif();
        }else {
            //开启指纹识别
            fingerUtil.startFingerListener(mFingerListener);
            //指纹开启成功，显示成功图标
            ivFinger.startGif();
        }
    }

    private void initFinger() {
        mFingerListener=new FingerprintManagerCompat.AuthenticationCallback() {

            /**
             * 多次指纹识别验证出现问题，进入此方法，并且，不能在短时间内调用指纹验证
             * @param errMsgId
             * @param errString
             */
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
                switch (errMsgId) {
                    case 5 :  //可以进行识别
                        tvLog.setTag(true);
                    break;
                    case 7:  //失败次数太多，禁用时间倒计时
                        tvLog.setTag(false);
                        tvLog.setText("失败次数太多，请"+errString+"秒后再试");
                    break;

                }
            }

            /**
             * message监听
             * @param helpMsgId
             * @param helpString
             */
            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
                if (tvLog.getTag()!=null&&false==(boolean)tvLog.getTag()) {
                    return;
                }

                switch (helpMsgId) {
                    case 10001:    //等待手指按下
                        tvLog.setText("请按下手指");
                        ivFinger.startGif();
                        break;
                    case 1002:    //手指按下
                        tvLog.setText("正在识别...");
                        break;
                    case 1003:    //手指抬起
                        tvLog.setText("手指抬起，请重新按下");
                        ivFinger.startGif();
                        break;
                }
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                tvLog.setText("指纹识别成功");
                ivFinger.end(true);

                Intent intent=new Intent(LoginActivity.this,DisplayMapActivity.class);
                startActivity(intent);
            }


            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                tvLog.setText("指纹识别失败");
                ivFinger.end(false);
            }
        };
        fingerUtil=new FingerUtil(this);
        fingerUtil.startFingerListener(mFingerListener);
    }

    //判断硬件是设备是否支持指纹解锁
    private boolean isFingerRecognition(){
        if (!fingerManager.isHardwareDetected()) {
            //Toast.makeText(this,"该手机不支持指纹解锁",Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }

    //判断是否设置了锁屏密码
    private boolean isSetCode() {
        if (!keyManager.isKeyguardSecure()) {
            Toast.makeText(this,"请设置锁屏密码",Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }

    //判断是否录入了指纹
    private boolean isInputFinger() {
        if (!fingerManager.hasEnrolledFingerprints()) {
            Toast.makeText(this,"没有录入指纹",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
