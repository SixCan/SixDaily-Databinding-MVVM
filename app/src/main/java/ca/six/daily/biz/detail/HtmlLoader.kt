package ca.six.daily.biz.detail

import android.text.TextUtils
import android.webkit.JavascriptInterface

/**
 * @author hellenxu
 * @date 2017-07-19
 * Copyright 2017 Six. All rights reserved.
 */

class HtmlLoader(val detailBody: String, val cssVer: String) {
    val LOCAL_CSS_VERSION = "4b3e3"

    @JavascriptInterface
    fun isTheSameCssFile() : Boolean {
        return TextUtils.equals(LOCAL_CSS_VERSION, cssVer)
    }

    @JavascriptInterface
    fun getBody() : String {
        return detailBody
    }

    @JavascriptInterface
    fun getCssLink() : String {
        println("get css...")
        return "http://news-at.zhihu.com/css/news_qa.auto.css?v=$cssVer"
    }
}