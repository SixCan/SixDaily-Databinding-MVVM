package ca.six.daily.biz.aboutme

import android.app.AlertDialog
import android.os.Bundle
import ca.six.daily.core.BaseActivity
import ca.six.daily.R
import ca.six.daily.core.network.HttpEngine
import ca.six.daily.data.CheckUpdateResponse
import ca.six.daily.utils.showToast

class AboutMeActivity : BaseActivity(), IAboutMeView {

    val presenter : AboutMePresenter by lazy { AboutMePresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)

        // check for update : version/android/2.5.4
        presenter.checkForUpdate()
    }

    override fun showUpdateMessage(resp : CheckUpdateResponse) {
        val dialog = AlertDialog.Builder(this)
                .setMessage(resp.message)
                .setPositiveButton("Download", { dialog, which ->
                    println("szw click okay. Start updating.")
//                    HttpEngine.downloadFile("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2859174087,963187950&fm=23&gp=0.jpg", "a000.jpg")
                    HttpEngine.downloadFile(resp.url, "SixDaily.apk")
                })
                .create()
        dialog.show()
    }

    override fun showNoUpdate() {
        showToast("Current vesrion is the latest version!")
    }
}


