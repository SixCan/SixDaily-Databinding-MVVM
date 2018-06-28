package ca.six.daily.biz.home.viewmodel

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import ca.six.daily.view.ViewType

/**
 * @CopyRight six.ca
 * Created by Heavens on 2018-06-27.
 */
class DailyListViewModel : BaseObservable() {
    val items = ObservableArrayList<ViewType<Any>>()
}