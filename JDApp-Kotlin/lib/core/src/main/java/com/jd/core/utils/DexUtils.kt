package com.jd.core.utils

import android.content.Context
import android.widget.Toast
import dalvik.system.DexClassLoader
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * 加载外部apk
 */
class DexUtils {
    fun extractAssets(context: Context, sourceName: String?) {
        val am = context.assets
        var `is`: InputStream? = null
        var fos: FileOutputStream? = null
        try {
            `is` = am.open(sourceName!!)
            val extractFile = context.getFileStreamPath(sourceName)
            fos = FileOutputStream(extractFile)
            val buffer = ByteArray(1024)
            var count = 0
            while (`is`.read(buffer).also { count = it } > 0) {
                fos.write(buffer, 0, count)
            }
            fos.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            closeSilently(`is`)
            closeSilently(fos)
        }
    }

    private fun closeSilently(closeable: Closeable?) {
        if (closeable == null) {
            return
        }
        try {
            closeable.close()
        } catch (e: Throwable) {
        }
    }

    private  fun loadDex(context: Context,apkName:String){
        var mDexPath: String? = null //apk文件地址
        var mFileRelease: File? = null //释放目录
        var mClassLoader: DexClassLoader? = null

        val extractFile: File = context.getFileStreamPath(apkName)
        mDexPath = extractFile.path
        mFileRelease = context.getDir("dex", 0) //0 表示Context.MODE_PRIVATE
        mClassLoader = DexClassLoader(
            mDexPath,
            mFileRelease!!.absolutePath, null, context.classLoader
        )

        //调用对应的类
        val mLoadClassBean: Class<*>
        try {
            mLoadClassBean =
                mClassLoader.loadClass("com.plugin.administrator.myapplication.UserInfo")
            val beanObject = mLoadClassBean.newInstance()
            Toast.makeText(context.applicationContext, beanObject.toString(), Toast.LENGTH_LONG)
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}