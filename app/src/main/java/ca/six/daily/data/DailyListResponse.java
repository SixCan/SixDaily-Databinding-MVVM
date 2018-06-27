package ca.six.daily.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DailyListResponse {
    public String date;
    public ArrayList<Story> stories;
    public ArrayList<TopStory> top_stories;

    public DailyListResponse(String jsonStr) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            date = json.optString("date");

            JSONArray array = json.optJSONArray("stories");
            stories = Story.createWithJsonArray(array);

            JSONArray array2 = json.optJSONArray("top_stories");
            top_stories = TopStory.createWithJsonArray(array);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}