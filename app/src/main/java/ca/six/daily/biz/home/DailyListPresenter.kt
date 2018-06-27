package ca.six.daily.biz.home

import ca.six.daily.biz.home.viewmodel.ListItemViewModel
import ca.six.daily.biz.home.viewmodel.ListTitleViewModel
import ca.six.daily.core.network.HttpEngine
import ca.six.daily.data.DailyListResponse
import ca.six.daily.data.Story
import ca.six.daily.utils.readCachedLatestNews
import ca.six.daily.utils.save2Sp
import ca.six.daily.utils.writeToCacheFile
import ca.six.daily.view.ViewType
import io.reactivex.Observable

class DailyListPresenter(val view: IDailyListView) {
    lateinit var listData: DailyListResponse
    lateinit var viewModels: MutableList<ViewType<out Any>>
    var ids: MutableList<Long> = ArrayList()
    val fileName = "news_latest.json"

    fun requestData() {
        viewModels = ArrayList()

        // readCachedLatestNews() will have the logic if the cached date is out-of-date
        val observableCache = readCachedLatestNews()
                .map { it }
        val observableHttp = HttpEngine.request("news/latest")
                .map { jsonString ->

                    writeToCacheFile(jsonString, fileName)
                    jsonString
                }

        Observable.concat(observableCache, observableHttp)
                .map { jsonString ->
                    listData = DailyListResponse(jsonString)
                    listData
                }
                .map { resp ->
                    viewModels.add(ListTitleViewModel(resp.date))
                    resp.stories.forEach { story ->
                        viewModels.add(ListItemViewModel(story))
                    }
                    listData
                }
                .map { resp ->
                    // refresh the id list after each get a new list data
                    ids.clear()
                    resp.stories.forEach { story ->
                        ids.add(story.id)
                    }
                }
                .subscribe( { view.refresh(viewModels) ; },
                        { error -> view.onError(error.message!!)},
                        { println("szw onComplete()")} )

    }

    fun jumpToDetail(position: Int) {
        val viewModel = viewModels[position]
        if (viewModel.getData() is Story) {
            val story = viewModel.getData() as Story
            val idArray: LongArray = ids.toLongArray()
            view.jumpToDetilsPage(story.id, idArray)
        }

    }
}
