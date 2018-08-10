package ca.six.daily.biz.home.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.support.v7.widget.RecyclerView
import ca.six.daily.biz.home.DailyListAdapter
import ca.six.daily.core.network.HttpEngine
import ca.six.daily.data.DailyListResponse
import ca.six.daily.data.Story
import ca.six.daily.utils.readCachedLatestNews
import ca.six.daily.utils.writeToCacheFile
import ca.six.daily.view.RvViewHolder
import ca.six.daily.view.ViewType
import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.*

/**
 * @CopyRight six.ca
 * Created by Heavens on 2018-06-27.
 */
class DailyListViewModel : ViewModel() {
    lateinit var listData: DailyListResponse
    val items = ObservableArrayList<ViewType<out Any>>()
    val isLoadingData = ObservableBoolean(false)
    val ids = ObservableArrayList<Long>()
    val cacheFileName = "news_latest.json"
    val formatterOut = SimpleDateFormat("MMM, dd yyyy", Locale.CANADA)
    val formatterIn = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

    fun requestData(adapter: RecyclerView.Adapter<RvViewHolder>) {
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
                    val date = formatterIn.parse(resp.date)
                    items.add(ListTitleViewModel(formatterOut.format(date)))
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
                .subscribe {
                    (adapter as DailyListAdapter).replaceData(items)
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