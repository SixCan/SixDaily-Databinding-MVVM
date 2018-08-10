package ca.six.daily.biz.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import ca.six.daily.R
import ca.six.daily.biz.detail.DailyDetailActivity
import ca.six.daily.biz.home.viewmodel.DailyListViewModel
import ca.six.daily.biz.home.viewmodel.ViewModelFactory
import ca.six.daily.core.BaseActivity
import ca.six.daily.databinding.ActivityDailyListBinding
import ca.six.daily.utils.showToast
import ca.six.daily.view.*
import kotlinx.android.synthetic.main.activity_daily_list.*

// 第一屏内容(banner与list) ： https://news-at.zhihu.com/api/4/news/latest

class DailyListActivity : BaseActivity(), IDailyListView {
    val presenter by lazy {
        DailyListPresenter(this)
    }

    lateinit var listBinding : ActivityDailyListBinding
    lateinit var listModel: DailyListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_list)

        rvDailyList.layoutManager = LinearLayoutManager(this)
        rvDailyList.addItemDecoration(WhiteSpaceDivider())

        // for UI holders before we get the real data from server
//        val holderLists = arrayListOf(1, 2, 3, 4)
//        val tempAdapter = object : OneAdapter<Int>(R.layout.item_daily_list_holder) {
//            override fun apply(vh: RvViewHolder, t: Int, position: Int) {
//            }
//        }
//        tempAdapter.data = holderLists
//        rvDailyList.adapter = tempAdapter

        listBinding = ActivityDailyListBinding.inflate(layoutInflater)
        val factory = ViewModelFactory.instance
        listModel = ViewModelProviders.of(this, factory).get(DailyListViewModel::class.java)
        listBinding.viewmodel = listModel

        val dailyListAdapter = DailyListAdapter()
        rvDailyList.adapter = dailyListAdapter
        rvDailyList.addOnItemTouchListener(object : OnRvItemClickListener(rvDailyList) {
            override fun onItemClick(holder: RecyclerView.ViewHolder) {
                listModel.jumpToDetail(holder.adapterPosition)

                val tv = holder.itemView.findViewById(R.id.tvListItemTitle) as TextView
                tv.setTextColor(0xff999999.toInt())
            }
        })

        listModel.requestData(dailyListAdapter)

        subscribe()

    }

    override fun refresh(data: MutableList<ViewType<out Any>>) {

    }

    override fun onError(errorText: String) {
        showToast(errorText)
    }

    override fun jumpToDetilsPage(thisStoryID: Long, allIDs: LongArray) {

    }

    private fun subscribe() {
        listModel.selectedItem.observe(DailyListActivity@this, Observer {
            val detailIntent = Intent(this, DailyDetailActivity::class.java)
            detailIntent.putExtra("it_detailID", it!![0] as Long)
            detailIntent.putExtra("it_detailID_array", it[1] as LongArray)
            startActivity(detailIntent)
        })
    }

}

