package ca.six.daily.biz.detail

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import ca.six.daily.R
import ca.six.daily.data.DailyDetailResponse
import ca.six.daily.view.RvViewHolder
import com.squareup.picasso.Picasso

/**
 * @author hellenxu
 * @date 2017-07-17
 * Copyright 2017 Six. All rights reserved.
 */
class RvDetailsAdapter(val ctx: Context, val ids: List<Long>, selectedId: Long, val layoutManager: LinearLayoutManager) :
        RecyclerView.Adapter<RvViewHolder>(), IDailyDetailView {
    private var data = ArrayList<DailyDetailResponse>()
    private var dataRight : ArrayList<Long> = ArrayList<Long>()
    private val presenter: DailyDetailPresenter = DailyDetailPresenter(this)
    private var separatedPos: Int = 0
    private var currentPos: Int = 0
    private var refreshCount: Int = 0
    private val emptyJsonStr = "{\"body\":\"\",\"image_source\":\"\",\"title\":\"\",\"image\":\"file path\",\"share_url\":\"\",\"js\":[],\"ga_prefix\":\"080219\",\"images\":[\"\"],\"type\":0,\"id\":0,\"css\":[\"\"]}"

    init {
        separatedPos = if (ids.indexOf(selectedId) > -1) ids.indexOf(selectedId) else 0
        ids.subList(separatedPos, ids.size).forEach {
            dataRight.add(it)
        }
        refreshCount = ids.size - dataRight.size

        dataRight.forEach {
            val item = DailyDetailResponse(emptyJsonStr)
            data.add(item)
        }
        presenter.getDetails(selectedId)
    }

    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {
        val size = data.size
        val banner = holder.getView<ImageView>(R.id.ivBanner)
        val content = holder.getView<WebView>(R.id.wvContent)
        content.settings.javaScriptEnabled = true
        if (size > 0 && position < size) {
            val item = data[position]
            Picasso.with(ctx)
                    .load(item.image)
                    .error(R.drawable.loading_placeholder)
                    .placeholder(R.drawable.loading_placeholder)
                    .into(banner)
            content.addJavascriptInterface(HtmlLoader(item.body, item.cssVer), "loader")
        }
        content.loadUrl("file:///android_asset/details.html")
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
        return RvViewHolder.createViewHolder(parent, R.layout.item_daily_details)
    }

    override fun updateDetails(details: DailyDetailResponse) {
        data[currentPos] = details
        notifyDataSetChanged()
    }

    fun changeCurrentPosition(pos: Int, isMoveEnd: Boolean) {
        println("change-pos: $pos")
        val selectedId: Long
        if(isMoveEnd && pos == 0 && refreshCount > 0){
            data.add(0, DailyDetailResponse(emptyJsonStr))
            refreshCount -= 1
            dataRight.add(0, ids[refreshCount])
            selectedId = dataRight[0]
            currentPos = 0
            println("xxl-add")
        } else {
            selectedId = dataRight[pos]
            currentPos = pos
            println("xxl-read")
        }

        println("change-current pos:$currentPos")
        println("change-selected: $selectedId")

        var isCached = false
        data.forEach {
            if (it.id == selectedId) {
                isCached = true
            }
        }
        if (!isCached) {
            presenter.getDetails(selectedId)
        }
    }
}