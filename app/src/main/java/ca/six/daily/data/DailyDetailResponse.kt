package ca.six.daily.data

import org.json.JSONObject

/**
 * @copyright six.ca
 * Created by Xiaolin on 2017-07-10.
 */
class DailyDetailResponse(jsonStr: String) {
    var body: String
    var image: String
    var title: String
    var id: Long
    var cssVer: String

    val REDUNDANT_PART = "<div class=\"img-place-holder\"></div>\n\n\n\n"

    init {
        val json = JSONObject(jsonStr)
        body = json.optString("body").replace(REDUNDANT_PART, "")
        image = json.optString("image")
        title = json.optString("title")
        id = json.optLong("id", 0)
        val css = json.optJSONArray("css")[0] as String
        cssVer = if(css.isNotEmpty()) css.substring(css.indexOf("=") + 1, css.length) else ""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as DailyDetailResponse

        if (body != other.body) return false
        if (image != other.image) return false
        if (title != other.title) return false
        if (id != other.id) return false
        if (cssVer != other.cssVer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = body.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + cssVer.hashCode()
        return result
    }


}