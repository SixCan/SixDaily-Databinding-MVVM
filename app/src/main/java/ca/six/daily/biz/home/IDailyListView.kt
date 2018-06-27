package ca.six.daily.biz.home

import ca.six.daily.view.ViewType

interface IDailyListView {
    fun refresh(data: MutableList<ViewType<out Any>>)
    fun jumpToDetilsPage(thisStoryID: Long, allIDs: LongArray)
    fun  onError(errorText: String)
}

