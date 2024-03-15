package com.jd.app;

import android.animation.Animator;
import android.content.Intent;
import android.widget.ImageView;

import com.jd.core.base.BaseActivity;


/**
 * @author sn
 */
public class SplashActivity extends BaseActivity implements Animator.AnimatorListener {

    ImageView ivSplash;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        ivSplash = findViewById(R.id.image_splash);
        // 渐变动画
        ivSplash.setAlpha(0.3f);
        ivSplash.animate()
                .alpha(1.0f)
                .setDuration(2000)
                .setListener(this)
                .start();
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override public void onAnimationStart(Animator animation) {
    }

    @Override public void onAnimationCancel(Animator animation) {
    }

    @Override public void onAnimationRepeat(Animator animation) {
    }

    /**
     * 隐藏导航
     */
    protected boolean preferredNavigationBarHidden() {
        return true;
    }

}
