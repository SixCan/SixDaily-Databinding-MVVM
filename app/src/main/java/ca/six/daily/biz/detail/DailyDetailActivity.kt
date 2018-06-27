package ca.six.daily.biz.detail

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import ca.six.daily.R
import ca.six.daily.core.BaseActivity
import kotlinx.android.synthetic.main.activity_daily_detail.*

// 详情页(id来自list屏)： https://news-at.zhihu.com/api/4/news/3892357   (应该是用WebView加载)
class DailyDetailActivity : BaseActivity() {
    var selectedId: Long = 0
    lateinit var ids: List<Long>
    private lateinit var detailAdapter: RvDetailsAdapter
    private lateinit var layoutManager: LinearLayoutManager

    val DEFAULT_ID = 3892357

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_detail)
        selectedId = intent.getLongExtra("it_detailID", DEFAULT_ID.toLong())
        ids = intent.getLongArrayExtra("it_detailID_array").asList()

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvList.layoutManager = layoutManager
        val snapHelper = DetailsSnapHelper()
        snapHelper.attachToRecyclerView(rvList)
        detailAdapter = RvDetailsAdapter(this, ids, selectedId, layoutManager)
        rvList.adapter = detailAdapter
        rvList.addOnScrollListener(DetailsOnScrollListener())
    }

    override fun onDestroy() {
        rvList.clearOnScrollListeners()
        super.onDestroy()
    }

    private var isMoveEnd: Boolean = false

    inner class DetailsOnScrollListener : RecyclerView.OnScrollListener() {
        private var afterDragging: Boolean = false

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    val currentPos = layoutManager.findFirstVisibleItemPosition()
                    if(afterDragging){
                    detailAdapter.changeCurrentPosition(currentPos, isMoveEnd)
                    }
                    println("idle..$currentPos")
                }
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                    afterDragging = true
                }
            }
            super.onScrollStateChanged(recyclerView, newState)
        }
    }

    inner class DetailsSnapHelper : LinearSnapHelper() {

        override fun onFling(velocityX: Int, velocityY: Int): Boolean {
            isMoveEnd = velocityX < 0
            return super.onFling(velocityX, velocityY)
        }
    }
}