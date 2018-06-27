package ca.six.daily.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class WhiteSpaceDivider : RecyclerView.ItemDecoration() {
    val height: Int = 35
    val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = 0xcccccc
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        var left: Int
        var right: Int
        var top: Int
        var bottom: Int

        left = parent.paddingLeft
        right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0..childCount - 1 - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            top = child.bottom + params.bottomMargin
            bottom = top + height
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State?) {
        outRect.set(0, 0, 0, 25)
    }
}