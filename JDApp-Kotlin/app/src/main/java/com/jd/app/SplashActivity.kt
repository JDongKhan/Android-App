package com.jd.app

import android.animation.Animator
import android.content.Intent
import android.view.View
import com.jd.app.databinding.ActivitySplashBinding

import com.jd.core.base.BaseActivity


class SplashActivity : BaseActivity(), Animator.AnimatorListener {

    lateinit var binding: ActivitySplashBinding
    override fun layoutView(): View {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {

        // 渐变动画
        binding.imageSplash.alpha = 0.3f
        binding.imageSplash.animate()
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
