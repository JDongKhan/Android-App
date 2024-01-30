package com.jd.core.easyhttp.builder

import android.os.Handler
import com.jd.core.easyhttp.okcallback.NetworkCallback
import okhttp3.CacheControl
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.nio.channels.FileChannel

/**
 * @author jd
 * 这里应该先生成临时文件
 */
class DownloadBuilder(okHttpClient: OkHttpClient, delivery: Handler) :
    HttpBuilder(okHttpClient, delivery) {
    /**
     * 断点续传的长度
     */
    private var currentLength: Long = 0

    /**
     * 文件路径(不包括文件名)
     */
    private var path: String? = null

    /**
     * 文件名
     */
    private var fileName: String? = null
    private var fileNameTemp: String? = null

    /**
     * 是否开启断点续传
     */
    private var resume = false
    override fun createBuilder(): Request.Builder {
        val mBuilder = Request.Builder()
        mBuilder.url(url!!)
        //这里只要断点上传，总会走缓存。。所以强制网络下载
        mBuilder.cacheControl(CacheControl.FORCE_NETWORK)
        if (resume) {
            val exFile = File(path, fileNameTemp)
            if (exFile.exists()) {
                currentLength = exFile.length()
                mBuilder.header("RANGE", "bytes=$currentLength-")
            }
        }
        return mBuilder
    }

    @Throws(IOException::class)
    override fun doSuccessCallback(
        call: Call,
        response: Response,
        resultMyCall: NetworkCallback<*>?,
        callback: Callback?
    ) {
        removeOnceTag()
        saveFile(call, response, resultMyCall)
    }

    override fun doFailureCallback(
        call: Call,
        e: IOException?,
        resultMyCall: NetworkCallback<*>?,
        callback: Callback?
    ) {
        removeOnceTag()
        //下载失败监听回调
        mDelivery.post {
            val errorMsg: String
            if (e is SocketException) {
            } else {
                errorMsg = if (e is ConnectException) {
                    "网络不可用,请检查网络"
                } else if (e is SocketTimeoutException) {
                    "请求超时,请稍后再试"
                } else {
                    "服务器异常,请稍后再试"
                }
                resultMyCall?.onError(errorMsg)
            }
        }
    }

    private fun saveFile(call: Call, response: Response, listener: NetworkCallback<*>?) {
        var `is`: InputStream? = null
        val buf = ByteArray(1024)
        var len = 0
        var fos: FileOutputStream? = null
        //储存下载文件的目录
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val tempFile = File(dir, fileNameTemp)
        try {
            `is` = response.body!!.byteStream()
            //总长度
            val total: Long = if (resume) {
                response.body!!.contentLength() + currentLength
            } else {
                response.body!!.contentLength()
            }
            mDelivery.post { listener?.inProgress(total, 0f) }
            fos = if (resume) {
                //这个方法是文件开始拼接
                FileOutputStream(tempFile, true)
            } else {
                //这个是不拼接，从头开始
                FileOutputStream(tempFile)
            }
            var sum: Long = if (resume) {
                currentLength
            } else {
                0
            }
            while (`is`.read(buf).also { len = it } != -1) {
                fos.write(buf, 0, len)
                sum += len.toLong()
                val progress = (sum * 1.0f / total * 100).toInt()
                //下载中更新进度条
                mDelivery.post { listener?.inProgress(total, progress.toFloat()) }
            }
            fos.flush()
            val file = File(dir, fileName)
            //从临时文件拷贝到目标地址
            copyFile(tempFile, file)
            //下载完成
            mDelivery.post { listener?.onSuccess(file as Nothing?) }
        } catch (e: Exception) {
            mDelivery.post { listener?.onError("文件下载异常") }
        } finally {
            try {
                `is`?.close()
                fos?.close()
            } catch (e: IOException) {
            }
        }
    }

    //拷贝文件
    @Throws(FileNotFoundException::class, IOException::class)
    private fun copyFile(sourceFile: File, targetFile: File) {
        var inputStream: FileChannel? = null
        var outputStream: FileChannel? = null
        try {
            inputStream = FileInputStream(sourceFile).channel
            outputStream = FileOutputStream(targetFile).channel
            outputStream?.transferFrom(inputStream, 0, inputStream.size())
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            throw e
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }

    fun path(path: String?): DownloadBuilder {
        this.path = path
        return this
    }

    fun fileName(fileName: String): DownloadBuilder {
        this.fileName = fileName
        fileNameTemp = "temp_$fileName"
        return this
    }

    fun resume(resume: Boolean): DownloadBuilder {
        this.resume = resume
        return this
    }

    override fun url(url: String?): DownloadBuilder {
        super.url(url)
        return this
    }

    override fun params(params: Map<String, String?>?): DownloadBuilder {
        super.params(params)
        return this
    }

    override fun once(once: Boolean): DownloadBuilder {
        super.once(once)
        return this
    }

    override fun tag(tag: String?): DownloadBuilder {
        super.tag(tag)
        return this
    }

    override fun headers(headers: Map<String, String?>?): DownloadBuilder {
        super.headers(headers)
        return this
    }
}