package ca.six.daily.biz.home.viewmodel

import android.widget.ImageView
import ca.six.daily.R
import ca.six.daily.data.Story
import ca.six.daily.view.RvViewHolder
import ca.six.daily.view.ViewType
import com.squareup.picasso.Picasso

class ListItemViewModel(val story: Story) : ViewType<Story> {

    override fun getViewType(): Int {
        return R.layout.item_daily_list
    }

    override fun bind(holder: RvViewHolder) {
        holder.setText(R.id.tvListItemTitle, story.title)

        Picasso.with(holder.itemView.context)
                .load(story.images[0])
                .into(holder.getView<ImageView>(R.id.ivListItemTitle))
    }

    override fun getData(): Story {
        return story
    }


}