package ca.six.daily.biz.detail

import ca.six.daily.data.DailyDetailResponse

/**
 * @copyright six.ca
 * Created by Xiaolin on 2017-07-10.
 */
interface IDailyDetailView {
    fun updateDetails(details: DailyDetailResponse)
}