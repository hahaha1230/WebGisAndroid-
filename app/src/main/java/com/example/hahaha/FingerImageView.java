package com.example.hahaha;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 佳佳 on 9/22/2018.
 */

public class FingerImageView extends ImageView {
    public FingerImageView(Context context) {
        super(context);
    }
    public FingerImageView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    public FingerImageView(Context context,AttributeSet attrs,int defStyleAttr) {
        super(context,attrs,defStyleAttr);
    }


    public void startGif() {
        //初始化
        setAlpha(0);
        setScaleX(1f);
        setScaleY(1f);

        //设置桢动画
        setBackgroundResource(R.drawable.finger_gif);
        //桢动画开始
        ((AnimationDrawable)getBackground()).start();

        //开启渐变动画
        ObjectAnimator.ofFloat(this,"alpha",0,1).setDuration(200).start();

    }

    public void end(final boolean isCuccess) {
        //初始化
        setAlpha(0);
        setScaleY(1.54f);
        setScaleX(1.5f);

        //设置帧动画
        setBackgroundResource(isCuccess?R.drawable.finger_suceed:R.drawable.finger_failed);
        //帧动画开始
        ((AnimationDrawable)getBackground()).start();

        //开启渐变动画
        ObjectAnimator.ofFloat(this,"alpha",0,1).setDuration(200).start();
    }
}
