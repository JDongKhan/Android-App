package com.jd.core.utils.html

import android.text.Editable
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.StrikethroughSpan
import com.jd.core.utils.SizeUtils
import org.xml.sax.Attributes
import java.util.Stack

class CustomerTagHandler : HtmlParser.TagHandler {
    /**
     * html 标签的开始下标
     */
    private var startIndex: Stack<Int>? = null

    /**
     * html的标签的属性值 value，如:<size value='16'></size>
     * 注：value的值不能带有单位,默认就是sp
     */
    private var propertyValue: Stack<String>? = null
    override fun handleTag(
        opening: Boolean,
        tag: String?,
        output: Editable?,
        attributes: Attributes?
    ): Boolean {
        if (opening) {
            handlerStartTAG(tag, output, attributes)
        } else {
            handlerEndTAG(tag, output, attributes)
        }
        return handlerBYDefault(tag)
    }

    private fun handlerStartTAG(tag: String?, output: Editable?, attributes: Attributes?) {
        if (tag.equals("font", ignoreCase = true)) {
            handlerStartFONT(output, attributes)
        } else if (tag.equals("del", ignoreCase = true)) {
            handlerStartDEL(output)
        }
    }

    private fun handlerEndTAG(tag: String?, output: Editable?, attributes: Attributes?) {
        if (tag.equals("font", ignoreCase = true)) {
            handlerEndFONT(output)
        } else if (tag.equals("del", ignoreCase = true)) {
            handlerEndDEL(output)
        }
    }

    private fun handlerStartFONT(output: Editable?, attributes: Attributes?) {
        if (startIndex == null) {
            startIndex = Stack()
        }
        startIndex!!.push(output?.length)
        if (propertyValue == null) {
            propertyValue = Stack()
        }
        propertyValue!!.push(HtmlParser.getValue(attributes!!, "size"))
    }

    private fun handlerEndFONT(output: Editable?) {
        if (propertyValue != null && !propertyValue!!.isEmpty()) {
            try {
                val value = propertyValue!!.pop().toInt()
                output?.setSpan(
                    AbsoluteSizeSpan(SizeUtils.sp2px(value.toFloat())),
                    startIndex!!.pop(),
                    output.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handlerStartDEL(output: Editable?) {
        if (startIndex == null) {
            startIndex = Stack()
        }
        startIndex!!.push(output!!.length)
    }

    private fun handlerEndDEL(output: Editable?) {
        output?.setSpan(
            StrikethroughSpan(),
            startIndex!!.pop(),
            output.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    /**
     * 返回true表示不交给系统后续处理
     * false表示交给系统后续处理
     *
     * @param tag
     * @return
     */
    private fun handlerBYDefault(tag: String?): Boolean {
        return tag?.equals("del", ignoreCase = true) ?: false
    }

}