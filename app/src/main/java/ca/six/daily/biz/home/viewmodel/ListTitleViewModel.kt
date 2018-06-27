package ca.six.daily.biz.home.viewmodel

import ca.six.daily.R
import ca.six.daily.view.RvViewHolder
import ca.six.daily.view.ViewType
import java.text.SimpleDateFormat
import java.util.*

class ListTitleViewModel(val title: String) : ViewType<String> {

    override fun getViewType(): Int {
        return R.layout.item_title
    }

    override fun bind(holder: RvViewHolder) {
        val formatterIn = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val date = formatterIn.parse(title)
        val formatterOut = SimpleDateFormat("MMM, dd yyyy", Locale.CANADA)
        holder.setText(R.id.tvTitleItem, formatterOut.format(date))
    }

    override fun getData(): String {
        return title
    }

}