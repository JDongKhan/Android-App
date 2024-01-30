package com.jd.core.utils

class ClassUtils private constructor() {
    init {
        throw UnsupportedOperationException("Can not be created！")
    }

    companion object {
        /**
         * 根据完整包名获取简单名称
         *
         * @param className
         * @return
         */
        fun getSimpleName(className: String): String {
            if (StringUtils.isEmptyWithBlank(className)) {
                return ""
            }
            val index = className.lastIndexOf(".") + 1
            return className.substring(index, className.length)
        }
    }
}