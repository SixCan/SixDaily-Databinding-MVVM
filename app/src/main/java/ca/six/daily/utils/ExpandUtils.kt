package ca.six.daily.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.widget.EditText

fun Activity.jump(clz: Class<out Activity>) {
    val it = Intent(this, clz)
    this.startActivity(it)
}

fun Activity.jump(clz: Class<out Activity>, args: Map<String, String>) {
    val it = Intent(this, clz)
    for ((k, v) in args) {
        it.putExtra(k, v)
    }
    this.startActivity(it)
}

fun EditText.text(): String {
    return this.getText()?.toString() ?: "" //this是指EditText对象。因为这是猴子补丁
}

fun Context.dp2px(dp: Float): Int {
    val res = this.resources;
    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.displayMetrics)
    return Math.round(px)
}
