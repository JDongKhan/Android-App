package com.jd.core.utils

import android.util.Log

import com.jd.core.BuildConfig

/**
 * 日志打印管理
 *
 */
class AppLog private constructor() {
    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated！")
    }

    companion object {

        private val TAG = "LOG_TAG"

        // 下面四个是默认tag的函数
        fun i(msg: Any) {
            if (BuildConfig.DEBUG)
                android.util.Log.i(TAG, msg.toString() + "")
        }

        fun d(msg: Any) {
            if (BuildConfig.DEBUG)
                android.util.Log.d(TAG, msg.toString() + "")
        }

        fun e(msg: Any) {
            if (BuildConfig.DEBUG)
                android.util.Log.e(TAG, msg.toString() + "")
        }

        fun v(msg: Any) {
            if (BuildConfig.DEBUG)
                android.util.Log.v(TAG, msg.toString() + "")
        }

        // 下面是传入自定义tag的函数
        fun i(tag: Any, msg: Any) {
            if (BuildConfig.DEBUG)
                android.util.Log.i(tag.toString() + "", msg.toString() + "")
        }

        fun d(tag: Any, msg: Any) {
            if (BuildConfig.DEBUG)
                android.util.Log.i(tag.toString() + "", msg.toString() + "")
        }

        fun e(tag: Any, msg: Any) {
            if (BuildConfig.DEBUG)
                android.util.Log.i(tag.toString() + "", msg.toString() + "")
        }

        fun v(tag: Any, msg: Any) {
            if (BuildConfig.DEBUG)
                android.util.Log.i(tag.toString() + "", msg.toString() + "")
        }

        fun logE(content: String) {
            var content = content
            if (BuildConfig.DEBUG) {

                val p = 2048
                val length = content.length.toLong()
                if (length < p || length == p.toLong())
                    android.util.Log.e(TAG, content)
                else {
                    while (content.length > p) {
                        val logContent = content.substring(0, p)
                        content = content.replace(logContent, "")
                        android.util.Log.e(TAG, logContent)
                    }
                    android.util.Log.e(TAG + "", content)
                }
            }
        }


        /**
         * 默认每次缩进两个空格
         */
        private val empty = "  "

        fun format(json: String): String {
            if (!BuildConfig.DEBUG) {
                return ""
            }
            try {
                var empty = 0
                val chs = json.toCharArray()
                val stringBuilder = StringBuilder()
                var i = 0
                while (i < chs.size) {
                    //若是双引号，则为字符串，下面if语句会处理该字符串
                    if (chs[i] == '\"') {
                        stringBuilder.append(chs[i])
                        i++
                        //查找字符串结束位置
                        while (i < chs.size) {
                            //如果当前字符是双引号，且前面有连续的偶数个\，说明字符串结束
                            if (chs[i] == '\"' && isDoubleSerialBackslash(chs, i - 1)) {
                                stringBuilder.append(chs[i])
                                i++
                                break
                            } else {
                                stringBuilder.append(chs[i])
                                i++
                            }

                        }
                    } else if (chs[i] == ',') {
                        stringBuilder.append(',').append('\n').append(getEmpty(empty))

                        i++
                    } else if (chs[i] == '{' || chs[i] == '[') {
                        empty++
                        stringBuilder.append(chs[i]).append('\n').append(getEmpty(empty))

                        i++
                    } else if (chs[i] == '}' || chs[i] == ']') {
                        empty--
                        stringBuilder.append('\n').append(getEmpty(empty)).append(chs[i])

                        i++
                    } else {
                        stringBuilder.append(chs[i])
                        i++
                    }


                }


                return stringBuilder.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                return json
            }

        }

        private fun isDoubleSerialBackslash(chs: CharArray, i: Int): Boolean {
            var count = 0
            for (j in i downTo -1 + 1) {
                if (chs[j] == '\\') {
                    count++
                } else {
                    return count % 2 == 0
                }
            }

            return count % 2 == 0
        }

        /**
         * 缩进
         *
         * @param count
         * @return
         */
        private fun getEmpty(count: Int): String {
            val stringBuilder = StringBuilder()
            for (i in 0 until count) {
                stringBuilder.append(empty)
            }

            return stringBuilder.toString()
        }
    }

}
