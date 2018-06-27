package ca.six.daily.core

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import okhttp3.OkHttpClient
import kotlin.properties.Delegates
class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // This process is dedicated to LeakCanary for heap analysis. You should not init your app in this process.
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        app = this;

    }

    companion object {
        var app: Context by Delegates.notNull<Context>()
    }
}

