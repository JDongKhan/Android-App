package com.jd.login.interceptor

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.jd.config.RoutePath
import com.jd.core.log.LogUtils

/**
 * 声明拦截器(拦截跳转过程，面向切面编程)
 * 比较经典的应用就是在跳转过程中处理登陆事件，这样就不需要在目标页重复做登陆检查
 * 拦截器会在跳转之间执行，多个拦截器会按优先级顺序依次执行
 */
@Interceptor(name = "login", priority = 1)
class LoginInterceptor : IInterceptor {

    companion object {
        const val TAG = "LoginInterceptor"
    }

    override fun init(context: Context?) {
        LogUtils.e(TAG,"路由登录拦截器初始化成功")
    }

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        LogUtils.e(TAG,"进拦截器了....")
        val path = postcard.path
        val isLogin = true
        if (isLogin) {
            LogUtils.e(TAG,"已经登录了", )
            callback.onContinue(postcard)
        } else {
            when (path) {
                RoutePath.List.TEST -> {
                    LogUtils.e(TAG,"未登录",)
                    callback.onInterrupt(null)
                }

                else -> {
                    LogUtils.e(TAG,"不需要登录")
                    callback.onContinue(postcard)
                }
            }
        }
    }
}