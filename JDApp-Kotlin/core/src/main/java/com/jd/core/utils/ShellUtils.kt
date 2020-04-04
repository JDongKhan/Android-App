package com.jd.core.utils

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * Shell 相关
 */

object ShellUtils {

    private val LINE_SEP = System.getProperty("line.separator")

    /**
     * The result of command.
     */
    class CommandResult(var result: Int, var successMsg: String?, var errorMsg: String?)
}
