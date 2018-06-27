package ca.six.daily.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Story {
    public String ga_prefix;
    public long id;
    public ArrayList<String> images;
    public String title;
    public int type;

    public Story(String jsonString) {
        try {
            init(new JSONObject(jsonString));
        } catch (Exception e) {
        }
    }

    public Story(JSONObject json) {
        init(json);
    }

    private void init(JSONObject json) {
        if (json != null) {
            ga_prefix = json.optString("ga_prefix");
            id = json.optLong("id");
            title = json.optString("title");
            type = json.optInt("type");

            JSONArray ary = json.optJSONArray("images");
            int size = ary.length();
            images = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                images.add(ary.optString(i));
            }
        }
    }

    public static ArrayList<Story> createWithJsonArray(JSONArray array) {
        if (array != null) {
            int len = array.length();
            ArrayList<Story> list = new ArrayList<Story>();
            for (int i = 0; i < len; i++) {
                JSONObject obj = array.optJSONObject(i);
                Story oneItem = new Story(obj);
                list.add(oneItem);
            }
            return list;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Story{" +
                "ga_prefix='" + ga_prefix + '\'' +
                ", id=" + id +
                ", images=" + images +
                ", title='" + title + '\'' +
                ", type=" + type +
                '}';
    }
}