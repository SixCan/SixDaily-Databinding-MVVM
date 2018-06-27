package ca.six.daily.utils

import android.widget.Toast
import ca.six.daily.core.BaseApp

fun showToast(text: String) {
    Toast.makeText(BaseApp.app, text, Toast.LENGTH_SHORT).show()
}