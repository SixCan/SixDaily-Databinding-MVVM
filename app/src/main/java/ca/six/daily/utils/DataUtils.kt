package ca.six.daily.utils

import android.content.Context
import ca.six.daily.core.BaseApp
import ca.six.daily.data.DailyListResponse
import io.reactivex.Observable
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun writeToCacheFile(content: String, fileName: String) {
    val dir = BaseApp.app.cacheDir //=> /data/user/0/ca.six.daily/cache
    val file = File(dir, fileName) //=> /data/user/0/ca.six.daily/cache/{fileName}
    file.writeText(content)
}

fun isCacheFileExist(fileName: String): Boolean {
    val dir = BaseApp.app.cacheDir //=> /data/user/0/ca.six.daily/cache
    val file = File(dir, fileName) //=> /data/user/0/ca.six.daily/cache/{fileName}
    return file.exists()
}

// TODO struggling. Should I handle the FileNotFoundException here?
fun readCacheFile(fileName: String): String {
    val dir = BaseApp.app.cacheDir //=> /data/user/0/ca.six.daily/cache
    val file = File(dir, fileName) //=> /data/user/0/ca.six.daily/cache/{fileName}
    return file.readText()
}

fun readCacheFileRx(fileName: String): Observable<String> {
    return Observable.create<String> {
        if (isCacheFileExist(fileName)) {
            val content = readCacheFile(fileName)
            it.onNext(content)
        } else {
            it.onComplete()
        }
    }
}

/*
若cache的20170716.
手机上当前时间若是20170716， 换成cn时间：
	若是20170717，要拉新的http数据
	若仍是201716，就用cache
 */
fun readCachedLatestNews(): Observable<String> {
    val fileName = "news_latest.json"
    return Observable.create<String> {
        if (isCacheFileExist(fileName)) {
            val content = readCacheFile(fileName)
            val resp = DailyListResponse(content)

            val cachedDateValue = resp.date

            val dateFormatter = SimpleDateFormat("yyyyMMdd", Locale.CHINA)
            dateFormatter.timeZone = TimeZone.getTimeZone("GMT+8:00")
            val nowTimeCn = dateFormatter.format(Date())

            println("szw cached date = $cachedDateValue")
            println("szw nowTimeCn = $nowTimeCn")
            if (nowTimeCn.toInt() != cachedDateValue.toInt()) {
                it.onComplete() // time is not matched. Need to fetch data from server
            } else {
                it.onNext(content)
            }
        } else {
            it.onComplete()
        }
    }
}

fun readCachedDetails(id: Long): Observable<String> {
    val fileName = "news_$id.json"
    return Observable.create {
        if(isCacheFileExist(fileName)) {
            it.onNext(readCacheFile(fileName))
        } else {
            it.onComplete()
        }
    }
}

fun save2Sp(key: String, value: String) {
    val sp = BaseApp.app.getSharedPreferences("SixDaily", Context.MODE_PRIVATE)
    val editor = sp.edit()
    editor.putString(key, value)
    editor.apply()
}

fun getSpValue(key: String): String {
    val sp = BaseApp.app.getSharedPreferences("SixDaily", Context.MODE_PRIVATE)
    return sp.getString(key, "")
}
