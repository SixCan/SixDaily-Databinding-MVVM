package ca.six.daily.biz.detail

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.databinding.ObservableLong
import ca.six.daily.core.network.HttpEngine
import ca.six.daily.utils.readCachedDetails
import ca.six.daily.utils.writeToCacheFile
import io.reactivex.Observable
import org.json.JSONObject

/**
 * @CopyRight six.ca
 * Created by Heavens on 2018-08-13.
 */
class DailyDetailViewModel(var id: ObservableLong) : ViewModel() {
    var body = ObservableField<String>()
    var imageUrl = ObservableField<String>()
    var title = ObservableField<String>()
    var cssVer = ObservableField<String>()
    var isCached: Boolean = false

    companion object {
        const val REDUNDANT_PART: String = "<div class=\"img-place-holder\"></div>\n\n\n\n"
    }

    fun getDetails() {
        val cachedObservable = readCachedDetails(id.get())
                .map {
                    isCached = true
                    it
                }

        val networkObservable = HttpEngine.request("news/${id.get()}")
                .map { jsonResp ->
                    writeToCacheFile(jsonResp, "news_${id.get()}.json")
                    jsonResp
                }

        Observable.concat(cachedObservable, networkObservable)
                .map {
                    parseResponse(it)
                }
    }

    private fun parseResponse(response: String) {
        val json = JSONObject(response)

        body.set(json.optString("body").replace(REDUNDANT_PART, ""))
        imageUrl.set(json.optString("image"))
        title.set(json.optString("title"))

        val css = json.optJSONArray("css")[0] as String
        val startIndex = css.indexOf("=")
        cssVer.set(if (css.isNotEmpty() && startIndex > -1) css.substring(startIndex + 1, css.length) else "")

    }

}