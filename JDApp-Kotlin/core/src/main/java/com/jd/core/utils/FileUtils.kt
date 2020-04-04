package com.jd.core.utils

import android.annotation.SuppressLint
import android.net.Uri

import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * 文件和文件夹操作类
 */

class FileUtils private constructor() {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    object MemoryConstants {

        /**
         * Byte与Byte的倍数
         */
        val BYTE = 1
        /**
         * KB与Byte的倍数
         */
        val KB = 1024
        /**
         * MB与Byte的倍数
         */
        val MB = 1048576
        /**
         * GB与Byte的倍数
         */
        val GB = 1073741824

    }

    companion object {

        /**
         * 通过文件路径转换成file
         *
         * @param filePath
         * @return
         */
        fun getFileByPath(filePath: String): File? {
            return if (isSpace(filePath)) null else File(filePath)
        }

        /**
         * 判断文件是否存在
         *
         * @param filePath
         * @return
         */
        fun isFileExists(filePath: String): Boolean {
            return isFileExists(getFileByPath(filePath))
        }

        /**
         * 判断文件是否存在
         *
         * @param file
         * @return
         */
        fun isFileExists(file: File?): Boolean {
            return file != null && file.exists()
        }


        /**
         * 判断是否是文件
         *
         * @param filePath 文件路径
         * @return `true`: 是<br></br>`false`: 否
         */
        fun isFile(filePath: String): Boolean {
            return isFile(getFileByPath(filePath))
        }

        /**
         * 判断是否是文件
         *
         * @param file 文件
         * @return `true`: 是<br></br>`false`: 否
         */
        fun isFile(file: File?): Boolean {
            return isFileExists(file) && file!!.isFile
        }

        /**
         * 判断是否是目录
         *
         * @param dirPath 目录路径
         * @return `true`: 是<br></br>`false`: 否
         */
        fun isDir(dirPath: String): Boolean {
            return isDir(getFileByPath(dirPath))
        }

        /**
         * 判断是否是目录
         *
         * @param file 文件
         * @return `true`: 是<br></br>`false`: 否
         */
        fun isDir(file: File?): Boolean {
            return isFileExists(file) && file!!.isDirectory
        }

        /**
         * 重命名文件
         *
         * @param filePath
         * @param newName
         * @return
         */
        fun rename(filePath: String, newName: String): Boolean {
            return rename(getFileByPath(filePath), newName)
        }

        /**
         * 重命名文件
         *
         * @param file
         * @param newName
         * @return
         */
        fun rename(file: File?, newName: String): Boolean {
            if (file == null) return false// 文件为空返回false
            if (!file.exists()) return false// 文件不存在返回false
            if (isSpace(newName)) return false// 新的文件名为空返回false
            if (newName == file.name) return true// 如果文件名没有改变返回true
            val newFile = File(file.parent + File.separator + newName)
            // 如果重命名的文件已存在返回false
            return !newFile.exists() && file.renameTo(newFile)
        }

        /**
         * 判断目录是否存在，不存在则判断是否创建成功
         *
         * @param dirPath 目录路径
         * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
         */
        fun createOrExistsDir(dirPath: String): Boolean {
            return createOrExistsDir(getFileByPath(dirPath))
        }

        /**
         * 判断目录是否存在，不存在则判断是否创建成功
         *
         * @param file 文件
         * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
         */
        fun createOrExistsDir(file: File?): Boolean {
            // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
            return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
        }

        /**
         * 获取文件路径
         * @param fileName
         * @return
         */
        fun getImageUri(path: String, fileName: String): Uri {
            val avatar = File(path)
            if (!avatar.exists()) {
                avatar.mkdirs()
            }
            val file = File(avatar, fileName)
            AppLog.e("文件路径$file")
            // 檢查文件是否存在
            if (file.exists()) {
                file.delete()
            }
            try {
                file.createNewFile()
                //writable -- 如果为true，允许写访问权限;如果为false，写访问权限是不允许的。
                file.setWritable(java.lang.Boolean.TRUE)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return Uri.fromFile(file)// 返回格式化后的uri
        }

        /**
         * 判断文件是否存在，不存在则判断是否创建成功
         *
         * @param filePath 文件路径
         * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
         */
        fun createOrExistsFile(filePath: String): Boolean {
            return createOrExistsFile(getFileByPath(filePath))
        }

        /**
         * 判断文件是否存在，不存在则判断是否创建成功
         *
         * @param file 文件
         * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
         */
        fun createOrExistsFile(file: File?): Boolean {
            if (file == null) return false
            // 如果存在，是文件则返回true，是目录则返回false
            if (file.exists()) return file.isFile
            if (!createOrExistsDir(file.parentFile)) return false
            try {
                return file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

        }

        /**
         * 判断文件是否存在，存在则在创建之前删除
         *
         * @param filePath 文件路径
         * @return `true`: 创建成功<br></br>`false`: 创建失败
         */
        fun createFileByDeleteOldFile(filePath: String): Boolean {
            return createFileByDeleteOldFile(getFileByPath(filePath))
        }

        /**
         * 判断文件是否存在，存在则在创建之前删除
         *
         * @param file 文件
         * @return `true`: 创建成功<br></br>`false`: 创建失败
         */
        fun createFileByDeleteOldFile(file: File?): Boolean {
            if (file == null) return false
            // 文件存在并且删除失败返回false
            if (file.exists() && file.isFile && !file.delete()) return false
            // 创建目录失败返回false
            if (!createOrExistsDir(file.parentFile)) return false
            try {
                return file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

        }

        /**
         * 获取文件夹大小
         *
         * @param dir
         * @return
         */
        fun getDirSize(dir: File?): Long {
            if (dir == null) return 0
            if (!dir.isDirectory) return 0
            var dirSize: Long = 0
            val files = dir.listFiles()
            for (file in files) {
                if (file.isFile) {
                    dirSize += file.length()
                } else if (file.isDirectory) {
                    dirSize += file.length()
                    dirSize += getDirSize(file)
                }
            }
            return dirSize
        }

        /**
         * 删除文件
         *
         * @param srcFilePath
         * @return
         */
        fun deleteFile(srcFilePath: String): Boolean {
            return deleteFile(getFileByPath(srcFilePath))
        }

        /**
         * 删除文件
         *
         * @param file
         * @return
         */
        fun deleteFile(file: File?): Boolean {
            return file != null && (!file.exists() || file.isFile && file.delete())
        }

        /**
         * 删除文件夹
         *
         * @param dirPath
         * @return
         */
        fun deleteDir(dirPath: String): Boolean {
            return deleteDir(getFileByPath(dirPath))
        }

        /**
         * 删除文件夹
         *
         * @param dir
         */
        fun deleteDir(dir: File?): Boolean {
            if (dir == null) return false
            if (!dir.exists()) return false
            if (!dir.isDirectory) return false
            val files = dir.listFiles()
            if (files != null && files.size != 0) {
                for (file in files) {
                    if (file.isFile) {
                        if (!deleteFile(file)) return false
                    } else if (file.isDirectory) {
                        if (!deleteDir(file)) return false
                    }
                }
            }
            return dir.delete()
        }

        /**
         * 获取文件大小
         *
         * @param filePath 文件路径
         * @return 文件大小
         */
        fun getFileSize(filePath: String): String {
            return getFileSize(getFileByPath(filePath))
        }

        /**
         * 获取文件长度
         *
         * @param filePath 文件路径
         * @return 文件长度
         */
        fun getFileLength(filePath: String): Long {
            return getFileLength(getFileByPath(filePath))
        }

        /**
         * 获取文件长度
         *
         * @param file 文件
         * @return 文件长度
         */
        fun getFileLength(file: File?): Long {
            return if (!isFile(file)) -1 else file!!.length()
        }

        /**
         * 获取文件大小
         *
         * @param file 文件
         * @return 文件大小
         */
        fun getFileSize(file: File?): String {
            val len = getFileLength(file)
            return if (len as Int  == -1) "" else byte2FitMemorySize(len)
        }

        /**
         * 获取文件的MD5校验码
         *
         * @param filePath 文件路径
         * @return 文件的MD5校验码
         */
        fun getFileMD5(filePath: String): ByteArray? {
            val file = if (isSpace(filePath)) null else File(filePath)
            return getFileMD5(file)
        }

        /**
         * 获取文件的MD5校验码
         *
         * @param file 文件
         * @return 文件的MD5校验码
         */
        fun getFileMD5(file: File?): ByteArray? {
            if (file == null) return null
            var dis: DigestInputStream? = null
            try {
                val fis = FileInputStream(file)
                var md = MessageDigest.getInstance("MD5")
                dis = DigestInputStream(fis, md)
                val buffer = ByteArray(1024 * 256)
                while (true) {
                    if (dis.read(buffer) <= 0) break
                }
                md = dis.messageDigest
                return md.digest()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                FileUtils.closeIO(dis!!)
            }
            return null
        }

        /**
         * 获取全路径中的最长目录
         *
         * @param file 文件
         * @return filePath最长目录
         */
        fun getDirName(file: File?): String? {
            return if (file == null) null else getDirName(file.path)
        }

        /**
         * 获取全路径中的最长目录
         *
         * @param filePath 文件路径
         * @return filePath最长目录
         */
        fun getDirName(filePath: String): String? {
            if (isSpace(filePath)) return filePath
            val lastSep = filePath.lastIndexOf(File.separator)
            return if (lastSep == -1) "" else filePath.substring(0, lastSep + 1)
        }

        /**
         * 获取全路径中的文件名
         *
         * @param file 文件
         * @return 文件名
         */
        fun getFileName(file: File?): String? {
            return if (file == null) null else getFileName(file.path)
        }

        /**
         * 获取全路径中的文件名
         *
         * @param filePath 文件路径
         * @return 文件名
         */
        fun getFileName(filePath: String): String? {
            if (isSpace(filePath)) return filePath
            val lastSep = filePath.lastIndexOf(File.separator)
            return if (lastSep == -1) filePath else filePath.substring(lastSep + 1)
        }

        /**
         * 获取全路径中的不带拓展名的文件名
         *
         * @param file 文件
         * @return 不带拓展名的文件名
         */
        fun getFileNameNoExtension(file: File?): String? {
            return if (file == null) null else getFileNameNoExtension(file.path)
        }

        /**
         * 获取全路径中的不带拓展名的文件名
         *
         * @param filePath 文件路径
         * @return 不带拓展名的文件名
         */
        fun getFileNameNoExtension(filePath: String): String? {
            if (isSpace(filePath)) return filePath
            val lastPoi = filePath.lastIndexOf('.')
            val lastSep = filePath.lastIndexOf(File.separator)
            if (lastSep == -1) {
                return if (lastPoi == -1) filePath else filePath.substring(0, lastPoi)
            }
            return if (lastPoi == -1 || lastSep > lastPoi) {
                filePath.substring(lastSep + 1)
            } else filePath.substring(lastSep + 1, lastPoi)
        }

        /**
         * 获取全路径中的文件拓展名
         *
         * @param file 文件
         * @return 文件拓展名
         */
        fun getFileExtension(file: File?): String? {
            return if (file == null) null else getFileExtension(file.path)
        }

        /**
         * 获取全路径中的文件拓展名
         *
         * @param filePath 文件路径
         * @return 文件拓展名
         */
        fun getFileExtension(filePath: String): String? {
            if (isSpace(filePath)) return filePath
            val lastPoi = filePath.lastIndexOf('.')
            val lastSep = filePath.lastIndexOf(File.separator)
            return if (lastPoi == -1 || lastSep >= lastPoi) "" else filePath.substring(lastPoi + 1)
        }

        /**
         * 判断是否是空或者空白字符
         *
         * @param s
         * @return
         */
        private fun isSpace(s: String?): Boolean {
            if (s == null) return true
            var i = 0
            val len = s.length
            while (i < len) {
                if (!Character.isWhitespace(s[i])) {
                    return false
                }
                ++i
            }
            return true
        }

        /**
         * 字节数转合适内存大小
         *
         * 保留3位小数
         *
         * @param byteNum 字节数
         * @return 合适内存大小
         */
        @SuppressLint("DefaultLocale")
        private fun byte2FitMemorySize(byteNum: Long): String {
            return if (byteNum < 0) {
                "shouldn't be less than zero!"
            } else if (byteNum < MemoryConstants.KB) {
                String.format("%.3fB", byteNum.toDouble() + 0.0005)
            } else if (byteNum < MemoryConstants.MB) {
                String.format("%.3fKB", byteNum.toDouble() / MemoryConstants.KB + 0.0005)
            } else if (byteNum < MemoryConstants.GB) {
                String.format("%.3fMB", byteNum.toDouble() / MemoryConstants.MB + 0.0005)
            } else {
                String.format("%.3fGB", byteNum.toDouble() / MemoryConstants.GB + 0.0005)
            }
        }

        private val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

        /**
         * 关闭 IO
         * @param closeables
         */
        fun closeIO(vararg closeables: Closeable) {
            if (closeables != null && closeables.size != 0) {
                val var2 = closeables.size

                for (var3 in 0 until var2) {
                    val closeable = closeables[var3]
                    if (closeable != null) {
                        try {
                            closeable.close()
                        } catch (var6: IOException) {
                            var6.printStackTrace()
                        }

                    }
                }

            }
        }
    }
}
