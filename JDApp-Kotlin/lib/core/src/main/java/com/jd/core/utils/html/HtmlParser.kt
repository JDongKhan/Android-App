package com.jd.core.utils.html

import android.text.Editable
import android.text.Html
import android.text.Spanned
import org.xml.sax.Attributes
import org.xml.sax.ContentHandler
import org.xml.sax.Locator
import org.xml.sax.SAXException
import org.xml.sax.XMLReader
import java.util.ArrayDeque

class HtmlParser private constructor(private val handler: TagHandler) : Html.TagHandler,
    ContentHandler {
    //This approach has the advantage that it allows to disable processing of some tags while using default processing for others,
    // e.g. you can make sure that ImageSpan objects are not created:
    interface TagHandler {
        // return true here to indicate that this tag was handled and
        // should not be processed further
        fun handleTag(
            opening: Boolean,
            tag: String?,
            output: Editable?,
            attributes: Attributes?
        ): Boolean
    }

    private var wrapped: ContentHandler? = null
    private var text: Editable? = null
    private val tagStatus = ArrayDeque<Boolean>()
    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
        if (wrapped == null) {
            // record result object
            text = output

            // record current content handler
            wrapped = xmlReader.contentHandler

            // replace content handler with our own that forwards to calls to original when needed
            xmlReader.contentHandler = this

            // handle endElement() callback for <inject/> tag
            tagStatus.addLast(java.lang.Boolean.FALSE)
        }
    }

    @Throws(SAXException::class)
    override fun startElement(
        uri: String,
        localName: String,
        qName: String,
        attributes: Attributes
    ) {
        val isHandled = handler.handleTag(true, localName, text, attributes)
        tagStatus.addLast(isHandled)
        if (!isHandled) wrapped!!.startElement(uri, localName, qName, attributes)
    }

    @Throws(SAXException::class)
    override fun endElement(uri: String, localName: String, qName: String) {
        if (!tagStatus.removeLast()) wrapped!!.endElement(uri, localName, qName)
        handler.handleTag(false, localName, text, null)
    }

    override fun setDocumentLocator(locator: Locator) {
        wrapped!!.setDocumentLocator(locator)
    }

    @Throws(SAXException::class)
    override fun startDocument() {
        wrapped!!.startDocument()
    }

    @Throws(SAXException::class)
    override fun endDocument() {
        wrapped!!.endDocument()
    }

    @Throws(SAXException::class)
    override fun startPrefixMapping(prefix: String, uri: String) {
        wrapped!!.startPrefixMapping(prefix, uri)
    }

    @Throws(SAXException::class)
    override fun endPrefixMapping(prefix: String) {
        wrapped!!.endPrefixMapping(prefix)
    }

    @Throws(SAXException::class)
    override fun characters(ch: CharArray, start: Int, length: Int) {
        wrapped!!.characters(ch, start, length)
    }

    @Throws(SAXException::class)
    override fun ignorableWhitespace(ch: CharArray, start: Int, length: Int) {
        wrapped!!.ignorableWhitespace(ch, start, length)
    }

    @Throws(SAXException::class)
    override fun processingInstruction(target: String, data: String) {
        wrapped!!.processingInstruction(target, data)
    }

    @Throws(SAXException::class)
    override fun skippedEntity(name: String) {
        wrapped!!.skippedEntity(name)
    }

    companion object {
        fun buildSpannedText(html: String, handler: TagHandler): Spanned {
            // add a tag at the start that is not handled by default,
            // allowing custom tag handler to replace xmlReader contentHandler
            return Html.fromHtml("<inject/>$html", null, HtmlParser(handler))
        }

        fun getValue(attributes: Attributes, name: String): String? {
            var i = 0
            val n = attributes.length
            while (i < n) {
                if (name == attributes.getLocalName(i)) return attributes.getValue(i)
                i++
            }
            return null
        }
    }
}