package ca.six.daily.view

interface ViewType<T> {
    fun getViewType(): Int
    fun bind(holder: RvViewHolder)
    fun getData(): T
}
