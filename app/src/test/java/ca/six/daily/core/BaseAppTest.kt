package ca.six.daily.core

import android.os.Build
import ca.six.daily.BuildConfig
import ca.six.daily.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(Build.VERSION_CODES.LOLLIPOP), application = BaseApp::class)
class BaseAppTest {

    @Test
    fun testContextNotNull() {
        assertNotNull(BaseApp.app)
    }


    @Test
    fun testContextObjectIsRight(){
        var app = BaseApp.app.getString(R.string.app_name)
        assertEquals("SixDaily2", app)
    }



}
