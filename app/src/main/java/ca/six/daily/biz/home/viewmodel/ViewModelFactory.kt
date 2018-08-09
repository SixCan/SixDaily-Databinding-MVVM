package ca.six.daily.biz.home.viewmodel

import android.arch.lifecycle.ViewModelProvider

/**
 * @CopyRight six.ca
 * Created by Heavens on 2018-08-08.
 */
class ViewModelFactory private constructor (): ViewModelProvider.NewInstanceFactory() {
    companion object {
        val instance: ViewModelFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ViewModelFactory()
        }
    }

    fun <T> create(modelCalss: Class<T>): T {
        if(modelCalss.isAssignableFrom(DailyListViewModel::class.java)) {
            return DailyListViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class ${modelCalss.name}")
    }

}