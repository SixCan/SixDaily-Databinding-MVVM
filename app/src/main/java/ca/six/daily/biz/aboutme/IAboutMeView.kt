package ca.six.daily.biz.aboutme;

import ca.six.daily.data.CheckUpdateResponse

interface IAboutMeView {
    fun showNoUpdate()
    fun showUpdateMessage(resp: CheckUpdateResponse)
}

