package ca.six.daily.view

import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent

abstract class OnRvItemClickListener(private val rv: RecyclerView) : RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetectorCompat

    init {
        gestureDetector = GestureDetectorCompat(rv.context, RvGestureListener())
    }

    private inner class RvGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent) {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null) {
                val vh = rv.getChildViewHolder(child)
                onLongClick(vh)
            }
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null) {
                val vh = rv.getChildViewHolder(child)
                onItemClick(vh)
            }
            return true //@return true if the event is consumed, else false
        }
    }

    fun onLongClick(vh: RecyclerView.ViewHolder) {}

    // ========================= OnItemTouchListener =================================
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(e)
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        gestureDetector.onTouchEvent(e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    // ========================= abstract methods =================================
    abstract fun onItemClick(holder: RecyclerView.ViewHolder)

}