package ca.six.daily.biz.home.viewmodel

import android.databinding.DataBindingUtil
import ca.six.daily.R
import ca.six.daily.databinding.ItemTitleBinding
import ca.six.daily.view.RvViewHolder
import ca.six.daily.view.ViewType

class ListTitleViewModel(val title: String) : ViewType<String> {

    override fun getViewType(): Int {
        return R.layout.item_title
    }

    override fun bind(holder: RvViewHolder) {

        val binding : ItemTitleBinding = DataBindingUtil.getBinding(holder.itemView)!!
        binding.viewmodel = this
        binding.executePendingBindings()

    }

    override fun getData(): String {
        return title
    }

}