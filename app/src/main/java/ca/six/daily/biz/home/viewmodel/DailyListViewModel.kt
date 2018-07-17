package ca.six.daily.biz.home.viewmodel

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import ca.six.daily.core.network.HttpEngine
import ca.six.daily.data.DailyListResponse
import ca.six.daily.data.Story
import ca.six.daily.utils.readCachedLatestNews
import ca.six.daily.utils.writeToCacheFile
import ca.six.daily.view.ViewType
import io.reactivex.Observable

/**
 * @CopyRight six.ca
 * Created by Heavens on 2018-06-27.
 */
class DailyListViewModel : BaseObservable() {
    lateinit var listData: DailyListResponse
    val items = ObservableArrayList<ViewType<out Any>>()
    val isLoadingData = ObservableBoolean(false)
    val ids = ObservableArrayList<Long>()
    val cacheFileName = "news_latest.json"

    fun requestData() {
        val observableCache = readCachedLatestNews()
                .map { it }
        val observableHttp = HttpEngine.request("news/latest")
                .map { jsonString ->
                    writeToCacheFile(jsonString, cacheFileName)
                    jsonString
                }

        Observable.concat(observableCache, observableHttp)
                .map { jsonString ->
                    listData = DailyListResponse(jsonString)
                    listData
                }
                .map { resp ->
                    items.add(ListTitleViewModel(resp.date))
                    resp.stories.forEach { story ->
                        items.add(ListItemViewModel(story))
                    }
                    listData
                }
                .map { resp ->
                    ids.clear()
                    resp.stories.forEach { story ->
                        ids.add(story.id)
                    }
                }
    }

    fun jumpToDetail(pos: Int) {
        val item = items[pos]
        if(item.getData() is Story) {
            val story = item.getData() as Story
            val idArray = ids.toLongArray()
            //TODO view model onclick event
        }
    }
}