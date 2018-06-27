package ca.six.daily.biz.detail

import ca.six.daily.core.network.HttpEngine
import ca.six.daily.data.DailyDetailResponse
import ca.six.daily.utils.readCachedDetails
import ca.six.daily.utils.writeToCacheFile
import io.reactivex.Observable

/**
 * @copyright six.ca
 * Created by Xiaolin on 2017-07-10.
 */
class DailyDetailPresenter(val view: IDailyDetailView) {
    var isCached: Boolean = false

    fun getDetails(id: Long) {
        val cachedObservable = readCachedDetails(id)
                .map {
                    isCached = true
                    it
                }
        val networkObservable = HttpEngine.request("news/$id")
                .map { jsonResp ->
                    writeToCacheFile(jsonResp, "news_$id.json")
                    jsonResp
                }

        Observable.concat(cachedObservable, networkObservable)
                .map {
                    DailyDetailResponse(it)
                }
                .subscribe { response ->
                    println("DailyDetailPresenter-subscribe")
                    view.updateDetails(response)
                }
    }
}