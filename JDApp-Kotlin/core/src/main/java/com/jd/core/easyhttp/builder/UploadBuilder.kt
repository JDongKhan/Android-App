package com.jd.core.easyhttp.builder

import android.os.Handler
import android.util.Pair
import com.jd.core.easyhttp.request.CountingRequestBody
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.net.URLConnection

/**
 * @author jd
 */
class UploadBuilder(okHttpClient: OkHttpClient, delivery: Handler) :
    HttpBuilder(okHttpClient, delivery) {
    private var files: Array<out Pair<String, File>?>? = null
    override fun createBuilder(): Request.Builder {
        val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (params != null && !params!!.isEmpty()) {
            for (key in params!!.keys) {
                bodyBuilder.addFormDataPart(key, params!![key])
            }
        }
        addFiles(bodyBuilder)
        val requestBody: RequestBody = bodyBuilder.build()
        //这是正常的请求头
        val mBuilder = Request.Builder()
        mBuilder.url(url)
        //进项这部操作才能监听进度，来自鸿洋okHttpUtils
        val requestBodyProgress: RequestBody =
            CountingRequestBody(requestBody) { bytesWritten: Long, contentLength: Long ->
                mDelivery.post {
                    callback!!.inProgress(
                        contentLength,
                        bytesWritten * 1.0f / contentLength
                    )
                }
            }
        mBuilder.post(requestBodyProgress)
        return mBuilder
    }

    fun files(vararg files: Pair<String, File>?): UploadBuilder {
        this.files = files
        return this
    }

    override fun url(url: String?): UploadBuilder {
        this.url = url
        return this
    }

    override fun tag(tag: String?): UploadBuilder {
        this.tag = tag
        return this
    }

    override fun params(params: Map<String, String?>?): UploadBuilder {
        this.params = params
        return this
    }

    fun addFiles(mBuilder: MultipartBody.Builder) {
        if (files != null) {
            var fileBody: RequestBody? = null
            for (i in files!!.indices) {
                if (files!![i] != null) {
                    val filePair = files!![i]
                    val fileKeyName = filePair!!.first
                    val file = filePair.second
                    val fileName = file.name
                    fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file)
                    mBuilder.addPart(
                        Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"$fileKeyName\"; filename=\"$fileName\""
                        ),
                        fileBody
                    )
                }
            }
        }
    }

    private fun guessMimeType(path: String): String {
        val fileNameMap = URLConnection.getFileNameMap()
        var contentTypeFor = fileNameMap.getContentTypeFor(path)
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream"
        }
        return contentTypeFor
    }
}