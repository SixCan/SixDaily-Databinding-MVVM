package ca.six.daily.core.network

import ca.six.daily.core.BaseApp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Okio
import java.io.File


object HttpEngine {
    val PREFIX = " https://news-at.zhihu.com/api/4/"
    val ERROR = "Error"
    val http: OkHttpClient by lazy {
        OkHttpClient.Builder()
                .addInterceptor(MockResponseInterceptor())
                .build()
    }

    // for test
    var isMock = false
    var mockJson = ""

    fun requestString(http: OkHttpClient, end: String): String {
        try {
            val req = Request.Builder()
                    .url(PREFIX + end)
                    .build()
            val resp = http.newCall(req).execute()
            return resp.body()?.string() ?: "" // 三目运算符
        } catch (exception: Exception) {
            return ERROR
        }
    }

    fun request(end: String): Observable<String> {
        val observable = Observable.create<String> { emitter ->
            val responseString = requestString(http, end)
            if (ERROR == responseString) {
                emitter.onError(NetworkError("Network or server is not available. Please try it later."))
            } else {
                emitter.onNext(responseString)
            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        return observable
    }

    fun downloadFile(remoteFile: String, localFile: String) {
        val request = Request.Builder().url(remoteFile).build()
        val parent = BaseApp.app.externalCacheDir //=>path = /storage/emulated/0/Android/data/ca.six.daily/cache
        val file = File(parent, localFile)

        Observable.create<Response> { emitter ->
                    val response = http.newCall(request).execute()
                    emitter.onNext(response)
                }
                .map { resp ->
                    println("szw 001 : file? = ${file.exists()} | fileSize = ${file.length()}")
                    if (!file.exists()) {
                        file.createNewFile()
                    }
                    val sink = Okio.buffer(Okio.sink(file))
                    sink.writeAll(resp.body()!!.source())
                    sink.close()
                    println("szw 002")
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    // TODO install apk
                }
    }

}

