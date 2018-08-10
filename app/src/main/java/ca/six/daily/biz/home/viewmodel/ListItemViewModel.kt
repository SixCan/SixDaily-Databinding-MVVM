package ca.six.daily.biz.home.viewmodel

import android.databinding.DataBindingUtil
import ca.six.daily.R
import ca.six.daily.data.Story
import ca.six.daily.databinding.ItemDailyListBinding
import ca.six.daily.view.RvViewHolder
import ca.six.daily.view.ViewType

class ListItemViewModel(val story: Story) : ViewType<Story> {

    override fun getViewType(): Int {
        return R.layout.item_daily_list
    }

    override fun bind(holder: RvViewHolder) {

        val binding : ItemDailyListBinding = DataBindingUtil.getBinding(holder.itemView)!!
        binding.viewmodel = this
        binding.executePendingBindings()

    }

    override fun getData(): Story {
        return story
    }

}