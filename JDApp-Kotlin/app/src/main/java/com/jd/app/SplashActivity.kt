package com.jd.app

import android.animation.Animator
import android.content.Intent

import com.jd.core.base.BaseActivity

import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity(), Animator.AnimatorListener {

    override fun layoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
        // 渐变动画
        image_splash!!.alpha = 0.3f
        image_splash!!.animate()
                .alpha(1.0f)
                .setDuration(2000)
                .setListener(this)
                .start()
    }

    override fun onAnimationEnd(animation: Animator) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onAnimationStart(animation: Animator) {}

    override fun onAnimationCancel(animation: Animator) {}

    override fun onAnimationRepeat(animation: Animator) {}

    /**
     * 隐藏导航
     */
    override fun preferredNavigationBarHidden(): Boolean {
        return true
    }

}
