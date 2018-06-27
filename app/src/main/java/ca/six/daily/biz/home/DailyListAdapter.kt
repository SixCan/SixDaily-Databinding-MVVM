package ca.six.daily.biz.home

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ca.six.daily.view.RvViewHolder
import ca.six.daily.view.ViewType

class DailyListAdapter(var data: List<ViewType<out Any>>) : RecyclerView.Adapter<RvViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return data[position].getViewType()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
        return RvViewHolder.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {
        data[position].bind(holder)
    }

}