package ca.six.daily.biz.home.viewmodel

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import ca.six.daily.biz.home.DailyListAdapter
import ca.six.daily.view.ViewType

/**
 * @CopyRight six.ca
 * Created by Heavens on 2018-08-07.
 */
object ItemListBindings {

    @BindingAdapter("app:items")
    @JvmStatic fun setItems(rv: RecyclerView, items: List<ViewType<out Any>>) {
        with(rv.adapter as DailyListAdapter) {
            replaceData(items)
        }
    }
}