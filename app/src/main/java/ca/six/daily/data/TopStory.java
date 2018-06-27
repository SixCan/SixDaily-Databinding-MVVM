package ca.six.daily.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TopStory {
    public String ga_prefix;
    public long id;
    public String image;
    public String title;
    public int type;

    public TopStory(JSONObject json) {
        if (json != null) {
            ga_prefix = json.optString("ga_prefix");
            id = json.optLong("id");
            image = json.optString("image");
            title = json.optString("title");
            type = json.optInt("type");
        }
    }

    public static ArrayList<TopStory> createWithJsonArray(JSONArray array) {
        if (array != null) {
            int len = array.length();
            ArrayList<TopStory> list = new ArrayList<TopStory>();
            for (int i = 0; i < len; i++) {
                JSONObject obj = array.optJSONObject(i);
                TopStory oneItem = new TopStory(obj);
                list.add(oneItem);
            }
            return list;
        }
        return null;
    }

}